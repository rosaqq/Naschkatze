package net.secknv.nkmod.recipes;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.secknv.nkmod.block.NkBlocks;
import net.secknv.nkmod.item.NkItems;

public class RecipesSmelting {

	public static void registerRecipes() {

		GameRegistry.addSmelting(NkBlocks.COPPER_ORE, new ItemStack(NkItems.COPPER_INGOT, 1), 0.7F);
	}

}