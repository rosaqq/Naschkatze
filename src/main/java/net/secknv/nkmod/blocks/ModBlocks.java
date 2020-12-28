package net.secknv.nkmod.blocks;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

public class ModBlocks {

    @ObjectHolder("nkmod:grinder")
    public static GrinderBlock GRINDER_BLOCK;

    @ObjectHolder("nkmod:grinder")
    public static TileEntityType<GrinderBlockTile> GRINDER_TILE;
}
