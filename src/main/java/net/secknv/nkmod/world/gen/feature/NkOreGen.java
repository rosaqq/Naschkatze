package net.secknv.nkmod.world.gen.feature;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placement.DepthAverageConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.secknv.nkmod.Naschkatze;
import net.secknv.nkmod.RegistryHandler;

@Mod.EventBusSubscriber(modid = "nkmod")
public class NkOreGen {
    public static ConfiguredFeature<?, ?> URANINITE_ORE;

    private NkOreGen() {}

    public static void setup() {
        URANINITE_ORE = register("uraninite_ore", Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, RegistryHandler.URANINITE_ORE.get().getDefaultState(), 18))
                // Explanation for the TopSolidRangeConfig
                // bottomOffset -> minimum height for the ore
                // maximum -> minHeight + maximum = top level
                // topOffset -> subtracted from the maximum to give actual top level
                // ore effectively exists from bottomOffset to bottomOffset + maximum - topOffset
                //.withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(20, 0, 40)))

                // DepthAverageConfig is exactly what you'd expect from the param names
                .withPlacement(Placement.DEPTH_AVERAGE.configure(new DepthAverageConfig(30, 10)))
                .square() // Vein shape

                // Ore frequency in chunk.
                // Vanilla iron is 20, TExp copper is 6, TExp silver is 2.
                // Considering earth crust abundances are 60-70ppm for copper, 0.07-0.08ppm for silver, and 2-4ppm for uranium,
                // Drilled out a couple of chunks chunk, found 30-40 copper, <=10 silver. Going with 18 for vein size and 1 for chunk frequency!
                .func_242731_b(1));
    }

    private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> register(String name, ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, Naschkatze.MODID + ":" + name, configuredFeature);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void addOreGen(BiomeLoadingEvent event) {
        BiomeGenerationSettingsBuilder builder = event.getGeneration();
        if (isOverworldBiome(event.getCategory())) {
            builder.withFeature(Decoration.UNDERGROUND_ORES, URANINITE_ORE);
        }
    }

    // thanks thermal expansion :)
    public static boolean isOverworldBiome(Category category) {
        return category != Category.NONE && category != Category.THEEND && category != Category.NETHER;
    }
}
