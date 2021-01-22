package net.sknv.nkmod;


import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ForgeRegistries;
import net.sknv.nkmod.blocks.NkOreBlock;
import net.sknv.nkmod.blocks.machines.GrinderContainer;
import net.sknv.nkmod.blocks.machines.GrinderTile;
import net.sknv.nkmod.blocks.machines.base.MachineBlock;
import net.sknv.nkmod.blocks.machines.base.MachineScreenFactory;

import javax.annotation.Nonnull;

public class RegistrateHandler {

    public static Registrate REGISTRATE;

    // register blocks
    public static RegistryEntry<NkOreBlock> URANINITE_ORE;
    public static RegistryEntry<MachineBlock> GRINDER;
    public static RegistryEntry<TileEntityType<GrinderTile>> GRINDER_TILE;
    public static RegistryEntry<ContainerType<Container>> GRINDER_CONTAINER;
    // register items
    public static RegistryEntry<Item> CRUSHED_URANINITE;
    public static RegistryEntry<Item> YELLOWCAKE;
    public static RegistryEntry<Item> URANIUM_INGOT;
    public static RegistryEntry<Item> UO2_INGOT;
    public static RegistryEntry<Item> GRAPHITE_INGOT;

    public static void init() {
        REGISTRATE = Registrate.create(Naschkatze.MODID).itemGroup(()-> new ItemGroup("tabNk") {
            @Nonnull
            @Override
            public ItemStack createIcon() {
                return new ItemStack(URANIUM_INGOT.get());
            }
        });
        URANINITE_ORE = REGISTRATE.object("uraninite_ore").block(NkOreBlock::new).properties(p -> p.harvestLevel(2)).simpleItem().register();
        GRINDER = REGISTRATE.object("grinder").block(Material.IRON, p -> new MachineBlock(p, () -> new GrinderTile(GRINDER.getSibling(ForgeRegistries.TILE_ENTITIES).get())))
                .simpleTileEntity(GrinderTile::new)
                .simpleItem().register();
        // todo: GrinderContainer should not ignore type param
        // todo: data.readBlockPos() ok? NPE?
        GRINDER_CONTAINER = REGISTRATE.container((type, windowId, inv, data) -> new GrinderContainer(windowId, inv, data.readBlockPos()), () -> new MachineScreenFactory<>("textures/gui/grinder_gui.png")).register();
        // register items
        CRUSHED_URANINITE = REGISTRATE.object("crushed_uraninite").item(Item::new).register();
        YELLOWCAKE = REGISTRATE.object("yellowcake").item(Item::new).register();
        URANIUM_INGOT = REGISTRATE.object("uranium_ingot").item(Item::new).register();
        UO2_INGOT = REGISTRATE.object("uo2_ingot").item(Item::new).register();
        GRAPHITE_INGOT = REGISTRATE.object("graphite_ingot").item(Item::new).register();
    }



    /*
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
    public static final RegistryObject<TileEntityType<ReactionChamberTile>> REACTIONCHAMBER_TILE = TILES.register("reaction_chamber", () -> TileEntityType.Builder.create(ReactionChamberTile::new, REACTIONCHAMBER.get()).build(null));

    // register block containers
    public static final RegistryObject<ContainerType<GrinderContainer>> GRINDER_CONTAINER = CONTAINERS.register("grinder", () -> IForgeContainerType.create((windowId, inv, data) -> new GrinderContainer(windowId, inv.player.getEntityWorld(), data.readBlockPos(), inv, inv.player)));
    public static final RegistryObject<ContainerType<ReactionChamberContainer>> REACTIONCHAMBER_CONTAINER = CONTAINERS.register("reaction_chamber", () -> IForgeContainerType.create((windowId, inv, data) -> new ReactionChamberContainer(windowId, inv.player.getEntityWorld(), data.readBlockPos(), inv, inv.player)));
*/



}
