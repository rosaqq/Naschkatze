package secknv.minecraft.nkmod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import secknv.minecraft.nkmod.Naschkatze;

public class BlockTestBlock extends Block
{
	
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	private final String name = "test_block";
	
	public BlockTestBlock()
	{
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
	
	@Override
	public boolean isOpaqueCube()
    {
        return false;
    }
	
	@Override
    public boolean isFullCube()
    {
        return false;
    }
    
	@Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
        worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
    }
	
	@Override
    public IBlockState getStateFromMeta(int meta)
	{
        EnumFacing enumfacing = EnumFacing.getFront(meta);
        
        if (enumfacing.getAxis() == EnumFacing.Axis.Y)
        {
            enumfacing = EnumFacing.NORTH;
        }
		
        return this.getDefaultState().withProperty(FACING, enumfacing);
    }
    
	@Override
    public int getMetaFromState(IBlockState state){
        return ((EnumFacing)state.getValue(FACING)).getIndex();
    }
    
	@Override
    protected BlockState createBlockState(){
        return new BlockState(this, new IProperty[] {FACING});
    }
}