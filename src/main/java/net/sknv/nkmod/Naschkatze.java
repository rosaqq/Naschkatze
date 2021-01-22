package net.sknv.nkmod;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.sknv.nkmod.world.gen.feature.NkOreGen;

@Mod(Naschkatze.MODID)
public class Naschkatze {


    public static final String MODID = "nkmod";


    public Naschkatze() {
        RegistrateHandler.init();

        // FMLCommonSetupEvent
        // First to fire, but after Registry events
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        // Registering Ourselves
        MinecraftForge.EVENT_BUS.register(this);

    }

    private void setup(final FMLCommonSetupEvent event) {
        NkOreGen.setup();
    }
}
