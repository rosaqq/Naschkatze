package net.secknv.nkmod.recipes;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.secknv.nkmod.block.NkBlocks;
import net.secknv.nkmod.item.NkItems;

public class RecipesCrafting {
	
	public static void registerRecipes() {

		//shaped recipes
		GameRegistry.addRecipe(new ItemStack(NkBlocks.TEST_BLOCK), "ACA", "DBD", "ACA", 'A', Blocks.OBSIDIAN, 'B', Blocks.DIAMOND_BLOCK, 'C', Blocks.GLASS_PANE, 'D', Blocks.COAL_BLOCK);

        //shapeless recipes
	}
}
