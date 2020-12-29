package net.secknv.nkmod.items;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.secknv.nkmod.Naschkatze;

public class NkBlockItem extends BlockItem {

    public NkBlockItem(Block blockIn) {
        super(blockIn, new Item.Properties().group(Naschkatze.TAB));
    }
}
