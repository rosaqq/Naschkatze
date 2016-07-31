package net.secknv.nkmod.proxy;

import net.secknv.nkmod.block.NkBlocks;
import net.secknv.nkmod.item.NkItems;

public class ClientProxy extends CommonProxy
{
	
	@Override
	public void registerRenders(){
		NkItems.registerRenders();
		NkBlocks.registerRenders();
	}

}
