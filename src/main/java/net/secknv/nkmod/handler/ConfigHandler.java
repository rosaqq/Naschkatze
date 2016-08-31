package net.secknv.nkmod.handler;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.secknv.nkmod.reference.Reference;

public class ConfigHandler {

	public static Configuration config;
    public static int coilRadius = 3;


	public static void init(File configFile) {

	    if(config == null) {
            config = new Configuration(configFile);
            loadConfiguration();
        }
	}

	@SubscribeEvent
	public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {

        if(event.getModID().equalsIgnoreCase(Reference.MOD_ID)) {
            loadConfiguration();
        }
    }

    private static void loadConfiguration() {

        coilRadius = config.getInt("Coil Influence Radius", Configuration.CATEGORY_GENERAL, 3, 0, 100, "Set the radius of the coils' influence on the compass.");

        if(config.hasChanged()) {
            config.save();
        }
    }
}
