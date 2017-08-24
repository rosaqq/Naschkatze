package net.secknv.nkmod.tileentity;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class NkTileEntities {


	public static void register() {

        GameRegistry.registerTileEntity(TileEntityNkGrinder.class, "nkmod.grinder");
    }
}
