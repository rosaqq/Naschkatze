package net.sknv.nkmod.blocks.machines;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.sknv.nkmod.RegistrateHandler;
import net.sknv.nkmod.blocks.machines.base.AbstractMachineTile;

import javax.annotation.Nonnull;


public class GrinderTile extends AbstractMachineTile {

    public GrinderTile(TileEntityType<?> type) {
        super(type, GrinderContainer::new);
    }

    @Nonnull
    @Override
    protected IItemHandler createHandler() {
        return new ItemStackHandler(1) {
            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return stack.getItem() == RegistrateHandler.URANINITE_ORE.get().asItem();
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if (stack.getItem() != RegistrateHandler.URANINITE_ORE.get().asItem()) {
                    return stack;
                }
                return super.insertItem(slot, stack, simulate);
            }
        };
    }
}
