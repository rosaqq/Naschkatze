package net.secknv.nkmod.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.secknv.nkmod.reference.Reference;

@GameRegistry.ObjectHolder(Reference.MOD_ID)
public class NkBlocks {
	
	public static Block URANIUM_ORE = new BlockTestBlock();
	public static Block TEST_BLOCK = new BlockNkOre("copper_ore", 1);
	public static Block COPPER_ORE = new BlockNkOre("uranium_ore", 2);
	public static Block COPPER_COIL = new BlockCoil();
	public static Block COPPER_BLOCK = new BlockNk("copper_block", Material.IRON, SoundType.METAL, 5.0F, 10.0F);
	
	public static void register() {

		registerBlock(URANIUM_ORE);
		registerBlock(TEST_BLOCK);
		registerBlock(COPPER_ORE);
		registerBlock(COPPER_COIL);
		registerBlock(COPPER_BLOCK);
	}
	
	public static void registerBlock(Block block) {

		GameRegistry.register(block);
		ItemBlock item = new ItemBlock(block);
		item.setRegistryName(block.getRegistryName());
		GameRegistry.register(item);
	}
	
	public static void registerRenders() {

		registerRender(URANIUM_ORE);
		registerRender(TEST_BLOCK);
		registerRender(COPPER_ORE);
		registerRender(COPPER_COIL);
		registerRender(COPPER_BLOCK);
	}

	public static void registerRender(Block block) {

		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), 0,
				new ModelResourceLocation(block.getRegistryName(), "inventory"));
	}
}
