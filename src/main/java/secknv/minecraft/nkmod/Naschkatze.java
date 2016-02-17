package secknv.minecraft.nkmod;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import secknv.minecraft.nkmod.blocks.NkBlocks;
import secknv.minecraft.nkmod.items.NkItems;
import secknv.minecraft.nkmod.proxy.CommonProxy;

@Mod(modid = Reference.MODID, version = Reference.VERSION)
public class Naschkatze{
	
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	NkItems.init();
    	NkBlocks.init();
    	
    }

    @EventHandler
    public void init(FMLInitializationEvent event){
    	proxy.registerRenders();
    }
}
