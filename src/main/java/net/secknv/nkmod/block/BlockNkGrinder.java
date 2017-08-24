package net.secknv.nkmod.block;

import javax.annotation.Nullable;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.secknv.nkmod.Naschkatze;
import net.secknv.nkmod.reference.Reference;
import net.secknv.nkmod.tileentity.TileEntityNkGrinder;

public class BlockNkGrinder extends BlockContainer{
	
	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	private final String name = "grinder";
    private static boolean keepInventory=false;
	
	public BlockNkGrinder() {
		super(Material.IRON);
		setRegistryName(name);
		setUnlocalizedName(Reference.MOD_ID + "." + name);
		setCreativeTab(Naschkatze.tabNk);
		setSoundType(SoundType.METAL);
		setHardness(3.5F);
		setResistance(5.0f);
		setHarvestLevel("pickaxe",1);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityNkGrinder();
	}
	
	@Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
    }
	
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        
		if (worldIn.isRemote) {
            return true;
        }
        else {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityNkGrinder) {
                playerIn.displayGUIChest((TileEntityNkGrinder)tileentity);
            }

            return true;
        }
    }
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		
        if (!keepInventory) {
        	
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityNkGrinder) {
                InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityNkGrinder)tileentity);
                worldIn.updateComparatorOutputLevel(pos, this);
            }
        }

        super.breakBlock(worldIn, pos, state);
    }
	
	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(NkBlocks.GRINDER);
    }
	
	
	@Override
    public IBlockState getStateFromMeta(int meta) {
        
        EnumFacing enumfacing = EnumFacing.getFront(meta);

        if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
            enumfacing = EnumFacing.NORTH;
        }
        return this.getDefaultState().withProperty(FACING, enumfacing);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }
    

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }
    
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }
	

}
