package net.secknv.nkmod.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.secknv.nkmod.Naschkatze;

public class BlockNkOre extends Block
{

	public BlockNkOre(int harvestLevel)
	{
		super(Material.ROCK);
		setCreativeTab(Naschkatze.tabNk);
		setSoundType(SoundType.STONE);
		setHardness(4.0f);
		setResistance(5.0f);
		setHarvestLevel("pickaxe",harvestLevel);
	}

}
