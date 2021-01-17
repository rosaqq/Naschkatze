package net.sknv.nkmod.blocks.machines;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.sknv.nkmod.blocks.machines.base.AbstractMachineTile;

import javax.annotation.Nonnull;

import static net.sknv.nkmod.RegistryHandler.*;

public class ReactionChamberTile extends AbstractMachineTile {

    public ReactionChamberTile() {
        super(REACTIONCHAMBER_TILE.get(), GrinderContainer::new);
    }

    @Nonnull
    @Override
    protected IItemHandler createHandler() {
        return null;
    }
}
