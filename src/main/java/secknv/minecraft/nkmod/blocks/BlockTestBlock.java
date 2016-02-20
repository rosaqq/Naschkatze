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
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import secknv.minecraft.nkmod.Naschkatze;

public class BlockTestBlock extends Block{
	
	private final String name = "test_block";
	
	public BlockTestBlock(){
		super(Material.rock);
		setUnlocalizedName(name);
		setCreativeTab(Naschkatze.tabNk);
		setHarvestLevel("pickaxe", 0);
		setHardness(1.0f);
		setResistance(5.0f);
		GameRegistry.registerBlock(this, name);
	}
	
	@SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.CUTOUT;
    }
	
	public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean isFullCube()
    {
        return false;
    }
}
