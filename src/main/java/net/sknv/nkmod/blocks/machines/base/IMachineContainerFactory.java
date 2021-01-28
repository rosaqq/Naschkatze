package net.sknv.nkmod.blocks.machines.base;

import jdk.nashorn.internal.ir.Block;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.math.BlockPos;

/**
 * This interface is required because there exists no Forge/Registrate version with these specific parameters.<br>
 * This is used in the {@link AbstractMachineTile} constructor so it recognizes {@code XXContainer::new} as a container factory.
 */
public interface IMachineContainerFactory {
    Container create(int windowID, PlayerInventory inv, BlockPos pos);
    // Container createContainerServerSide(int windowID, PlayerInventory inv, BlockPos pos, MachineContents inputContents, MachineContents outputContents, MachineStateData machineStateData);
}