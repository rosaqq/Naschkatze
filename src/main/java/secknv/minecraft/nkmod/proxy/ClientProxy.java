package secknv.minecraft.nkmod.proxy;

import secknv.minecraft.nkmod.blocks.NkBlocks;
import secknv.minecraft.nkmod.items.NkItems;

public class ClientProxy extends CommonProxy
{
	
	@Override
	public void registerRenders(){
		NkItems.registerRenders();
		NkBlocks.registerRenders();
	}
}
