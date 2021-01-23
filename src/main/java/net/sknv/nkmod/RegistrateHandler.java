package net.sknv.nkmod;


import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.sknv.nkmod.blocks.NkOreBlock;
import net.sknv.nkmod.blocks.machines.GrinderContainer;
import net.sknv.nkmod.blocks.machines.GrinderTile;
import net.sknv.nkmod.blocks.machines.base.MachineBlock;
import net.sknv.nkmod.blocks.machines.base.MachineScreenFactory;

import javax.annotation.Nonnull;

public class RegistrateHandler {

    public static Registrate REGISTRATE;

    // blocks
    public static RegistryEntry<NkOreBlock> URANINITE_ORE;
    public static RegistryEntry<MachineBlock> GRINDER;
    public static RegistryEntry<ContainerType<Container>> GRINDER_CONTAINER;

    // items
    public static RegistryEntry<Item> CRUSHED_URANINITE;
    public static RegistryEntry<Item> YELLOWCAKE;
    public static RegistryEntry<Item> URANIUM_INGOT;
    public static RegistryEntry<Item> UO2_INGOT;
    public static RegistryEntry<Item> GRAPHITE_INGOT;

    public static void init() {

        REGISTRATE = Registrate.create(Naschkatze.MODID).itemGroup(()-> new ItemGroup("nk_tab") {
            @Nonnull
            @Override
            public ItemStack createIcon() {
                return new ItemStack(URANIUM_INGOT.get());
            }
        });

        // blocks
        URANINITE_ORE = REGISTRATE.object("uraninite_ore").block(NkOreBlock::new).properties(p -> p.harvestLevel(2)).simpleItem().register();
        GRINDER = REGISTRATE.object("grinder").block(Material.IRON, p -> new MachineBlock(p, () -> new GrinderTile(GRINDER.getSibling(ForgeRegistries.TILE_ENTITIES).get())))
                .simpleTileEntity(GrinderTile::new)
                .simpleItem().register();
        // todo: GrinderContainer should not ignore type param
        // todo: data.readBlockPos() ok? NPE?
        GRINDER_CONTAINER = REGISTRATE.container((type, windowId, inv, data) -> new GrinderContainer(windowId, inv, data.readBlockPos()), () -> new MachineScreenFactory<>("textures/gui/grinder_gui.png")).register();

        // items
        CRUSHED_URANINITE = REGISTRATE.object("crushed_uraninite").item(Item::new).register();
        YELLOWCAKE = REGISTRATE.object("yellowcake").item(Item::new).register();
        URANIUM_INGOT = REGISTRATE.object("uranium_ingot").item(Item::new).register();
        UO2_INGOT = REGISTRATE.object("uo2_ingot").item(Item::new).register();
        GRAPHITE_INGOT = REGISTRATE.object("graphite_ingot").item(Item::new).register();
    }
}
