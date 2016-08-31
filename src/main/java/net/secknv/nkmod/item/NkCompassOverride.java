package net.secknv.nkmod.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.init.Items;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.secknv.nkmod.handler.ConfigHandler;
import net.secknv.nkmod.tileentity.TileEntityCoil;
import net.secknv.nkmod.util.WorldHelper;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static sun.audio.AudioPlayer.player;

public class NkCompassOverride {
	
	public static void asdf() {

		Items.COMPASS.addPropertyOverride(new ResourceLocation("angle"), new IItemPropertyGetter() {

            @SideOnly(Side.CLIENT)
            double rotation;
            @SideOnly(Side.CLIENT)
            double rota;
            @SideOnly(Side.CLIENT)
            long lastUpdateTick;
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {

                if (entityIn == null && !stack.isOnItemFrame()) {

                    return 0.0F;
                }
                else {

                    boolean flag = entityIn != null;
                    Entity entity = flag ? entityIn : stack.getItemFrame();

                    if (worldIn == null) {

                        worldIn = entity.worldObj;
                    }

                    double d0;

                    if (worldIn.provider.isSurfaceWorld()) {

                        double d1 = flag ? (double)entity.rotationYaw : this.getFrameRotation((EntityItemFrame)entity);
                        d1 = (double)MathHelper.positiveModulo( (float)d1 / 360F, 1.0F );
                        double d2 = this.getSpawnToAngle(worldIn, entity) / (Math.PI * 2D);
                        d0 = 0.5D - ((d1 - 0.25D) - d2);
                    }
                    else {

                        d0 = Math.random();
                    }

                    if (flag) {

                        d0 = this.wobble(worldIn, d0);
                    }

                    return MathHelper.positiveModulo((float)d0, 1.0F);
                }
            }

            
            @SideOnly(Side.CLIENT)
            private double wobble(World worldIn, double num) {

                if (worldIn.getTotalWorldTime() != this.lastUpdateTick) {

                    this.lastUpdateTick = worldIn.getTotalWorldTime();
                    double d0 = num - this.rotation;
                    d0 = (double)MathHelper.positiveModulo( (float)d0 + 0.5F, 1.0F ) - 0.5D;
                    this.rota += d0 * 0.1D;
                    this.rota *= 0.8D;
                    this.rotation += this.rota;
                    this.rotation = (double)MathHelper.positiveModulo( (float)this.rotation, 1.0F );
                }

                return this.rotation;
            }
            @SideOnly(Side.CLIENT)
            private double getFrameRotation(EntityItemFrame entFrame) {

                return (double)MathHelper.clampAngle(180 + entFrame.facingDirection.getHorizontalIndex() * 90);
            }
            @SideOnly(Side.CLIENT)
            private double getSpawnToAngle(World worldIn, Entity ent) {

                TileEntityCoil closestActiveCoil = getClosestActivatedCoil(worldIn, ent);
                BlockPos blockpos = ( closestActiveCoil == null ? worldIn.getSpawnPoint() : closestActiveCoil.getPos());

            	return Math.atan2((double)blockpos.getZ() - ent.posZ, (double)blockpos.getX() - ent.posX);
            }

            @SideOnly(Side.CLIENT)
            private TileEntityCoil getClosestActivatedCoil(World worldIn, Entity ent) {
                LinkedList<TileEntity> coilList = WorldHelper.getTileEntitiesWithinAABB(worldIn, TileEntityCoil.class, WorldHelper.createAABBFromBlockPos(ent.getPosition(), ConfigHandler.coilRadius + 2));
                Iterator coilIterator = coilList.iterator();
                while (coilIterator.hasNext()) {
                    TileEntityCoil coil = (TileEntityCoil) coilIterator.next();
                    if (!coil.messUpCompass){
                        coilIterator.remove();
                    }
                }
                return coilList.isEmpty() ? null : (TileEntityCoil) WorldHelper.closestTEToPlayer(coilList, ent);
            }
        });
		
	}

}
