package secknv.minecraft.nkmod.recipes;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import secknv.minecraft.nkmod.blocks.NkBlocks;

public class RecipesCrafting
{
	
	public static void registerRecipes()
	{
		GameRegistry.addRecipe(new ItemStack(NkBlocks.test_block), "ACA", "DBD", "ACA", 'A', Blocks.obsidian, 'B', Blocks.diamond_block, 'C', Blocks.glass_pane, 'D', Blocks.coal_block);
	}
}
