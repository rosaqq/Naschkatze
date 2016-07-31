package net.secknv.nkmod.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.secknv.nkmod.Naschkatze;

public class NkItems
{
	
	public static Item REDSTONE_INGOT;
	public static Item COPPER_INGOT;
	
	public static void init()
	{
		REDSTONE_INGOT = new ItemNk().setRegistryName("redstone_ingot").setUnlocalizedName("redstone_ingot");
		COPPER_INGOT = new ItemNk().setRegistryName("copper_ingot").setUnlocalizedName("copper_ingot");
	}
	
	
	public static void register()
	{
		registerItem(REDSTONE_INGOT);
		registerItem(COPPER_INGOT);
	}
	
	public static void registerItem(Item item)
	{
		GameRegistry.register(item);
	}
	
	public static void registerRenders()
	{
		registerRender(REDSTONE_INGOT);
		registerRender(COPPER_INGOT);
	}
	
	public static void registerRender(Item item)
	{
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0,
				new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
}
