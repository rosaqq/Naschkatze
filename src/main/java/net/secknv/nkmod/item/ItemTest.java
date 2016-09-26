package net.secknv.nkmod.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Created by secknv on 06/09/2016.
 */

public class ItemTest extends ItemNk{


    public ItemTest() {
        super("test_item");
        this.maxStackSize = 1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        if (!(stack.getTagCompound() == null)) {
            if(stack.getTagCompound().hasKey("Description")) {
                tooltip.add(stack.getTagCompound().getCompoundTag("display").getString("Description"));
            }
        }
    }


    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        String blockName = worldIn.getBlockState(pos).getBlock().getLocalizedName();
        if (stack.getTagCompound() == null)
        {
            stack.setTagCompound(new NBTTagCompound());
        }

        stack.getTagCompound().setString("Description", blockName);


        if (!worldIn.isRemote) {
            playerIn.addChatMessage(new TextComponentString("Block set to " + blockName));
        }
        return EnumActionResult.SUCCESS;
    }
}
