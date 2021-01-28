package net.sknv.nkmod.blocks.machines.base;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.ItemStackHandler;

import java.util.function.Predicate;

/**
 * Based on <a href="https://github.com/TheGreyGhost/MinecraftByExample/blob/master/src/main/java/minecraftbyexample/mbe31_inventory_furnace/FurnaceZoneContents.java">MBE31-FurnaceZoneContents.java</a>,
 * created by TGG on 4/04/2020.<br>
 *
 * This class is used to encapsulate the contents of the one of the zones of the machine (eg input zone, output zone)
 * and provide the link between the parent TileEntity and the Container:
 * <ol>
 *     <li>stores information about the items in the machine: allows the container to manipulate the data stored in the tile entity</li>
 *     <li>provides a way for the container to ask the TileEntity if certain actions are permitted (eg isUsableByPlayer, isItemValidForSlot)</li>
 *     <li>provides a way for the container to notify the TileEntity that the container has changed (eg markDirty, openInventory)</li>
 * </ol>
 * Typical usage for a TileEntity which needs to store Items:
 * <ol>
 *     <li>When constructing the TileEntity, create and store a MachineContents using createForTileEntity()</li>
 *     <li>
 *         In your ContainerType<AbstractMachineContainer>, create a MachineContents using createForClientSideContainer()
 *         and pass it to the constructor of your client-side Container.
 *     </li>
 *     <li>
 *         In your TileEntity write() and read() methods, call the serializeNBT() and deserializeNBT() methods;
 *         Vanilla and the Container code will take care of everything else.
 *     </li>
 * </ol>
 */
public class MachineContents implements IInventory {

    /**
     * Used to call the lambdas below.<br>
     * Note from MBE is: <i>Some folks use Runnable, but I prefer not to use it for non-thread-related tasks.</i><br>
     * Todo: is there an existing @FunctionalInterface that would work here?
     */
    @FunctionalInterface
    public interface Notify {   //
        void invoke();
    }
    /**
     * The function that the Container should call in order to tell the parent TE that the contents of its inventory have changed.
     * Default is "do nothing".
     */
    private Notify markDirtyNotificationLambda = () -> {};
    /**
     * The function that the Container should call in order to tell the parent TE that it has been OPENED by a player.<br>
     * Default is "do nothing".
     */
    private Notify openInventoryNotificationLambda = () -> {};
    /**
     * The function that the Container should call in order to tell the parent TE that it has been CLOSED by a player.<br>
     * Default is "do nothing".
     */
    private Notify closeInventoryNotificationLambda = () -> {};

    /**
     * The function that the Container should call in order to decide if the given player can access it or not.
     * The lambda function is only used on server side.<br>
     * Default is "true".
     */
    private Predicate<PlayerEntity> canPlayerAccessInventoryLambda = x -> true;
    // todo: I feel this field does not have a very intuitive name. What is it used for?
    private final ItemStackHandler machineComponentContents;

    /**
     * Use this constructor to create a MachineContents which is linked to its parent TileEntity.<br>
     * On the server, this link will be used by the Container to request information and provide notifications to the parent.<br>
     * On the client, the link will be unused.<br>
     * There are additional notification lambdas available; these two are explicitly specified because your TileEntity will
     * nearly always need to implement at least these two.<br>
     * @param size  The max number of ItemStacks in the inventory
     * @param canPlayerAccessInventoryLambda The function that the container should call in order to decide if the given player
     *                                       can access the container's contents not.  Usually, this is a check to see
     *                                       if the player is closer than 8 blocks away.
     * @param markDirtyNotificationLambda  The function that the container should call in order to tell the parent TileEntity
     *                                     that the contents of its inventory have been changed and need to be saved. Usually,
     *                                     this is TileEntity::markDirty.
     * @return the new MachineContents.
     */

