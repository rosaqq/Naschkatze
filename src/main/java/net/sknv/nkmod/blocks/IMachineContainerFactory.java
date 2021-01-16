package net.sknv.nkmod.blocks;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IMachineContainerFactory<T extends Container> {
    T create(int windowId, World worldIn, BlockPos pos, PlayerInventory inv, PlayerEntity entity);
}