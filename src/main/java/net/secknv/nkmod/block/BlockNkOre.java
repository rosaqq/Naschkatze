package net.secknv.nkmod.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.secknv.nkmod.Naschkatze;

public class BlockNkOre extends BlockNk
{

	public BlockNkOre(String name, int harvestLevel)
	{
		super(name, Material.ROCK, SoundType.STONE, 4.0f, 5.0f);
		setHarvestLevel("pickaxe",harvestLevel);
	}

}
