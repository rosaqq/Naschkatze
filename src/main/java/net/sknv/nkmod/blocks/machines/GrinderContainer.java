package net.sknv.nkmod.blocks.machines;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.NonNullConsumer;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.sknv.nkmod.Naschkatze;
import net.sknv.nkmod.blocks.machines.base.AbstractMachineContainer;


public class GrinderContainer extends AbstractMachineContainer {

    // GRINDER_CONTAINER = new GrinderBlockContainer()
    // public GrinderBlockContainer().. {
    //     super(GRINDER_CONTAINER.get()
    // }

    // new (windowId, inv.player.getEntityWorld(), data.readBlockPos(), inv, inv.player)
    // (ContainerType<T> var1, int var2, PlayerInventory var3)
    // old ctor
    public GrinderContainer(int windowId, PlayerInventory inv, BlockPos pos) {
        super(Naschkatze.GRINDER_CONTAINER.get(), Naschkatze.GRINDER, windowId, inv, pos);
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
