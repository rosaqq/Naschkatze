package net.secknv.nkmod;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.secknv.nkmod.block.NkBlocks;
import net.secknv.nkmod.config.ConfigHandler;
import net.secknv.nkmod.item.NkItems;
import net.secknv.nkmod.proxy.IProxy;
import net.secknv.nkmod.recipes.RecipesCrafting;
import net.secknv.nkmod.recipes.RecipesSmelting;
import net.secknv.nkmod.reference.Reference;
import net.secknv.nkmod.tileentity.NkTileEntities;

@Mod(modid = Reference.MODID, name=Reference.MODNAME, version = Reference.VERSION)
public class Naschkatze{
	
	@Mod.Instance(Reference.MODID)
	public static Naschkatze instance;
	
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static IProxy proxy;
    
	public static final NkTab tabNk = new NkTab("tabNk");
	
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
    	
    	ConfigHandler.init(event.getSuggestedConfigurationFile());
    	
    	NkItems.init();
    	NkItems.register();
    	NkBlocks.init();
    	NkBlocks.register();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
    	
    	proxy.registerRenders();
    	proxy.registerWorldGen();
    	NkTileEntities.register();
    	RecipesCrafting.registerRecipes();
    	RecipesSmelting.registerRecipes();
    }
    
    @Mod.EventHandler
    public void postInit(FMLInitializationEvent event){
    	
    }
}
