package net.sknv.nkmod.blocks.machines.base;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
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

    // set for all machines
    // vanilla inv uses slots 0-35
    private static final int HOTBAR_FIRST_SLOT_INDEX = 0;
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_FIRST_SLOT_INDEX = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = 27;

    // this should be defined in the subclass
    private static final int FIRST_INPUT_SLOT_INDEX = 36;
    private static final int INPUT_SLOTS_COUNT = 1;
    private static final int FIRST_OUTPUT_SLOT_INDEX = FIRST_INPUT_SLOT_INDEX + INPUT_SLOTS_COUNT;
    private static final int  OUTPUT_SLOTS_COUNT = 1;

    protected static int INPUT_SLOT;
    protected static int OUTPUT_SLOT;


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

        layoutPlayerInventorySlots();
        trackIntArray(tileEntity.machineStateData);
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

    /**
     * Copied and refactored from AbstractFurnaceContainer
     * This is where you specify what happens when a player shift clicks a slot in the GUI.<br>
     * At the very least must be overridden to return ItemStack.EMPTY or game will crash on shift+click
     * @param playerIn
     * @param index
     * @return ItemStack.EMPTY if slot is empty, else a copy of the source stack.
     */
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        Slot sourceSlot = this.inventorySlots.get(index);
        if (sourceSlot == null || !sourceSlot.getHasStack()) return ItemStack.EMPTY;

        ItemStack sourceItemStack = sourceSlot.getStack();
        ItemStack sourceStackBeforeMerge = sourceItemStack.copy();
        SlotZone sourceZone = SlotZone.getZoneFromIndex(index);

        boolean hasRecipe = false;
        boolean noFitsInputZone = false;

        if (sourceZone == SlotZone.OUTPUT_ZONE) {
            if (!mergeInto(SlotZone.PLAYER_HOTBAR, sourceItemStack, true) &&  !mergeInto(SlotZone.PLAYER_MAIN_INVENTORY, sourceItemStack, true)) {
                return ItemStack.EMPTY;
            }

            sourceSlot.onSlotChange(sourceItemStack, sourceStackBeforeMerge);
        }
        // This means it must have come from inv
        else if (sourceZone != SlotZone.INPUT_ZONE) {
            // If the recipe doesn't return EMPTY -> there is a recipe
            if (!this.tileEntity.getSmeltingResultForItem(playerIn.getEntityWorld(), sourceItemStack).isEmpty()) {
                hasRecipe = true;
                // If can't merge to input zone
                if (!mergeInto(SlotZone.INPUT_ZONE, sourceItemStack, false)) noFitsInputZone = true;
            }
            // Execution is supposed to enter this if when:
            // a) there is no recipe
            // b) there is a recipe but stack doesn't fit input slots
            if (!hasRecipe || noFitsInputZone) {
                // If coming from hotbar
                if (sourceZone == SlotZone.PLAYER_HOTBAR) {
                    // If doesn't fit in main inv, return EMPTY
                    if (!mergeInto(SlotZone.PLAYER_MAIN_INVENTORY, sourceItemStack, false)) return ItemStack.EMPTY;
                }
                // Else must be coming from main inv
                // If doesn't fit into hotbar, return EMPTY
                if (!mergeInto(SlotZone.PLAYER_HOTBAR, sourceItemStack, false)) return ItemStack.EMPTY;
            }

            // This happens when there is no recipe and can't move hotbar <-> main inv
            return ItemStack.EMPTY;
        }

        // This means it must have come from INPUT_ZONE
        else {
            sourceSlot.putStack(ItemStack.EMPTY);
        }

        // If source stack is empty (the entire stack was moved) set slot contents to empty
        if (sourceItemStack.isEmpty()) {
            sourceSlot.putStack(ItemStack.EMPTY);
        } else {
            // If can't put into hotbar
            if (!mergeInto(SlotZone.PLAYER_HOTBAR, sourceItemStack, false))
                // If can't put into main inv
                if (!mergeInto(SlotZone.PLAYER_MAIN_INVENTORY, sourceItemStack, false))
                    return ItemStack.EMPTY;
        }

        // if source stack is still the same as before the merge, the transfer failed somehow?  not expected.
        if (sourceItemStack.getCount() == sourceStackBeforeMerge.getCount()) {
            return ItemStack.EMPTY;
        }

        sourceSlot.onTake(playerIn, sourceItemStack);
        return sourceStackBeforeMerge;
    }

    /**
     * Try to merge from the given source ItemStack into the given SlotZone.
     * @param destinationZone the zone to merge into
     * @param sourceItemStack the itemstack to merge from
     * @param fillFromEnd if true: try to merge from the end of the zone instead of from the start
     * @return true if a successful transfer occurred
     */
    private boolean mergeInto(SlotZone destinationZone, ItemStack sourceItemStack, boolean fillFromEnd) {
        return mergeItemStack(sourceItemStack, destinationZone.firstIndex, destinationZone.lastIndexPlus1, fillFromEnd);
    }



    private void layoutPlayerInventorySlots() {
        // Player inventory
        int leftCol = 8;
        int topRow = 84;

        // add main inv, index 9-35
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        // Hotbar, index 0-9
        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }

    /**
     * Adds a row of slots.
     *
     * @param handler
     * @param index Index for the first slot. Gets auto incremented for each sequential slot.
     * @param x Horizontal position for the first slot.
     * @param y Vertical position for the first slot.
     * @param amount Number of slots to add.
     * @param dx Change in x position for next slot.
     * @return The last added slot's index.
     */
    private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0 ; i < amount ; i++) {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    /**
     * Adds multiple rows of slots, creating a grid.
     *
     * @param handler
     * @param index Index of the first slot.
     * @param x Horizontal position for the first slot.
     * @param y Vertical position for the first slot.
     * @param rowSize How many slots for each row.
     * @param dx Change in x position for next slot.
     * @param rowCount How many rows.
     * @param dy Change in y position for next slot.
     * @return The last added slot's index.
     */
    private int addSlotBox(IItemHandler handler, int index, int x, int y, int rowSize, int dx, int rowCount, int dy) {
        for (int j = 0 ; j < rowCount ; j++) {
            index = addSlotRange(handler, index, x, y, rowSize, dx);
            y += dy;
        }
        return index;
    }

    public int getBurnLeftScaled() {
        return 0;
    }
    public boolean isBurning() {
        return this.tileEntity.isBurning();
        // return this.furnaceData.get(0) > 0;
    }

    /**
     * Helper enum to make the code more readable
     */
    private enum SlotZone {
        INPUT_ZONE(FIRST_INPUT_SLOT_INDEX, INPUT_SLOTS_COUNT),
        OUTPUT_ZONE(FIRST_OUTPUT_SLOT_INDEX, OUTPUT_SLOTS_COUNT),
        PLAYER_MAIN_INVENTORY(PLAYER_INVENTORY_FIRST_SLOT_INDEX, PLAYER_INVENTORY_SLOT_COUNT),
        PLAYER_HOTBAR(HOTBAR_FIRST_SLOT_INDEX, HOTBAR_SLOT_COUNT);

        SlotZone(int firstIndex, int numberOfSlots) {
            this.firstIndex = firstIndex;
            this.slotCount = numberOfSlots;
            this.lastIndexPlus1 = firstIndex + numberOfSlots;
        }

        public final int firstIndex;
        public final int slotCount;
        public final int lastIndexPlus1;

        public static SlotZone getZoneFromIndex(int slotIndex) {
            for (SlotZone slotZone : SlotZone.values()) {
                if (slotIndex >= slotZone.firstIndex && slotIndex <= slotZone.lastIndexPlus1) return slotZone;
            }
            throw new IndexOutOfBoundsException("Unexpected slotIndex");
        }
    }
}
