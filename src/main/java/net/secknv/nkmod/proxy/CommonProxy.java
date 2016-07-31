package net.secknv.nkmod.proxy;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.secknv.nkmod.world.oregen.CopperWorldGen;
import net.secknv.nkmod.world.oregen.UraniumWorldGen;

public class CommonProxy{
	public void registerRenders()
	{
		
	}
	
	public void registerWorldGen()
	{
		GameRegistry.registerWorldGenerator(new UraniumWorldGen(), 0);
		GameRegistry.registerWorldGenerator(new CopperWorldGen(), 0);
	}
}
