package net.sknv.nkmod.blocks;

import net.minecraft.block.Block;
import net.minecraftforge.common.ToolType;

public class NkOreBlock extends Block {
    /**
     * Harvest Level must be set on registration!<br>
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
     * Hardness hardcoded to 3 due to most ores having 3.
     */
    public NkOreBlock(Properties p) {
        super(p.harvestTool(ToolType.PICKAXE).hardnessAndResistance(3));
    }
}