    public static MachineContents createForTileEntity(int size, Predicate<PlayerEntity> canPlayerAccessInventoryLambda, Notify markDirtyNotificationLambda) {
        return new MachineContents(size, canPlayerAccessInventoryLambda, markDirtyNotificationLambda);
    }

    /**
     * Use this constructor to create a MachineContents which is not linked to any parent TileEntity; i.e.
     * is used by the client side container:
     * <ul>
     *     <li>Does not permanently store items.</li>
     *     <li>Cannot ask questions/provide notifications to a parent TileEntity.</li>
     * </ul>
     * @param size  The max number of ItemStacks in the inventory
     * @return The new MachineContents
     */
    public static MachineContents createForClientSideContainer(int size) {
        return new MachineContents(size);
    }

    // todo: secknv - note I believe I should pass my custom ItemHandler instead of calling new ItemStackHandler(size)
    private MachineContents(int size) {
        this.machineComponentContents = new ItemStackHandler(size);
    }
    // todo: same as above
    private MachineContents(int size, Predicate<PlayerEntity> canPlayerAccessInventoryLambda, Notify markDirtyNotificationLambda) {
        this.machineComponentContents = new ItemStackHandler(size);
        this.canPlayerAccessInventoryLambda = canPlayerAccessInventoryLambda;
        this.markDirtyNotificationLambda = markDirtyNotificationLambda;
    }

    //--------- Methods used to load / save the contents to NBT
    /**
     * Writes the chest contents to a CompoundNBT tag (used to save the contents to disk)
     * @return the tag containing the contents
     */
    public CompoundNBT serializeNBT()  {
        return machineComponentContents.serializeNBT();
    }
    /**
     * Writes the machine contents to a CompoundNBT tag (used to save contents to disk)
     * @param nbt The NBT tag
     */
    public void deserializeNBT(CompoundNBT nbt) {
        machineComponentContents.deserializeNBT(nbt);
    }

    //  ------------- linking methods  -------------
    //  The following group of methods are used to establish a link between the parent TileEntity and the machine contents,
    //    so that the Container can communicate with the parent TileEntity without having to talk to it directly.
    //  This is important because the link to the TileEntity only exists on the server side. On the client side, the
    //    container gets a dummy link instead - there is no link to the client TileEntity. Linking to the client TileEntity
    //    is prohibited because of synchronisation clashes, i.e. vanilla would attempt to synchronise the TileEntity in two
    //    different ways at the same time: via the tile entity server->client packets and via the container directly poking
    //    around in the inventory contents.
    //  I've used lambdas to make the decoupling more explicit. You could instead:
    //  * provide an Optional TileEntity to the MachineContents constructor (and ignore the markDirty() etc calls), or
    //  * implement IInventory directly in your TileEntity, and construct your client-side container using an Inventory
    //    instead of passing it a TileEntity.  (This is how vanilla does it)
    //    todo: check this^ for a deeper understanding of vanilla code

    /**
     * Sets the function that the Container should call in order to decide if the given player can access it or not.
     * The lambda function is only used on server side.<br>
     * Default is "true".
     * @param canPlayerAccessInventoryLambda
     */
    public void setCanPlayerAcessInventoryLambda(Predicate<PlayerEntity> canPlayerAccessInventoryLambda) {
        this.canPlayerAccessInventoryLambda = canPlayerAccessInventoryLambda;
    }

    /**
     * Sets the function that the Container should call in order to tell the parent TE that the contents of its inventory have changed.<br>
     * Default is "do nothing".
     * @param markDirtyNotificationLambda
     */
    public void setMarkDirtyNotificationLambda(Notify markDirtyNotificationLambda) {
        this.markDirtyNotificationLambda = markDirtyNotificationLambda;
    }

    /**
     * Sets the function that the Container should call in order to tell the parent TE that it has been opened by a player
     * (so that the chest can animate it's lid opening).<br>
     * Default is "do nothing".
     * @param openInventoryNotificationLambda
     */
    public void setOpenInventoryNotificationLambda(Notify openInventoryNotificationLambda) {
        this.openInventoryNotificationLambda = openInventoryNotificationLambda;
    }

