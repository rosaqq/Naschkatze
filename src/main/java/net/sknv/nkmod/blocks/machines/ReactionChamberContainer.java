package net.sknv.nkmod.blocks.machines;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.NonNullConsumer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.sknv.nkmod.blocks.machines.base.AbstractMachineContainer;

import javax.annotation.Nullable;

import static net.sknv.nkmod.RegistryHandler.*;

public class ReactionChamberContainer extends AbstractMachineContainer {

    public ReactionChamberContainer(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
        super(REACTIONCHAMBER_CONTAINER.get(), REACTIONCHAMBER, windowId, world, pos, playerInventory, player);;
    }

    @Override
    public NonNullConsumer<IItemHandler> itemHandlerConsumerProvider() {
        return h -> {
            // Furnace input slot
            addSlot(new SlotItemHandler(h, 0, 56, 17));
            // Furnace fuel slot
            // todo: item handler for fuel
            //addSlot(new SlotItemHandler(h, 1, 56, 53));
            // Furnace result slot
            // todo: item handler / code for output
            //addSlot(new SlotItemHandler(h, 2, 116, 35));

        };
    }
}
