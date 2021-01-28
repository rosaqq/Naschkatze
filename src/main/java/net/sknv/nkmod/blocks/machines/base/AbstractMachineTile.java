package net.sknv.nkmod.blocks.machines.base;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.Optional;

/**
 * Subclass to create a specific TileEntity type for a {@link MachineBlock}, {@code XXTile}.<br>
 * A supplier of this {@code XXTile} must be passed as argument to construct a {@link MachineBlock}.<br>
 * Register with {@code .simpleTileEntity()} on the block registration chain:<br>
 * <pre>
 * MY_MACHINE = REGISTRATE.object("my_machine").block(Material.IRON, p -> new MachineBlock(p, () -> new XXTile(MY_MACHINE.getSibling(ForgeRegistries.TILE_ENTITIES).get())))
 *         .simpleTileEntity(XXTile::new)
 *         .simpleItem().register();
 * </pre>
 * Also, because of how Registrate's registration chains work, {@code XXTile}'s constructor needs to eat a it's own TileEntityType.<br>
 * You can get that with {@code GRINDER.getSibling(ForgeRegistries.TILE_ENTITIES).get()}<br>
 * More details on the constructor javadoc: {@link AbstractMachineTile#AbstractMachineTile}.
 */
public abstract class AbstractMachineTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

    // todo: trying to implement machine functionality, reference code:
    // https://github.com/TheGreyGhost/MinecraftByExample/blob/master/src/main/java/minecraftbyexample/mbe31_inventory_furnace/TileEntityFurnace.java
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(this::createHandler);
    private final IMachineContainerFactory containerFactory;

    public static int OUTPUT_SLOTS_COUNT;
    public static int INPUT_SLOTS_COUNT;
    public static int TOTAL_SLOTS_COUNT;

    private final String INPUT_SLOTS_NBT = "inputSlots";
    private final String OUTPUT_SLOTS_NBT = "outputSlots";

    // stores input
    protected MachineContents inputContents;
    // stores output
    protected MachineContents outputContents;
    // stores state data
    protected final MachineStateData machineStateData = new MachineStateData();

    protected NonNullList<ItemStack> items;

    /**
     * Check class javadoc: {@link AbstractMachineTile}.
     * @param tileEntityTypeIn Subclass constructor param, pass through
     * @param containerFactory A factory of {@code XXTile}'s respective {@code XXContainer} -> {@code XXContainer::new} should suffice.
     */
    public AbstractMachineTile(TileEntityType<?> tileEntityTypeIn, IMachineContainerFactory containerFactory) {
        super(tileEntityTypeIn);
        this.containerFactory = containerFactory;
        inputContents = MachineContents.createForTileEntity(INPUT_SLOTS_COUNT, this::canPlayerAccessInventory, this::markDirty);
        outputContents = MachineContents.createForTileEntity(OUTPUT_SLOTS_COUNT, this::canPlayerAccessInventory, this::markDirty);
    }

    /**
     * Implementation example:
     * <pre>{@code return new ItemStackHandler(1) {
     *     @Override
     *     public boolean isItemValid(int slot, ItemStack stack) {
     *         return stack.getItem() == RegistrateHandler.SOME_ITEM.get().asItem();
     *     }
     *     @Override
     *     public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
     *         if (stack.getItem() != RegistrateHandler.SOME_ITEM.get().asItem()) {
     *             return stack;
     *         }
     *         return super.insertItem(slot, stack, simulate);
     *     }
     * };}</pre>
     * @return An ItemHandler with your machine's specific functionality.
     */
    @Nonnull
    protected abstract IItemHandler createHandler();

    public abstract boolean canPlayerAccessInventory(PlayerEntity player);

    @Nonnull
    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent(Objects.requireNonNull(getType().getRegistryName()).getPath());
    }

    /**
     * The name is misleading; createMenu has nothing to do with creating a Screen, it is used to create the Container on the server only
     * @param windowID
     * @param inv
     * @param player
     * @return
     */
    @Override
    @ParametersAreNonnullByDefault
    public Container createMenu(int windowID, PlayerInventory inv, PlayerEntity player) {
        // return containerFactory.create(windowID, inv, pos);
        return containerFactory.create(windowID, inv, pos);
    }

    // This is where you load the data that you saved in writeToNBT
    @Override
    @ParametersAreNonnullByDefault
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);
        machineStateData.readFromNBT(tag);
        CompoundNBT inventoryNBT = tag.getCompound(INPUT_SLOTS_NBT);
        inputContents.deserializeNBT(inventoryNBT);
        inventoryNBT = tag.getCompound(OUTPUT_SLOTS_NBT);
        outputContents.deserializeNBT(inventoryNBT);

        if (inputContents.getSizeInventory() != INPUT_SLOTS_COUNT || outputContents.getSizeInventory() != OUTPUT_SLOTS_COUNT)
            throw new IllegalArgumentException("Corrupted NBT: Number of inventory slots did not match expected");
    }


    // This is where you save any data that you don't want to lose when the tile entity unloads
    // In this case, it saves the state of the machine (burn time etc) and the ItemStacks stored in the input, and output slots
    @Nonnull
    @Override
    @ParametersAreNonnullByDefault
    public CompoundNBT write(CompoundNBT tag) {

        machineStateData.putIntoNBT(tag);
        tag.put(INPUT_SLOTS_NBT, inputContents.serializeNBT());
        tag.put(OUTPUT_SLOTS_NBT, outputContents.serializeNBT());

        return super.write(tag);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }
        return super.getCapability(cap, side);
    }

    /*
    * Notes on TileEntity and why all the following networking is required:
    * Client and Server refer to the logical client (render, guis, etc) and logical server (game logic) that run for every minecraft world
    *
    * There is no connection between the Client TE and the Client Container
    * The client TE syncs with the Server TE via these networking methods
    * The Server TE talks to the Server Container
    * The Server Container syncs with the Client Container, which talks with the Screen, both existing only when the client opens the GUI (created and destroyed)
    * */

    // When the world loads from disk, the server needs to send the TE info to the client
    // This happens with getUpdatePacket(), getUpdateTag(), onDataPacket() and handleUpdateTag()
    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT updateTag = getUpdateTag();
        final int METADATA = 42; // arbitrary --> so why dafuk??? https://github.com/TheGreyGhost/MinecraftByExample/blob/b8d7d5f304314b3065f5d149ab15f50485c1e1d8/src/main/java/minecraftbyexample/mbe31_inventory_furnace/TileEntityFurnace.java#L376
        return new SUpdateTileEntityPacket(this.pos, METADATA, updateTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT updateTag = pkt.getNbtCompound();
        BlockState blockState = world.getBlockState(pos);
        handleUpdateTag(blockState, updateTag);
    }

    // Creates a tag containing the TE info, used by vanilla to transmit from server to client
    // Warning - although out getUpdatePacket() uses this, vanilla also calls it directly so don't remove it!
    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tag = new CompoundNBT();
        write(tag);
        return tag;
    }

    // Populates this TE with info from the tag, used by vanilla to transmit from server to client
    // This is exactly equal to the method it overrides (vanilla default). Here for clarity
    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        read(state, tag);
    }

    /**
     * Drop all TE contents when destroyed
     * @param world
     * @param pos
     */
    public void dropAllContents(World world, BlockPos pos) {
        InventoryHelper.dropInventoryItems(world, pos, inputContents);
        InventoryHelper.dropInventoryItems(world, pos, outputContents);
    }

    public boolean isBurning() {
        return machineStateData.cookTimeElapsed < machineStateData.cookTimeForCompletion;
    }

    /**
     *
     * @param world
     * @param itemStack
     * @return result of smelt for itemStack or ItemStack.EMPTY if can't be smelted
     */
    public ItemStack getSmeltingResultForItem(World world, ItemStack itemStack) {
        Optional<FurnaceRecipe> matchingRecipe = getMatchingRecipeForInput(world, itemStack);
        return matchingRecipe.map(furnaceRecipe -> furnaceRecipe.getRecipeOutput().copy()).orElse(ItemStack.EMPTY);
    }

    // todo: making this not static to allow overrides started a chain of making methods not static that should be thought over
    protected abstract Optional<FurnaceRecipe> getMatchingRecipeForInput(World world, ItemStack itemStack);
}
