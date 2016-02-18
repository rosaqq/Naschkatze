package secknv.minecraft.nkmod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.registry.GameRegistry;
import secknv.minecraft.nkmod.Reference;

public class BlockUraniumOre extends Block{
	
	private final String name = "uranium_ore";
	
	public BlockUraniumOre(){
		super(Material.rock);
		setUnlocalizedName(name);
		setCreativeTab(CreativeTabs.tabBlock);
		setHarvestLevel("pickaxe", 2);
		setHardness(4.0f);
		setResistance(5.0f);
		GameRegistry.registerBlock(this, name);
	}
}
