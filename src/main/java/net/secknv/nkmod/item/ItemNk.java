package net.secknv.nkmod.item;

import net.minecraft.item.Item;
import net.secknv.nkmod.Naschkatze;
import net.secknv.nkmod.reference.Reference;

public class ItemNk extends Item{
	
	public ItemNk(String name){
		
		setRegistryName(name);
		setUnlocalizedName(Reference.MODID + "." + name);
		setCreativeTab(Naschkatze.tabNk);
	}
}
