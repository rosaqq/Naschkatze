package net.secknv.nkmod.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import static net.minecraft.world.World.MAX_ENTITY_RADIUS;

/**
 * Created by secknv on 28/08/2016.
 */

public class WorldHelper {


    public static AxisAlignedBB createAABBFromBlockPos(BlockPos pos, double radius) {

        AxisAlignedBB block = new AxisAlignedBB(pos);

        return block.expand(radius, radius, radius);
    }

    private static final Method methodIsChunkLoaded;

    static {
        Method m;
        try {
            m = World.class.getDeclaredMethod("isChunkLoaded", int.class, int.class, boolean.class);
            m.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        methodIsChunkLoaded = m;
    }

    public static List<TileEntity> getTileEntitiesWithinAABB(World world, Class<net.secknv.nkmod.tileentity.TileEntityCoil> tileEntityClass, AxisAlignedBB aabb)
    {
        int i = MathHelper.floor_double((aabb.minX - MAX_ENTITY_RADIUS) / 16.0D);
        int j = MathHelper.floor_double((aabb.maxX + MAX_ENTITY_RADIUS) / 16.0D);
        int k = MathHelper.floor_double((aabb.minZ - MAX_ENTITY_RADIUS) / 16.0D);
        int l = MathHelper.floor_double((aabb.maxZ + MAX_ENTITY_RADIUS) / 16.0D);
        List<TileEntity> list = new LinkedList<TileEntity>();

        for (int i1 = i; i1 <= j; ++i1)
        {
            for (int j1 = k; j1 <= l; ++j1)
            {
                boolean chunkLoaded = false;
                try {
                    chunkLoaded = (Boolean) methodIsChunkLoaded.invoke(world, i1, j1, true);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

                if(chunkLoaded)
                {

                    for (TileEntity te : world.getChunkFromChunkCoords(i1, j1).getTileEntityMap().values()) {
                        if (tileEntityClass.isInstance(te)) {
                            if (te.getRenderBoundingBox().intersectsWith(aabb)) {
                                list.add(te);
                            }
                        }
                    }
                }
            }
        }
        return list;
    }
}
