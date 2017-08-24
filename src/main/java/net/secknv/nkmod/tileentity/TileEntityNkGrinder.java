package net.secknv.nkmod.tileentity;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityNkGrinder extends TileEntityLockable implements ITickable, ISidedInventory{
	
	/**
	 * 
	 * code is copied from minecraft furnace for testing purposes
	 * works as furnace, already started trying to morph it into something more grinder-like
	 * 
	 * start by having it consume coal like a furnace but only turn uranium ore into uranium ore dust
	 * 
	 * --secknv
	 *
	 */
	
    private static final int[] SLOTS_TOP = new int[] {0};
    private static final int[] SLOTS_BOTTOM = new int[] {2, 1};
    private static final int[] SLOTS_SIDES = new int[] {1};
    /** The ItemStacks that hold the items currently being used in the furnace */
    private ItemStack[] grinderItemStacks = new ItemStack[3];
    /** The number of ticks that the furnace will keep burning */
    private int grinderBurnTime;
    /** The number of ticks that a fresh copy of the currently-burning item would keep the furnace burning for */
    private int currentItemBurnTime;
    private int cookTime;
    private int totalCookTime;
    private String grinderCustomName;

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory() {
        return this.grinderItemStacks.length;
    }

    /**
     * Returns the stack in the given slot.
     */
    @Nullable
    public ItemStack getStackInSlot(int index)
    {
        return this.grinderItemStacks[index];
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    @Nullable
    public ItemStack decrStackSize(int index, int count)
    {
        return ItemStackHelper.getAndSplit(this.grinderItemStacks, index, count);
    }

    /**
     * Removes a stack from the given slot and returns it.
     */
    @Nullable
    public ItemStack removeStackFromSlot(int index)
    {
        return ItemStackHelper.getAndRemove(this.grinderItemStacks, index);
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int index, @Nullable ItemStack stack)
    {
        boolean flag = stack != null && stack.isItemEqual(this.grinderItemStacks[index]) && ItemStack.areItemStackTagsEqual(stack, this.grinderItemStacks[index]);
        this.grinderItemStacks[index] = stack;

        if (stack != null && stack.stackSize > this.getInventoryStackLimit())
        {
            stack.stackSize = this.getInventoryStackLimit();
        }

        if (index == 0 && !flag)
        {
            this.totalCookTime = this.getCookTime(stack);
            this.cookTime = 0;
            this.markDirty();
        }
    }

    /**
     * Get the name of this object. For players this returns their username
     */
    public String getName()
    {
        return this.hasCustomName() ? this.grinderCustomName : "container.grinder";
    }

    /**
     * Returns true if this thing is named
     */
    public boolean hasCustomName()
    {
        return this.grinderCustomName != null && !this.grinderCustomName.isEmpty();
    }

    public void setCustomInventoryName(String p_145951_1_)
    {
        this.grinderCustomName = p_145951_1_;
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        NBTTagList nbttaglist = compound.getTagList("Items", 10);
        this.grinderItemStacks = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound.getByte("Slot");

            if (j >= 0 && j < this.grinderItemStacks.length)
            {
                this.grinderItemStacks[j] = ItemStack.loadItemStackFromNBT(nbttagcompound);
            }
        }

        this.grinderBurnTime = compound.getInteger("BurnTime");
        this.cookTime = compound.getInteger("CookTime");
        this.totalCookTime = compound.getInteger("CookTimeTotal");
        this.currentItemBurnTime = getItemBurnTime(this.grinderItemStacks[1]);

        if (compound.hasKey("CustomName", 8))
        {
            this.grinderCustomName = compound.getString("CustomName");
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setInteger("BurnTime", this.grinderBurnTime);
        compound.setInteger("CookTime", this.cookTime);
        compound.setInteger("CookTimeTotal", this.totalCookTime);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.grinderItemStacks.length; ++i)
        {
            if (this.grinderItemStacks[i] != null)
            {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte)i);
                this.grinderItemStacks[i].writeToNBT(nbttagcompound);
                nbttaglist.appendTag(nbttagcompound);
            }
        }

        compound.setTag("Items", nbttaglist);

        if (this.hasCustomName())
        {
            compound.setString("CustomName", this.grinderCustomName);
        }

        return compound;
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended.
     */
    public int getInventoryStackLimit()
    {
        return 64;
    }

    /**
     * Furnace isBurning
     */
    public boolean isBurning()
    {
        return this.grinderBurnTime > 0;
    }

    @SideOnly(Side.CLIENT)
    public static boolean isBurning(IInventory inventory)
    {
        return inventory.getField(0) > 0;
    }

    /**
     * Like the old updateEntity(), except more generic.
     */
    public void update()
    {
        boolean flag = this.isBurning();
        boolean flag1 = false;

        if (this.isBurning()) {
            --this.grinderBurnTime;
        }

        if (!this.worldObj.isRemote) {
            if (this.isBurning() || this.grinderItemStacks[1] != null && this.grinderItemStacks[0] != null) {
            	
                if (!this.isBurning() && this.canSmelt()) {
                    this.grinderBurnTime = getItemBurnTime(this.grinderItemStacks[1]);
                    this.currentItemBurnTime = this.grinderBurnTime;

                    if (this.isBurning())
                    {
                        flag1 = true;

                        if (this.grinderItemStacks[1] != null)
                        {
                            --this.grinderItemStacks[1].stackSize;

                            if (this.grinderItemStacks[1].stackSize == 0)
                            {
                                this.grinderItemStacks[1] = grinderItemStacks[1].getItem().getContainerItem(grinderItemStacks[1]);
                            }
                        }
                    }
                }

                if (this.isBurning() && this.canSmelt())
                {
                    ++this.cookTime;

                    if (this.cookTime == this.totalCookTime)
                    {
                        this.cookTime = 0;
                        this.totalCookTime = this.getCookTime(this.grinderItemStacks[0]);
                        this.smeltItem();
                        flag1 = true;
                    }
                }
                else
                {
                    this.cookTime = 0;
                }
            }
            else if (!this.isBurning() && this.cookTime > 0)
            {
                this.cookTime = MathHelper.clamp_int(this.cookTime - 2, 0, this.totalCookTime);
            }

            if (flag != this.isBurning())
            {
                flag1 = true;
            }
        }

        if (flag1)
        {
            this.markDirty();
        }
    }

    public int getCookTime(@Nullable ItemStack stack)
    {
        return 200;
    }

    /**
     * Returns true if the furnace can smelt an item, i.e. has a source item, destination stack isn't full, etc.
     */
    private boolean canSmelt()
    {
        if (this.grinderItemStacks[0] == null)
        {
            return false;
        }
        else
        {
            ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(this.grinderItemStacks[0]);
            if (itemstack == null) return false;
            if (this.grinderItemStacks[2] == null) return true;
            if (!this.grinderItemStacks[2].isItemEqual(itemstack)) return false;
            int result = grinderItemStacks[2].stackSize + itemstack.stackSize;
            return result <= getInventoryStackLimit() && result <= this.grinderItemStacks[2].getMaxStackSize(); //Forge BugFix: Make it respect stack sizes properly.
        }
    }

    /**
     * Turn one item from the furnace source stack into the appropriate smelted item in the furnace result stack
     */
    public void smeltItem()
    {
        if (this.canSmelt())
        {
            ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(this.grinderItemStacks[0]);

            if (this.grinderItemStacks[2] == null)
            {
                this.grinderItemStacks[2] = itemstack.copy();
            }
            else if (this.grinderItemStacks[2].getItem() == itemstack.getItem())
            {
                this.grinderItemStacks[2].stackSize += itemstack.stackSize; // Forge BugFix: Results may have multiple items
            }

            if (this.grinderItemStacks[0].getItem() == Item.getItemFromBlock(Blocks.SPONGE) && this.grinderItemStacks[0].getMetadata() == 1 && this.grinderItemStacks[1] != null && this.grinderItemStacks[1].getItem() == Items.BUCKET)
            {
                this.grinderItemStacks[1] = new ItemStack(Items.WATER_BUCKET);
            }

            --this.grinderItemStacks[0].stackSize;

            if (this.grinderItemStacks[0].stackSize <= 0)
            {
                this.grinderItemStacks[0] = null;
            }
        }
    }

    /**
     * Returns the number of ticks that the supplied fuel item will keep the furnace burning, or 0 if the item isn't
     * fuel
     * 
     * this makes grinder run on coal like furnace --secknv
     */
    public static int getItemBurnTime(ItemStack stack) {
        if (stack == null) {
            return 0;
        }
        else {
            Item item = stack.getItem();

            if (item instanceof ItemBlock && Block.getBlockFromItem(item) != Blocks.AIR) {
                Block block = Block.getBlockFromItem(item);

                if (block == Blocks.WOODEN_SLAB) {
                    return 150;
                }

                if (block.getDefaultState().getMaterial() == Material.WOOD) {
                    return 300;
                }

                if (block == Blocks.COAL_BLOCK) {
                    return 16000;
                }
            }

            if (item instanceof ItemTool && "WOOD".equals(((ItemTool)item).getToolMaterialName())) return 200;
            if (item instanceof ItemSword && "WOOD".equals(((ItemSword)item).getToolMaterialName())) return 200;
            if (item instanceof ItemHoe && "WOOD".equals(((ItemHoe)item).getMaterialName())) return 200;
            if (item == Items.STICK) return 100;
            if (item == Items.COAL) return 1600;
            if (item == Items.LAVA_BUCKET) return 20000;
            if (item == Item.getItemFromBlock(Blocks.SAPLING)) return 100;
            if (item == Items.BLAZE_ROD) return 2400;
            return net.minecraftforge.fml.common.registry.GameRegistry.getFuelValue(stack);
        }
    }

    public static boolean isItemFuel(ItemStack stack)
    {
        return getItemBurnTime(stack) > 0;
    }

    /**
     * Don't rename this method to canInteractWith due to conflicts with Container
     */
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return this.worldObj.getTileEntity(this.pos) != this ? false : player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
    }

    public void openInventory(EntityPlayer player)
    {
    }

    public void closeInventory(EntityPlayer player)
    {
    }

    public String getGuiID()
    {
        return "minecraft:furnace";
    }

    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
        return new ContainerFurnace(playerInventory, this);
    }

    public int getField(int id)
    {
        switch (id)
        {
            case 0:
                return this.grinderBurnTime;
            case 1:
                return this.currentItemBurnTime;
            case 2:
                return this.cookTime;
            case 3:
                return this.totalCookTime;
            default:
                return 0;
        }
    }

    public void setField(int id, int value)
    {
        switch (id)
        {
            case 0:
                this.grinderBurnTime = value;
                break;
            case 1:
                this.currentItemBurnTime = value;
                break;
            case 2:
                this.cookTime = value;
                break;
            case 3:
                this.totalCookTime = value;
        }
    }

    public int getFieldCount()
    {
        return 4;
    }

    public void clear()
    {
        for (int i = 0; i < this.grinderItemStacks.length; ++i)
        {
            this.grinderItemStacks[i] = null;
        }
    }

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		// TODO Auto-generated method stub
		//for automation with pipes; leave like this for disabled --secknv.
		return false;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		// TODO Auto-generated method stub
		//for automation with pipes; leave like this for disabled --secknv.
		return null;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		// TODO Auto-generated method stub
		//for automation with pipes; leave like this for disabled --secknv.
		return false;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		// TODO Auto-generated method stub
		//for automation with pipes; leave like this for disabled --secknv.
		return false;
	}
}