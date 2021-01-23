package net.sknv.nkmod.blocks.machines.base;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.NonNullConsumer;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.sknv.nkmod.RegistrateHandler;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

//todo: javadoc - update container registration when type gets included in the ctor
/**
 * Subclass to create a specific Container type for a {@link MachineBlock}, {@code XXContainer}.<br>
 * Register with:<br>
 * <pre>
 * XX_CONTAINER = REGISTRATE.container(
 *         (type, windowId, inv, data) -> new XXContainer(windowId, inv, data.readBlockPos()),
 *         () -> new MachineScreenFactory<>("textures/gui/my_gui.png")).register();
 * </pre>
 * Note: If you register this right after it's respective block, it piggybacks on the block's {@code .object()} call so you don't need to include it. Full example on {@link RegistrateHandler}<br>
 * Check the constructor javadoc: {@link AbstractMachineContainer#AbstractMachineContainer}.
 *
 */
public abstract class AbstractMachineContainer extends Container {

    protected TileEntity tileEntity;
    protected PlayerEntity playerEntity;
    protected IItemHandler playerInventory;
    protected RegistryEntry<MachineBlock> blockEntry;

    /**
     * Check class javadoc: {@link AbstractMachineContainer}.
     * @param type Container type, get it from {@link RegistrateHandler}.XX_CONTAINER.get()
     * @param blockEntry The respective Block RegistryEntry, pass {@code RegistrateHandler.XX_BLOCK}
     * @param windowId Subclass constructor param, pass through
     * @param inv Subclass constructor param, pass through
     * @param pos Subclass constructor param, pass through
     */
    public AbstractMachineContainer(@Nullable ContainerType<?> type, RegistryEntry<MachineBlock> blockEntry, int windowId, PlayerInventory inv, BlockPos pos) {
        super(type, windowId);
        tileEntity = inv.player.getEntityWorld().getTileEntity(pos);
        this.playerEntity = inv.player;
        this.playerInventory = new InvWrapper(inv);
        this.blockEntry = blockEntry;

        if (tileEntity != null)
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(itemHandlerConsumerProvider());

        layoutPlayerInventorySlots(8, 84);
    }

    /**
     * Implementation example:
     * <pre>{@code return h -> {
     *     // Furnace input slot
     *     addSlot(new SlotItemHandler(h, 0, 56, 17));
     * };}</pre>
     * @return Something that consumes an ItemHandler.
     */
    public abstract NonNullConsumer<IItemHandler> itemHandlerConsumerProvider();

    @Override
    @ParametersAreNonnullByDefault
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(IWorldPosCallable.of(Objects.requireNonNull(tileEntity.getWorld()), tileEntity.getPos()), playerEntity, blockEntry.get());
    }

    private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x+=dx;
            index++;
        }
        return index;
    }

    private int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0; j < verAmount; j++) {
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y+=dy;
        }
        return index;
    }

    protected void layoutPlayerInventorySlots(int leftCol, int topRow) {
        // Player inventory
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);
        // Hotbar
        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }
}