    /**
     * Sets the function that the Container should call in order to tell the parent TE that it has been closed by a player.<br>
     * Default is "do nothing".
     * @param closeInventoryNotificationLambda
     */
    public void setCloseInventoryNotificationLambda(Notify closeInventoryNotificationLambda) {
        this.closeInventoryNotificationLambda = closeInventoryNotificationLambda;
    }

    //--------- These methods are used by the container to ask whether certain actions are permitted
    //  If you need special behaviour (eg a chest can only be used by a particular player) then either modify this method
    //  or ask the parent TileEntity.

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        return canPlayerAccessInventoryLambda.test(player);
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return machineComponentContents.isItemValid(index, stack);
    }

    //--------- Methods used to inform the parent tile entity that something has happened to the contents.
    // Note from MBE:
    // You can make direct calls to the parent if you like, I've used lambdas because I think it shows the separation
    // of responsibilities more clearly.

    @Override
    public void markDirty() {
        markDirtyNotificationLambda.invoke();
    }

    @Override
    public void openInventory(PlayerEntity player) {
        openInventoryNotificationLambda.invoke();
    }

    @Override
    public void closeInventory(PlayerEntity player) {
        closeInventoryNotificationLambda.invoke();
    }

    //--------- The following methods are called by Vanilla Container methods to manipulate the inventory contents ---

    @Override
    public int getSizeInventory() {
        return machineComponentContents.getSlots();
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < machineComponentContents.getSlots(); i++) {
            if (!machineComponentContents.getStackInSlot(i).isEmpty()) return false;
        }
        return false;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return machineComponentContents.getStackInSlot(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (count < 0) throw new IllegalArgumentException("Count should be >= 0: " + count);
        return machineComponentContents.extractItem(index, count, false);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        int maxPossibleItemStackSize = machineComponentContents.getSlotLimit(index);
        // extractItem gives you whatever is in the slot up to the param amount.
        // i.e. maxSize = 64, but only 30 there -> you get 30
        // 30 gets passed as amount, and 64 there -> you get 30
        return machineComponentContents.extractItem(index, maxPossibleItemStackSize, false);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        machineComponentContents.setStackInSlot(index, stack);
    }

    @Override
    public void clear() {
        for (int i = 0; i < machineComponentContents.getSlots(); i++) {
            machineComponentContents.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    //--------- useful functions that aren't in IInventory
    /**
     * Tries to insert the given ItemStack into the given slot.
     * @param index The slot to insert into
     * @param itemStackToInsert The itemStack to insert. Is not mutated by the function.
     * @return If successful insertion: ItemStack.EMPTY. Otherwise, the leftover ItemStack
     *         (eg if ItemStack has a size of 23, and only 12 will fit, then ItemStack with a size of 11 is returned)
     */
    public ItemStack increaseStackSize(int index, ItemStack itemStackToInsert) {
        return machineComponentContents.insertItem(index, itemStackToInsert, false);
    }

    /**
     *  Checks if the given slot will accept all of the given itemStack.<br>
     *  Does NOT actually insert ItemStack, for that use {@link MachineContents#increaseStackSize}.
     * @param index The slot to insert into
     * @param itemStackToInsert The itemStack to insert
     * @return If successful insertion: ItemStack.EMPTY.  Otherwise, the leftover ItemStack
     *         (eg if ItemStack has a size of 23, and only 12 will fit, then ItemStack with a size of 11 is returned)
     */
    public boolean doesItemStackFit(int index, ItemStack itemStackToInsert) {
        ItemStack leftoverItemStack = machineComponentContents.insertItem(index, itemStackToInsert, true);
        return leftoverItemStack.isEmpty();
    }
    // ---------
}
