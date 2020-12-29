package net.secknv.nkmod;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Naschkatze.MODID)
public class Naschkatze {


    public static final String MODID = "nkmod";
    private static final Logger LOGGER = LogManager.getLogger();

    public static final ItemGroup TAB = new ItemGroup("tabNk") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(RegistryHandler.URANIUM_INGOT.get());
        }
    };

    public Naschkatze() {

        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init);
        MinecraftForge.EVENT_BUS.register(this);

        RegistryHandler.init();
    }
}
