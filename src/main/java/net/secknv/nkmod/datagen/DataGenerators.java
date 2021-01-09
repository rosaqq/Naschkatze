package net.secknv.nkmod.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.secknv.nkmod.Naschkatze;
import org.apache.logging.log4j.LogManager;

@Mod.EventBusSubscriber(modid = Naschkatze.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class DataGenerators {

    private DataGenerators() {}

    // todo: seems to be not working atm
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        LogManager.getLogger().error("I am getting called");

        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        generator.addProvider(new BlockStates(generator, existingFileHelper));
    }
}
