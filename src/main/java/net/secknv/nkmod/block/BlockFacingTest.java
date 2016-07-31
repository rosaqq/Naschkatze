package net.secknv.nkmod.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.secknv.nkmod.Naschkatze;

public class BlockFacingTest extends Block{
	
	private final String name = "test_block";
	
	public BlockFacingTest(){
		super(Material.ROCK);
		setUnlocalizedName(name);
		setCreativeTab(Naschkatze.tabNk);
		setHarvestLevel("pickaxe", 0);
		setHardness(1.0f);
		setResistance(5.0f);
		GameRegistry.registerBlock(this, name);
	}
}
