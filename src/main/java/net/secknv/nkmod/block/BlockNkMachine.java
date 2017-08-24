package net.secknv.nkmod.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.secknv.nkmod.Naschkatze;
import net.secknv.nkmod.reference.Reference;

public abstract class BlockNkMachine extends BlockContainer{
	
	public static final PropertyDirection FACING = BlockHorizontal.FACING;

	public BlockNkMachine(String name) {
		
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
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
    }
	
	public abstract TileEntity createNewTileEntity(World worldIn, int meta);

}
