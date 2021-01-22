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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.sknv.nkmod.blocks.NkOreBlock;
import net.sknv.nkmod.blocks.machines.GrinderContainer;
import net.sknv.nkmod.blocks.machines.GrinderTile;
import net.sknv.nkmod.blocks.machines.base.MachineBlock;
import net.sknv.nkmod.blocks.machines.base.MachineScreenFactory;
import net.sknv.nkmod.world.gen.feature.NkOreGen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

@Mod(Naschkatze.MODID)
public class Naschkatze {


    public static final String MODID = "nkmod";
    private static final Logger LOGGER = LogManager.getLogger();

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

    public Naschkatze() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        // Registering Ourselves
        MinecraftForge.EVENT_BUS.register(this);
        REGISTRATE = Registrate.create(MODID).itemGroup(()-> new ItemGroup("tabNk") {
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

    private void setup(final FMLCommonSetupEvent event) {
        NkOreGen.setup();
    }
}
