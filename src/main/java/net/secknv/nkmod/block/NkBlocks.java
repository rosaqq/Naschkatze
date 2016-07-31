package net.secknv.nkmod.block;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class NkBlocks
{
	
	public static Block URANIUM_ORE;
	public static Block TEST_BLOCK;
	public static Block COPPER_ORE;
	
	public static void init()
	{
		TEST_BLOCK = new BlockTestBlock();
		COPPER_ORE = new BlockNkOre(1).setRegistryName("copper_ore").setUnlocalizedName("copper_ore");
		URANIUM_ORE = new BlockNkOre(2).setRegistryName("uranium_ore").setUnlocalizedName("uranium_ore");
	}
	
	public static void register()
	{
		registerBlock(URANIUM_ORE);
		registerBlock(TEST_BLOCK);
		registerBlock(COPPER_ORE);
	}
	
	public static void registerBlock(Block block)
	{
		GameRegistry.register(block);
		ItemBlock item = new ItemBlock(block);
		item.setRegistryName(block.getRegistryName());
		GameRegistry.register(item);
	}
	
	public static void registerRenders()
	{
		registerRender(URANIUM_ORE);
		registerRender(TEST_BLOCK);
		registerRender(COPPER_ORE);
	}

	public static void registerRender(Block block)
	{
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), 0,
				new ModelResourceLocation(block.getRegistryName(), "inventory"));
	}
}
