package net.secknv.nkmod;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.secknv.nkmod.item.NkItems;

public class NkTab extends CreativeTabs
{

	public NkTab(String label)
	{
		super(label);
		this.setBackgroundImageName("items.png");
	}

	@Override
	public Item getTabIconItem()
	{
		return NkItems.REDSTONE_INGOT;
	}

}
