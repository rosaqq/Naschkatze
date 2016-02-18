package secknv.minecraft.nkmod.blocks;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import secknv.minecraft.nkmod.Reference;

public class NkBlocks{
	
	public static Block uranium_ore;
	
	public static void init(){
		uranium_ore = new BlockUraniumOre();
			
	}
	
	public static void registerRenders(){
		registerRender(uranium_ore);
	}
	
	public static void registerRender(Block block){
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		renderItem.getItemModelMesher().register(Item.getItemFromBlock(block), 0,
				new ModelResourceLocation(Reference.MODID + ":" + 
						block.getUnlocalizedName().substring(5), "inventory"));
	}
	
}
