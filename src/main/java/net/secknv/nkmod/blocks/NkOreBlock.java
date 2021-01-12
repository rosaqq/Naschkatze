package net.secknv.nkmod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class NkOreBlock extends Block {
    /**
     * Harvest Level Reference:<br>
     * 0 -> Wood<br>
     * 1 -> Stone<br>
     * 2 -> Iron<br>
     * 3 -> Diamond<br>
     * <br>
     * Hardness Reference:<br>
     * 0.5 -> Dirt<br>
     * 2 -> Cobble<br>
     * 3 -> Most Ores<br>
     * 5 -> Block of Iron / Diamond<br>
     * 50 -> Obsidian<br>
     * @param harvestLevel Tool grade required.
     * @param hardness Affects breaking time.
     */
    public NkOreBlock(int harvestLevel, int hardness) {
        super(Block.Properties.create(Material.ROCK)
                .harvestTool(ToolType.PICKAXE)
                .harvestLevel(harvestLevel)
                .hardnessAndResistance(hardness));
    }
}
