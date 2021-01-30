package net.sknv.nkmod.blocks.machines;

import com.google.common.collect.Lists;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.*;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.datafix.fixes.WolfCollarColor;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.sknv.nkmod.RegistrateHandler;
import net.sknv.nkmod.blocks.machines.base.AbstractMachineTile;
import net.sknv.nkmod.blocks.machines.base.MachineBlock;
import net.sknv.nkmod.blocks.machines.base.MachineContents;
import net.sknv.nkmod.util.SetBlockStateFlag;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nonnull;
import java.util.Optional;


public class GrinderTile extends AbstractMachineTile {

    //protected final IRecipeType<? extends AbstractCookingRecipe> recipeType;

    private ItemStack currentlySmeltingItemLastTick = ItemStack.EMPTY;

    public GrinderTile(TileEntityType<?> type) {
        super(type, GrinderContainer::new);
        this.items = NonNullList.withSize(3, ItemStack.EMPTY);
        //this.recipeType = recipeType

        INPUT_SLOTS_COUNT = 1;
        OUTPUT_SLOTS_COUNT = 1;
        TOTAL_SLOTS_COUNT = INPUT_SLOTS_COUNT + OUTPUT_SLOTS_COUNT;
    }

    // Return true if the given player is able to use this block. In this case it checks that
    // 1) the world tile entity hasn't been replaced in the meantime, and
    // 2) the player isn't too far away from the centre of the block
    public boolean canPlayerAccessInventory(PlayerEntity player) {
        if (this.world.getTileEntity(this.pos) != this) return false;
        final double X_CENTRE_OFFSET = 0.5;
        final double Y_CENTRE_OFFSET = 0.5;
        final double Z_CENTRE_OFFSET = 0.5;
        final double MAXIMUM_DISTANCE_SQ = 8.0 * 8.0;
        return player.getDistanceSq(pos.getX() + X_CENTRE_OFFSET, pos.getY() + Y_CENTRE_OFFSET, pos.getZ() + Z_CENTRE_OFFSET) < MAXIMUM_DISTANCE_SQ;
    }

    @Override
    public void tick() {
        if (world.isRemote) return; // do nothing on client.
        ItemStack currentlySmeltingItem = getCurrentlySmeltingInputItem();
        if (!ItemStack.areItemsEqual(currentlySmeltingItem, currentlySmeltingItemLastTick)) {  // == and != don't work!
            machineStateData.cookTimeElapsed = 0;
        }
        currentlySmeltingItemLastTick = currentlySmeltingItem.copy();
        if (!currentlySmeltingItem.isEmpty()) {
            machineStateData.cookTimeElapsed++;

            int cookTimeForCurrentItem = getCookTime(this.world, currentlySmeltingItem);
            machineStateData.cookTimeForCompletion = cookTimeForCurrentItem;

            // if cook time has reached required level, smelt and reset
            if (machineStateData.cookTimeElapsed >= cookTimeForCurrentItem) {
                smelt();
                machineStateData.cookTimeElapsed = 0;
            }
        }
        else {
            machineStateData.cookTimeElapsed = 0;
        }

        // force block re-render
        BlockState currentBlockState = world.getBlockState(this.pos);
        BlockState newBlockState = currentBlockState.with(MachineBlock.WORKING, isBurning());
        if (!newBlockState.equals(currentBlockState)) {
            final int FLAGS = SetBlockStateFlag.get(SetBlockStateFlag.BLOCK_UPDATE, SetBlockStateFlag.SEND_TO_CLIENTS);
            world.setBlockState(this.pos, newBlockState, FLAGS);
            markDirty();
        }
    }

    /**
     * Check if any of the input items are smeltable and there is sufficient space in the output slots
     * @return the ItemStack of the first input item that can be smelted; ItemStack.EMPTY if none
     */
    private ItemStack getCurrentlySmeltingInputItem() {
        return smeltFirstSuitableItem(false);
    }

    /**
     * Smelt an input item into an output slot, if possible
     */
    private void smeltFirstSuitableItem() {
        smeltFirstSuitableItem(true);
    }

