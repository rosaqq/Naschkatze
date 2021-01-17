package net.sknv.nkmod;

import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.sknv.nkmod.blocks.machines.GrinderContainer;
import net.sknv.nkmod.blocks.machines.GrinderTile;
import net.sknv.nkmod.blocks.machines.ReactionChamberContainer;
import net.sknv.nkmod.blocks.machines.ReactionChamberTile;
import net.sknv.nkmod.blocks.machines.base.MachineBlock;
import net.sknv.nkmod.blocks.NkOreBlock;
import net.sknv.nkmod.items.NkBlockItem;
import net.sknv.nkmod.items.NkItem;

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

    // register blocks
    public static final RegistryObject<Block> URANINITE_ORE = BLOCKS.register("uraninite_ore", () -> new NkOreBlock(2,3));

    // How to add MachineBlocks
    // Create XXBlockContainer extends AbstractMachineContainer, register here
    // Create XXBlockTile extends AbstractMachineTile, register here
    // Register block here with () -> new MachineBlock(XXBlockTile::new)
    // Register BlockItem
    // Register screen on ClientSetup with ClientSetupMachineScreenFactory<>("textures/gui/xx_gui.png")

    /*
    * Traditionally:
    * Block -> Tile
    * Tile -> Container
    * ClientSetup registers Container <-> Screen
    * */

    // todo: document machine blocks abstract classes and interfaces
    // todo: make wrapper method to add machines that adds Block, Tile, Container, BlockItem automatically.

    public static final RegistryObject<Block> GRINDER = BLOCKS.register("grinder", () -> new MachineBlock(GrinderTile::new));
    public static final RegistryObject<Block> REACTIONCHAMBER = BLOCKS.register("reaction_chamber", () -> new MachineBlock(ReactionChamberTile::new));


    // register block items
    public static final RegistryObject<Item> URANINITE_ORE_ITEM = ITEMS.register("uraninite_ore", () -> new NkBlockItem(URANINITE_ORE.get()));
    public static final RegistryObject<Item> GRINDER_ITEM = ITEMS.register("grinder", () -> new NkBlockItem(GRINDER.get()));
    public static final RegistryObject<Item> REACTIONCHAMBER_ITEM = ITEMS.register("reaction_chamber", () -> new NkBlockItem(REACTIONCHAMBER.get()));

    // register block TEs
    public static final RegistryObject<TileEntityType<GrinderTile>> GRINDER_TILE = TILES.register("grinder", () -> TileEntityType.Builder.create(GrinderTile::new, GRINDER.get()).build(null));
    public static final RegistryObject<TileEntityType<ReactionChamberTile>> REACTIONCHAMBER_TILE = TILES.register("reaction_chamber", () -> TileEntityType.Builder.create(ReactionChamberTile::new, GRINDER.get()).build(null));

    // register block containers
    public static final RegistryObject<ContainerType<GrinderContainer>> GRINDER_CONTAINER = CONTAINERS.register("grinder", () -> IForgeContainerType.create((windowId, inv, data) -> new GrinderContainer(windowId, inv.player.getEntityWorld(), data.readBlockPos(), inv, inv.player)));
    public static final RegistryObject<ContainerType<ReactionChamberContainer>> REACTIONCHAMBER_CONTAINER = CONTAINERS.register("reaction_chamber", () -> IForgeContainerType.create((windowId, inv, data) -> new ReactionChamberContainer(windowId, inv.player.getEntityWorld(), data.readBlockPos(), inv, inv.player)));


    // register items
    public static final RegistryObject<Item> CRUSHED_URANINITE = ITEMS.register("crushed_uraninite", NkItem::new);
    public static final RegistryObject<Item> YELLOWCAKE = ITEMS.register("yellowcake", NkItem::new);
    public static final RegistryObject<Item> URANIUM_INGOT = ITEMS.register("uranium_ingot", NkItem::new);
    public static final RegistryObject<Item> UO2_INGOT = ITEMS.register("uo2_ingot", NkItem::new);
    public static final RegistryObject<Item> GRAPHITE_INGOT = ITEMS.register("graphite_ingot", NkItem::new);
}
