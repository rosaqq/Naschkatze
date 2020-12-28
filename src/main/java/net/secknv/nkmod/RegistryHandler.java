package net.secknv.nkmod;

import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.secknv.nkmod.blocks.GrinderBlock;
import net.secknv.nkmod.blocks.GrinderBlockTile;
import net.secknv.nkmod.blocks.UraniniteOreBlock;
import net.secknv.nkmod.items.BlockItemBase;
import net.secknv.nkmod.items.NkItem;

public class RegistryHandler {
    // create DeferredRegister object
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Naschkatze.MODID);
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Naschkatze.MODID);
    private static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Naschkatze.MODID);
    private static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Naschkatze.MODID);

    public static void init() {
        // attach DeferredRegister to the event bus
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    // register item
    public static final RegistryObject<Item> URANIUM_INGOT = ITEMS.register("uranium_ingot", NkItem::new);

    // register blocks
    public static final RegistryObject<Block> URANINITE_ORE = BLOCKS.register("uraninite_ore", UraniniteOreBlock::new);
    public static final RegistryObject<Block> GRINDER = BLOCKS.register("grinder", GrinderBlock::new);

    // block items
    public static final RegistryObject<Item> URANINITE_ORE_ITEM = ITEMS.register("uraninite_ore", () -> new BlockItemBase(URANINITE_ORE.get()));
    public static final RegistryObject<Item> GRINDER_ITEM = ITEMS.register("grinder", () -> new BlockItemBase(GRINDER.get()));

    // block TE
    public static final RegistryObject<TileEntityType<GrinderBlockTile>> GRINDER_TILE = TILES.register("grinder", () -> TileEntityType.Builder.create(GrinderBlockTile::new, GRINDER.get()).build(null));
}