    /**
     * checks that there is an item to be smelted in one of the input slots and that there is room for the result in the output slots
     * If desired, performs the smelt
     * @param performSmelt if true, perform the smelt.  if false, check whether smelting is possible, but don't change the inventory
     * @return a copy of the ItemStack of the input item smelted or to-be-smelted
     */
    private ItemStack smeltFirstSuitableItem(boolean performSmelt) {
        Integer firstSuitableInputSlot = null;
        Integer firstSuitableOutputSlot = null;
        ItemStack result = ItemStack.EMPTY;

        // finds the first input slot which is smeltable and whose result fits into an output slot (stacking if possible)
        // Grinder case is 1 input slot, so iterates once
        for (int inputIndex = 0; inputIndex < INPUT_SLOTS_COUNT; inputIndex++) {
            ItemStack itemStackToSmelt = inputContents.getStackInSlot(inputIndex);
            if (!itemStackToSmelt.isEmpty()) {
                result = getSmeltingResultForItem(this.world, itemStackToSmelt);
                if (!result.isEmpty()) {
                    // find the first suitable output slot - either empty, or with stackable item
                    for (int outputIndex = 0; outputIndex < OUTPUT_SLOTS_COUNT; outputIndex++) {
                        if (willItemStackFit(outputContents, outputIndex, result)) {
                            firstSuitableInputSlot = inputIndex;
                            firstSuitableOutputSlot = outputIndex;
                            break;
                        }
                    }
                    // as soon as it finds one smeltable input slot, it breaks out
                    // this will only happen if it also found a suitable output slot (broken out above)
                    if (firstSuitableInputSlot != null) break;
                }
            }
        }

        if (firstSuitableInputSlot == null) return ItemStack.EMPTY;
        ItemStack returnvalue = inputContents.getStackInSlot(firstSuitableInputSlot).copy();
        if (!performSmelt) return returnvalue;
        // if we want to smelt
        inputContents.decrStackSize(firstSuitableInputSlot, 1);
        outputContents.increaseStackSize(firstSuitableOutputSlot, result);
        markDirty();
        return returnvalue;
    }

    /**
     * Will the given ItemStack fully fit into the target slot?
     * @param machineContents
     * @param slotIndex
     * @param itemStackOrigin the item we are trying to add to the output
     * @return true if the given ItemStack will fit completely; false otherwise
     */
    public boolean willItemStackFit(MachineContents machineContents, int slotIndex, ItemStack itemStackOrigin) {
        ItemStack itemStackDestination = machineContents.getStackInSlot(slotIndex);
        // will fit if output is empty or input is empty (because empty input smelts to nothing)
        if (itemStackDestination.isEmpty() || itemStackOrigin.isEmpty()) return true;
        // won't fit if different items
        if (!itemStackOrigin.isItemEqual(itemStackDestination)) return false;

        // size of stack after adding recipe result?
        int sizeAfterMerge = itemStackDestination.getCount() + itemStackOrigin.getCount();

        // will fit if resulting size <= machine inv slot max size and <= output stack max size, else nope
        return sizeAfterMerge <= machineContents.getInventoryStackLimit() && sizeAfterMerge <= itemStackDestination.getMaxStackSize();
    }

    @Override
    public Optional<FurnaceRecipe> getMatchingRecipeForInput(World world, ItemStack itemStack) {
        RecipeManager recipeManager = world.getRecipeManager();
        Inventory singleItemInventory = new Inventory(itemStack);
        return recipeManager.getRecipe(IRecipeType.SMELTING, singleItemInventory, world);
    }

    /**
     * Gets the cooking time for this recipe input
     * @param world
     * @param itemStack the input item to be smelted
     * @return cooking time (ticks) or 0 if no matching recipe
     */
    public int getCookTime(World world, ItemStack itemStack) {
        Optional<FurnaceRecipe> matchingRecipe = getMatchingRecipeForInput(world, itemStack);
        return matchingRecipe.map(AbstractCookingRecipe::getCookTime).orElse(0);
    }

    // Return true if the given stack is allowed to be inserted in the given slot
    // Unlike the vanilla furnace, we allow anything to be placed in the input slots
    static public boolean isItemValidForInputSlot(ItemStack itemStack)
    {
        return true;
    }

    // Return true if the given stack is allowed to be inserted in the given slot
    static public boolean isItemValidForOutputSlot(ItemStack itemStack)
    {
        return false;
    }



    @Nonnull
    @Override
    protected IItemHandler createHandler() {
        return new ItemStackHandler(3);
    }

    private void smelt() {
        //todo: not detecting which item is in slot, this prints air
        LogManager.getLogger().error("tryna smelt itemstack: " + this.items.get(0));
        //ItemStack itemstack = this.items.get(0);
        //ItemStack itemstack1 = recipe.getRecipeOutput();
        //ItemStack itemstack2 = this.items.get(2);

        //LogManager.getLogger().error("itemstack: " + itemstack.getItem());
        //LogManager.getLogger().error("itemstack1: " + itemstack1.getItem());
        //LogManager.getLogger().error("itemstack2: " + itemstack2.getItem());
    }
}
