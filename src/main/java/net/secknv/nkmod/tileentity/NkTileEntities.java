package net.secknv.nkmod.tileentity;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class NkTileEntities {
	
	public static TileEntityCoil CoilList[] = new TileEntityCoil[20];

	public static void register() {
        GameRegistry.registerTileEntity(TileEntityCoil.class, "nkmod.copper_coil");
    }
}
