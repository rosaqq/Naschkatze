package net.sknv.nkmod.blocks.machines;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.sknv.nkmod.blocks.machines.base.AbstractMachineTile;

import javax.annotation.Nonnull;

import static net.sknv.nkmod.RegistryHandler.*;

public class GrinderTile extends AbstractMachineTile {

    public GrinderTile() {
        super(GRINDER_TILE.get(), GrinderContainer::new);
    }

    @Nonnull
    @Override
    protected IItemHandler createHandler() {
        return new ItemStackHandler(1) {
            @Override public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return stack.getItem() == URANINITE_ORE_ITEM.get();
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if (stack.getItem() != URANINITE_ORE_ITEM.get()) {
                    return stack;
                }
                return super.insertItem(slot, stack, simulate);
            }
        };
    }
}
