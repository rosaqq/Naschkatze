package net.sknv.nkmod.blocks.machines.base;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
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

    protected AbstractMachineTile tileEntity;
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
        tileEntity = (AbstractMachineTile) inv.player.getEntityWorld().getTileEntity(pos);
        this.playerEntity = inv.player;
        this.playerInventory = new InvWrapper(inv);
        this.blockEntry = blockEntry;

        if (tileEntity != null)
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(itemHandlerConsumerProvider());

        bindPlayerInventory(inv);
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

    protected void bindPlayerInventory(PlayerInventory inventory) {
        int xOffset = 8;
        int yOffset = 84;

        int i;
        for(i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, xOffset + j * 18, yOffset + i * 18));
            }
        }

        for(i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inventory, i, xOffset + i * 18, yOffset + 58));
        }

    }

    public int getBurnLeftScaled() {
        int i = this.tileEntity.getTimeLeft();
        if (i == 0) {
            i = 200;
        }

        return this.tileEntity.getTimeLeft() * 13 / i;
    }
    public boolean isBurning() {
        return this.tileEntity.isBurning();
        // return this.furnaceData.get(0) > 0;
    }
}
