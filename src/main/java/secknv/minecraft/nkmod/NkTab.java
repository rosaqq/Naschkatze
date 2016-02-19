package secknv.minecraft.nkmod;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import secknv.minecraft.nkmod.items.NkItems;

public class NkTab extends CreativeTabs{

	public NkTab(String label){
		super(label);
		this.setBackgroundImageName("items.png");
	}

	@Override
	public Item getTabIconItem(){
		return NkItems.redstone_ingot;
	}

}
