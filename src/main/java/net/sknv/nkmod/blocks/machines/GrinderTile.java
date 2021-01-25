package net.sknv.nkmod.blocks.machines;

import com.google.common.collect.Lists;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.sknv.nkmod.RegistrateHandler;
import net.sknv.nkmod.blocks.machines.base.AbstractMachineTile;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class GrinderTile extends AbstractMachineTile {

    private final int processTime = 10;
    private int timeLeft = processTime;
    private boolean runProcess = false;
    //protected final IRecipeType<? extends AbstractCookingRecipe> recipeType;

    public GrinderTile(TileEntityType<?> type) {
        super(type, GrinderContainer::new);
        this.items = NonNullList.withSize(3, ItemStack.EMPTY);
        //this.recipeType = recipeType
    }

    @Override
    public void tick() {
        if (runProcess) {
            timeLeft--;
            if (timeLeft<=0) finishProcess();
        }
    }

    private void finishProcess() {
        //IRecipe<?> irecipe = this.world.getRecipeManager().getRecipe((IRecipeType<AbstractCookingRecipe>)this.recipeType, this, this.world).orElse(null);
        smelt();
    }

    @Nonnull
    @Override
    protected IItemHandler createHandler() {
        return new ItemStackHandler(3) {
            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return stack.getItem() == RegistrateHandler.URANINITE_ORE.get().asItem();
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                // if not uraninite ore, returns stack
                if (stack.getItem() != RegistrateHandler.URANINITE_ORE.get().asItem()) {
                    return stack;
                }
                // else starts grinding
                runProcess = true;
                return super.insertItem(slot, stack, simulate);
            }

            @Nonnull
            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                if (getStackInSlot(slot).getItem() == RegistrateHandler.URANINITE_ORE.get().asItem()) {
                    runProcess = false;
                }
                return super.extractItem(slot, amount, simulate);
            }
        };
    }

    @Override
    protected boolean isBurning() {
        return runProcess;
    }

    private void smelt() {
        //todo: not detecting which item is in slot, this prints air
        LogManager.getLogger().error("tryna smelt itemstack: " + this.items.get(0));
        //ItemStack itemstack = this.items.get(0);
        //ItemStack itemstack1 = recipe.getRecipeOutput();
        //ItemStack itemstack2 = this.items.get(2);

        //LogManager.getLogger().error("itemstack: " + itemstack.getItem());
        //LogManager.getLogger().error("itemstack1: " + itemstack1.getItem());
        //LogManager.getLogger().error("itemstack2: " + itemstack2.getItem());
    }

    public int getTimeLeft() {
        return timeLeft;
    }
}
