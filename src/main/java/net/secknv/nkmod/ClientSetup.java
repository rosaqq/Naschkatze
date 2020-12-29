package net.secknv.nkmod;

import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.secknv.nkmod.blocks.GrinderBlockScreen;

@Mod.EventBusSubscriber(modid = Naschkatze.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    public static void init(final FMLClientSetupEvent event) {
        ScreenManager.registerFactory(RegistryHandler.GRINDER_CONTAINER.get(), GrinderBlockScreen::new);
    }
}
