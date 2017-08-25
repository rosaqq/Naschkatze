package net.secknv.nkmod.recipes;

import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.secknv.nkmod.block.NkBlocks;

public class GrinderRecipes {
	
	private static final GrinderRecipes GRINDING_BASE = new GrinderRecipes();
    /** The list of grinding results. */
    private final Map<ItemStack, ItemStack> grindList = Maps.<ItemStack, ItemStack>newHashMap();
    /** A list which contains how many experience points each recipe output will give. */
    private final Map<ItemStack, Float> xpList = Maps.<ItemStack, Float>newHashMap();

    /**
     * Returns an instance of GrinderRecipes.
     */
    public static GrinderRecipes instance() {
        return GRINDING_BASE;
    }

    private GrinderRecipes() {
    	
        this.addGrindRecipeForBlock(NkBlocks.URANIUM_ORE, new ItemStack(Items.REDSTONE, 2), 0.7F);
    }

    /**
     * Adds a grind recipe, where the input item is an instance of Block.
     */
    public void addGrindRecipeForBlock(Block input, ItemStack stack, float xp) {
        this.addGrind(Item.getItemFromBlock(input), stack, xp);
    }

    /**
     * Adds a grind recipe using an Item as the input item.
     */
    public void addGrind(Item input, ItemStack stack, float experience) {
        this.addGrindRecipe(new ItemStack(input, 1, 32767), stack, experience);
    }

    /**
     * Adds a grind recipe using an ItemStack as the input for the recipe.
     */
    public void addGrindRecipe(ItemStack input, ItemStack stack, float experience) {
        if (getGrindResult(input) != null) { net.minecraftforge.fml.common.FMLLog.info("Ignored grinder recipe with conflicting input: " + input + " = " + stack); return; }
        this.grindList.put(input, stack);
        this.xpList.put(stack, Float.valueOf(experience));
    }

    /**
     * Returns the grind result of an item.
     */
    @Nullable
    public ItemStack getGrindResult(ItemStack stack) {
        for (Entry<ItemStack, ItemStack> entry : this.grindList.entrySet()) {
            if (this.compareItemStacks(stack, (ItemStack)entry.getKey())) {
                return (ItemStack)entry.getValue();
            }
        }

        return null;
    }

    /**
     * Compares two itemstacks to ensure that they are the same. This checks both the item and the metadata of the item.
     */
    private boolean compareItemStacks(ItemStack stack1, ItemStack stack2) {
        return stack2.getItem() == stack1.getItem() && (stack2.getMetadata() == 32767 || stack2.getMetadata() == stack1.getMetadata());
    }

    public Map<ItemStack, ItemStack> getGrindingList() {
        return this.grindList;
    }

    public float getGrindExperience(ItemStack stack) {

        for (Entry<ItemStack, Float> entry : this.xpList.entrySet()) {
            if (this.compareItemStacks(stack, (ItemStack)entry.getKey())) {
                return ((Float)entry.getValue()).floatValue();
            }
        }

        return 0.0F;
    }

}
