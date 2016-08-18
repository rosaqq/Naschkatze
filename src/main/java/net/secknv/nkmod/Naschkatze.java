package net.secknv.nkmod;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.secknv.nkmod.block.NkBlocks;
import net.secknv.nkmod.item.NkItems;
import net.secknv.nkmod.proxy.CommonProxy;
import net.secknv.nkmod.recipes.RecipesCrafting;
import net.secknv.nkmod.recipes.RecipesSmelting;
import net.secknv.nkmod.tileentity.NkTileEntities;
import net.secknv.nkmod.tileentity.TileEntityCoil;

@Mod(modid = Reference.MODID, version = Reference.VERSION)
public class Naschkatze
{
	
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;
    
	public static final NkTab tabNk = new NkTab("tabNk");
	
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	NkItems.init();
    	NkItems.register();
    	NkBlocks.init();
    	NkBlocks.register();
    	NkTileEntities.register();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	proxy.registerRenders();
    	proxy.registerWorldGen();
    	RecipesCrafting.registerRecipes();
    	RecipesSmelting.registerRecipes();
    }
}
