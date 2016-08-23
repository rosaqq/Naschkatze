package net.secknv.nkmod.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class ConfigHandler {
	
	public static void init(File configFile) {
		
		Configuration config = new Configuration(configFile);
		
		boolean configValue = false;
		
		try {
			
			config.load();
			configValue = config.get(config.CATEGORY_GENERAL, "configValue", true, "example config value").getBoolean(true);
			
		}
		catch(Exception e) {
			
			//log the exception
		}
		finally {
			
			config.save();
		}
		
		System.out.println(configValue);
	}

}
