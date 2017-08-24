package net.secknv.nkmod.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.secknv.nkmod.reference.Reference;

@GameRegistry.ObjectHolder(Reference.MOD_ID)
public class NkItems {

	public static Item REDSTONE_INGOT = new ItemNk("redstone_ingot");
	public static Item COPPER_INGOT = new ItemNk("copper_ingot");
	public static Item COPPER_WIRE = new ItemNk("copper_wire");
	public static Item TEST_ITEM = new ItemTest();


	public static void register() {

		registerItem(REDSTONE_INGOT);
		registerItem(COPPER_INGOT);
		registerItem(COPPER_WIRE);
		registerItem(TEST_ITEM);
	}
	
	public static void registerItem(Item item) {

		GameRegistry.register(item);
	}
	
	public static void registerRenders() {

		registerRender(REDSTONE_INGOT);
		registerRender(COPPER_INGOT);
		registerRender(COPPER_WIRE);
		registerRender(TEST_ITEM);
	}
	
	public static void registerRender(Item item) {

		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0,
				new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
}
