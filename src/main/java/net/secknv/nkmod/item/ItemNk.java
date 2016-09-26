package net.secknv.nkmod.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.secknv.nkmod.Naschkatze;
import net.secknv.nkmod.reference.Reference;

public class ItemNk extends Item {
	
	public ItemNk(String name) {


		super();
		setRegistryName(name);
		setUnlocalizedName(Reference.MOD_ID + "." + name);
		setCreativeTab(Naschkatze.tabNk);
	}

}
