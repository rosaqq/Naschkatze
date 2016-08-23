package net.secknv.nkmod.proxy;

import net.secknv.nkmod.world.oregen.NkWorldGens;

public abstract class CommonProxy implements IProxy {

    
	@Override
	public void registerRenders() {

	}

	@Override
	public void registerWorldGen() {

        NkWorldGens.register();
	}
}
