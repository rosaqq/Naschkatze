package net.secknv.nkmod;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.secknv.nkmod.block.NkBlocks;
import net.secknv.nkmod.handler.ConfigHandler;
import net.secknv.nkmod.item.NkItems;
import net.secknv.nkmod.proxy.IProxy;
import net.secknv.nkmod.recipes.RecipesCrafting;
import net.secknv.nkmod.recipes.RecipesSmelting;
import net.secknv.nkmod.reference.Reference;
import net.secknv.nkmod.tileentity.NkTileEntities;
import net.secknv.nkmod.util.LogHelper;

@Mod(modid = Reference.MODID, name=Reference.MODNAME, version = Reference.VERSION, guiFactory = Reference.GUI_FACTORY_CLASS)
public class Naschkatze{

	@Mod.Instance(Reference.MODID)
	public static Naschkatze instance;
	
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static IProxy proxy;
    
	public static final NkTab tabNk = new NkTab("tabNk");
	
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
    	
    	ConfigHandler.init(event.getSuggestedConfigurationFile());
        MinecraftForge.EVENT_BUS.register(new ConfigHandler());
    	
    	NkItems.init();
    	NkItems.register();
    	NkBlocks.init();
    	NkBlocks.register();

        LogHelper.info("preInit complete");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
    	
    	proxy.registerRenders();
    	proxy.registerWorldGen();
    	NkTileEntities.register();
    	RecipesCrafting.registerRecipes();
    	RecipesSmelting.registerRecipes();

        LogHelper.info("init complete");
    }
    
    @Mod.EventHandler
    public void postInit(FMLInitializationEvent event){

        LogHelper.info("postInit complete");
    	
    }
}
