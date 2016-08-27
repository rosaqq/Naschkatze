package net.secknv.nkmod.item;

import net.minecraft.item.Item;
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
