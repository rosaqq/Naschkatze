package secknv.minecraft.nkmod.items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import secknv.minecraft.nkmod.Reference;

public class NkItems
{
	
	public static Item redstone_ingot;
	
	public static void init()
	{
		redstone_ingot = new ItemRedstoneIngot();		
	}
	
	public static void registerRenders()
	{
		registerRender(redstone_ingot);
	}
	
	public static void registerRender(Item item)
	{
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		renderItem.getItemModelMesher().register(item, 0,
				new ModelResourceLocation(Reference.MODID + ":" + 
						(item.getUnlocalizedName().substring(5)), "inventory"));
	}
}
