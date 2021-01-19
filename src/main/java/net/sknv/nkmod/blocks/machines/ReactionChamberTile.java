package net.sknv.nkmod.blocks.machines;

import cofh.thermal.core.ThermalCore;
import cofh.thermal.core.common.ThermalItemGroups;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import net.sknv.nkmod.blocks.machines.base.AbstractMachineTile;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nonnull;
import static net.sknv.nkmod.RegistryHandler.*;

public class ReactionChamberTile extends AbstractMachineTile {

    public ReactionChamberTile() {
        super(REACTIONCHAMBER_TILE.get(), ReactionChamberContainer::new);
    }

    @Nonnull
    @Override
    protected IItemHandler createHandler() {

        return new ItemStackHandler(1) {
            @Override public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                if (ModList.get().isLoaded("thermal_expansion")) {
                    return stack.getItem() == ForgeRegistries.ITEMS.getValue(new ResourceLocation("thermal", "sulfur_dust"));
                }
                return false;
            }
        };
    }
}
