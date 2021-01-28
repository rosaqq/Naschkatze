package net.sknv.nkmod.blocks.machines.base;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IIntArray;

import java.util.Arrays;

/**
 * Based on <a href="https://github.com/TheGreyGhost/MinecraftByExample/blob/master/src/main/java/minecraftbyexample/mbe31_inventory_furnace/FurnaceStateData.java">MBE31-FurnaceStateData.java</a>,
 * created by TGG on 4/04/2020.<br>
 * This class is used to store some state data for the MachineBlocks (smelting time, smelting time elapsed, etc):
 * <ol>
 *   <li>The Server TileEntity uses it to store the data permanently, including NBT serialisation and deserialization</li>
 *   <li>
 *     The server container uses it to
 *       <ol>
 *         <li>read/write permanent data back into the TileEntity</li>
 *         <li>synchronise the server container data to the client container using the IIntArray interface (via Container::trackIntArray)</li>
 *      </ol>
 *   <li>The client container uses it to store a temporary copy of the data, for rendering / GUI purposes</li>
 * </ol>
 * The TileEntity and the client container both use it by poking directly into its member variables. That's not good
 * practice but it's easier to understand than the vanilla method which uses an anonymous class/lambda functions.<br>
 * // todo: check the vanilla anonymous class/lambda method mentioned above<br>
 * The IIntArray interface collates all the separate member variables into a single array for the purposes of transmitting
 * from server to client (handled by Vanilla).<br>
 */
public class MachineStateData implements IIntArray {

    /** The number of ticks the current item has been cooking. */
    public int cookTimeElapsed;

    /** The number of ticks required to cook the current item */
    public int cookTimeForCompletion;

    // --------- read/write to NBT for permanent storage (on disk, or packet transmission) - used by the TileEntity only
    public void putIntoNBT(CompoundNBT tag) {
        tag.putInt("CookTimeElapsed", cookTimeElapsed);
        tag.putInt("CookTimeForCompletion", cookTimeForCompletion);
    }

    public void readFromNBT(CompoundNBT tag) {
        cookTimeElapsed = tag.getInt("CookTimeElapsed");
        cookTimeForCompletion = tag.getInt("CookTimeForCompletion");

        // No plan to impl this, left as an example
        // Getting Arrays from NBT and creating a copy for the variables
        // burnTimesRemaining = Arrays.copyOf(nbtTagCompound.getIntArray("burnTimesRemaining"), FUEL_SLOTS_COUNT);
        // burnTimeInitialValues = Arrays.copyOf(nbtTagCompound.getIntArray("burnTimeInitialValues"), FUEL_SLOTS_COUNT);
    }

    // -------- next section used by vanilla, not intended for mod code
    //  * The ints are mapped (internally) as:
    //  * 0 = cookTimeElapsed
    //  * 1 = cookTimeForCompletion
    //  * 2 .. FUEL_SLOTS_COUNT+1 = burnTimeInitialValues[]
    //  (Third value so starts on 2. Add fuel slots count and for 3 slots you get position 4 (FUEL_SLOTS_COUNT+1)
    //   because one slot goes on #2, another on #3 and last on #4)
    //  * FUEL_SLOTS_COUNT + 2 .. 2*FUEL_SLOTS_COUNT +1 = burnTimeRemainings[]
    //  (Starts on the next value from prev, so FUEL_SLOTS_COUNT+2.
    //   Ends on start_value + 2*fuel_slots_count - 1 (because one of the fuel slots goes int the start value)
    //   which in this case means 2+...-1 <=> 2*FUEL_SLOTS_COUNT + 1
    //           ┌ fuel_slots_count    ┐ ┌ fuel_slots_count    ┐
    //   [0] [1] [2] [3] [4] [5] [6] [7] [8] [9] [ ] [ ] [ ] [ ]

    private final int COOKTIME_INDEX = 0;
    private final int COOKTIME_FOR_COMPLETION_INDEX = 1;
    private final int END_OF_DATA_INDEX_PLUS_ONE = COOKTIME_FOR_COMPLETION_INDEX + 1;

    @Override
    public int get(int index) {
        validateIndex(index);
        // Only two values so if not one, must be the other
        // I can do this because index has already been validated to match a value.
        if (index == COOKTIME_INDEX) return cookTimeElapsed;
        return cookTimeForCompletion;
    }

    @Override
    public void set(int index, int value) {
        validateIndex(index);
        if (index == COOKTIME_INDEX) cookTimeElapsed = value;
        cookTimeForCompletion = value;
    }

    @Override
    public int size() {
        return END_OF_DATA_INDEX_PLUS_ONE;
    }

    /**
     * Ensures index matches an actual value.
     * @param index
     */
    private void validateIndex(int index) {
        if (index < 0 || index >= size()) throw new IndexOutOfBoundsException("Index out of bounds: " + index);
    }
}
