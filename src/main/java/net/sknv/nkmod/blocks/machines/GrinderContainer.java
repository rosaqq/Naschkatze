package net.sknv.nkmod.blocks.machines;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.NonNullConsumer;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.sknv.nkmod.blocks.machines.base.AbstractMachineContainer;

import static net.sknv.nkmod.RegistryHandler.GRINDER;
import static net.sknv.nkmod.RegistryHandler.GRINDER_CONTAINER;

public class GrinderContainer extends AbstractMachineContainer {

    // GRINDER_CONTAINER = new GrinderBlockContainer()
    // public GrinderBlockContainer().. {
    //     super(GRINDER_CONTAINER.get()
    // }

    // new GrinderB
    public GrinderContainer(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
        super(GRINDER_CONTAINER.get(), GRINDER, windowId, world, pos, playerInventory, player);
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
