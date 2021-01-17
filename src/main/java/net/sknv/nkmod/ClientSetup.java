package net.sknv.nkmod;

import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.sknv.nkmod.blocks.machines.base.MachineScreenFactory;

@Mod.EventBusSubscriber(modid = Naschkatze.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    public static void init(final FMLClientSetupEvent event) {
        ScreenManager.registerFactory(RegistryHandler.GRINDER_CONTAINER.get(), new MachineScreenFactory<>("textures/gui/grinder_gui.png"));
        ScreenManager.registerFactory(RegistryHandler.REACTIONCHAMBER_CONTAINER.get(), new MachineScreenFactory<>("textures/gui/grinder_gui.png"));
    }
}
