package net.sknv.nkmod.blocks.machines.base;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * This interface is required because there exists no Forge version with these specific parameters.<br>
 * This is used in the {@link AbstractMachineTile} constructor so it recognizes {@code XXBlockContainer:new} as a container factory.
 */
public interface IMachineContainerFactory {
    Container create(int windowId, World worldIn, BlockPos pos, PlayerInventory playerInv, PlayerEntity playerEntity);
}