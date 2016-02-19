package secknv.minecraft.nkmod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import secknv.minecraft.nkmod.Naschkatze;

public class BlockFacingTest extends Block{
	
	private final String name = "test_block";
	
	public BlockFacingTest(){
		super(Material.rock);
		setUnlocalizedName(name);
		setCreativeTab(Naschkatze.tabNk);
		setHarvestLevel("pickaxe", 0);
		setHardness(1.0f);
		setResistance(5.0f);
		GameRegistry.registerBlock(this, name);
	}
}
