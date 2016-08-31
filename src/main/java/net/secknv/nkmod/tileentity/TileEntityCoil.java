package net.secknv.nkmod.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.secknv.nkmod.block.BlockCoil;
import net.secknv.nkmod.handler.ConfigHandler;

public class TileEntityCoil extends TileEntity implements ITickable {
	
	private int ticks = 1;
	public boolean messUpCompass = false;
    private int activatingRangeFromPlayer = ConfigHandler.coilRadius;


	private boolean isActivated() {
		BlockPos blockpos = this.pos;
		return this.worldObj.isAnyPlayerWithinRangeAt((double)blockpos.getX() + 0.5D, (double)blockpos.getY() + 0.5D, (double)blockpos.getZ() + 0.5D, this.activatingRangeFromPlayer);
	}

	@Override
	public void update() {
		
		if(ticks == 15){
			this.messUpCompass = this.worldObj.getBlockState(this.pos).getValue(BlockCoil.ENABLED) && isActivated();
			ticks = 1;
		}
		ticks++;
	}
	
}
