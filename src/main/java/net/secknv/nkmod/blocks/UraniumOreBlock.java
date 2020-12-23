package net.secknv.nkmod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class UraniumOreBlock extends Block {
    public UraniumOreBlock() {
        super(Block.Properties.create(Material.ROCK)
                .harvestLevel(2)
                .harvestTool(ToolType.PICKAXE));
    }
}
