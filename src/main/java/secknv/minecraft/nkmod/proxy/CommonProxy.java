package secknv.minecraft.nkmod.proxy;

import net.minecraftforge.fml.common.registry.GameRegistry;
import secknv.minecraft.nkmod.world.oregen.UraniumWorldGen;

public class CommonProxy{
	public void registerRenders()
	{
		
	}
	
	public void registerWorldGen()
	{
		GameRegistry.registerWorldGenerator(new UraniumWorldGen(), 0);
	}
}
