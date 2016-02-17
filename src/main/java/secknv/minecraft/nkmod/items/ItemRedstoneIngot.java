package secknv.minecraft.nkmod.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import secknv.minecraft.nkmod.Reference;

public class ItemRedstoneIngot extends Item{
	
	private final String name = "redstone_ingot";
	
	public ItemRedstoneIngot(){
		GameRegistry.registerItem(this, name);
		setUnlocalizedName(name);
		setCreativeTab(CreativeTabs.tabMaterials);
	}
	
}
