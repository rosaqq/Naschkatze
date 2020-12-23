package net.secknv.nkmod;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.secknv.nkmod.blocks.GrinderBlock;
import net.secknv.nkmod.blocks.UraniumOreBlock;
import net.secknv.nkmod.items.BlockItemBase;
import net.secknv.nkmod.items.NkItem;

public class RegistryHandler {
    // create DeferredRegister object
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Naschkatze.MODID);
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Naschkatze.MODID);

    public static void init() {
        // attach DeferredRegister to the event bus
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    // register item
    public static final RegistryObject<Item> URANIUM_INGOT = ITEMS.register("uranium_ingot", NkItem::new);

    // register blocks
    public static final RegistryObject<Block> URANIUM_ORE = BLOCKS.register("uranium_ore", UraniumOreBlock::new);
    public static final RegistryObject<Block> GRINDER = BLOCKS.register("grinder", GrinderBlock::new);

    // block items
    public static final RegistryObject<Item> URANIUM_ORE_ITEM = ITEMS.register("uranium_ore", () -> new BlockItemBase(URANIUM_ORE.get()));
    public static final RegistryObject<Item> GRINDER_ITEM = ITEMS.register("grinder", () -> new BlockItemBase(GRINDER.get()));
}
