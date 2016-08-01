package net.secknv.nkmod.recipes;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.secknv.nkmod.block.NkBlocks;
import net.secknv.nkmod.item.NkItems;

public class RecipesCrafting
{
	
	public static void registerRecipes()
	{
		GameRegistry.addRecipe(new ItemStack(NkBlocks.TEST_BLOCK), "ACA", "DBD", "ACA", 'A', Blocks.OBSIDIAN, 'B', Blocks.DIAMOND_BLOCK, 'C', Blocks.GLASS_PANE, 'D', Blocks.COAL_BLOCK);
		GameRegistry.addRecipe(new ItemStack(NkItems.COPPER_WIRE), "AAA", 'A', NkItems.COPPER_INGOT);
		GameRegistry.addRecipe(new ItemStack(NkBlocks.COPPER_BLOCK), "AAA", "AAA", "AAA", 'A', NkItems.COPPER_INGOT);
		GameRegistry.addRecipe(new ItemStack(NkBlocks.COPPER_COIL), "AAA", "ABA", "AAA", 'A', NkItems.COPPER_WIRE, 'B', Blocks.PLANKS);
	}
}
