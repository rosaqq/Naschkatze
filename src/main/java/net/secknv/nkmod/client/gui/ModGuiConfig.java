package net.secknv.nkmod.client.gui;


import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.secknv.nkmod.handler.ConfigHandler;
import net.secknv.nkmod.reference.Reference;

public class ModGuiConfig extends GuiConfig {

    public ModGuiConfig(GuiScreen screen) {
        super(screen, new ConfigElement(ConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(),
                Reference.MOD_ID, false, false, GuiConfig.getAbridgedConfigPath(ConfigHandler.config.toString()));
    }
}
