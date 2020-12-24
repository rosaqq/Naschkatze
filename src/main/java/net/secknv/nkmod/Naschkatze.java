package net.secknv.nkmod;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Naschkatze.MODID)
public class Naschkatze {


    public static final String MODID = "nkmod";
    private static final Logger LOGGER = LogManager.getLogger();

    public static final ItemGroup TAB = new ItemGroup("tabNk") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(RegistryHandler.URANIUM_INGOT.get());
        }
    };

    public Naschkatze() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        RegistryHandler.init();
    }

    private void setup(final FMLCommonSetupEvent event) {
        // pre init code
        LOGGER.info("nkmod pre init");
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
    }
}
