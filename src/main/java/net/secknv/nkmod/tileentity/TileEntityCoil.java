package net.secknv.nkmod.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.secknv.nkmod.block.BlockCoil;

public class TileEntityCoil extends TileEntity implements ITickable{
	
	private int ticks = 1;
	public boolean messUpCompass = false;
	
	
	public BlockPos getCoilPosition()
    {
        return TileEntityCoil.this.pos;
    }
	

	@Override
	public void update() {
		
		if(ticks == 20){
			this.messUpCompass = this.worldObj.getBlockState(this.pos).getValue(BlockCoil.ENABLED);
			ticks = 1;
		}
		ticks++;
	}
	
}
