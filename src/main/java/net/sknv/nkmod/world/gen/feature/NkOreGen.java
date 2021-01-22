package net.sknv.nkmod.world.gen.feature;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.DepthAverageConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sknv.nkmod.Naschkatze;

@Mod.EventBusSubscriber(modid = "nkmod")
public class NkOreGen {
    public static ConfiguredFeature<?, ?> URANINITE_ORE_SURFACE;
    public static ConfiguredFeature<?, ?> URANINITE_ORE_DEEP;

    private NkOreGen() {}

    public static void setup() {
        // todo: statistical analysis of ore frequency to adjust overall abundance
        // Surface ore layer
        URANINITE_ORE_SURFACE = register("uraninite_ore", Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, Naschkatze.URANINITE_ORE.get().getDefaultState(), 6))
                // TopSolidRangeConfig is explained in comments in the helper method NkOreGen::topRange
                // DepthAverageConfig is exactly what you'd expect from the param names
                // .withPlacement(Placement.DEPTH_AVERAGE.configure(new DepthAverageConfig(15, 5)))
                .withPlacement(Placement.RANGE.configure(topRange(48, 60)))
                .square() // Vein shape

                // Ore frequency in chunk.
                // Vanilla iron is 20, TExp copper is 6, TExp silver is 2.
                // Considering earth crust abundances are 60-70ppm for copper, 0.07-0.08ppm for silver, and 2-4ppm for uranium,
                // Drilled out a couple of chunks chunk, found 30-40 copper, <=10 silver.
                .func_242731_b(1));

        // Deep ore layer
        URANINITE_ORE_DEEP = register("uraninite_ore", Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, Naschkatze.URANINITE_ORE.get().getDefaultState(), 18))
                .withPlacement(Placement.DEPTH_AVERAGE.configure(new DepthAverageConfig(15, 5)))
                .square()
                .func_242731_b(1));
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void addOreGen(BiomeLoadingEvent event) {
        BiomeGenerationSettingsBuilder builder = event.getGeneration();
        if (isOverworldBiome(event.getCategory())) {
            builder.withFeature(Decoration.UNDERGROUND_ORES, URANINITE_ORE_SURFACE);
            builder.withFeature(Decoration.UNDERGROUND_ORES, URANINITE_ORE_DEEP);
        }
    }

    private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> register(String name, ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, Naschkatze.MODID + ":" + name, configuredFeature);
    }

    // thanks thermal expansion :)
    public static boolean isOverworldBiome(Category category) {
        return category != Category.NONE && category != Category.THEEND && category != Category.NETHER;
    }

    /**
     * Helper method to generate an ore config between two levels.
     * @param min Minimum ore level.
     * @param max Maximum ore level.
     * @return A {@link TopSolidRangeConfig} for the respective levels.
     */
    private static TopSolidRangeConfig topRange(int min, int max) {
        // Explanation for the TopSolidRangeConfig
        // bottomOffset -> minimum height for the ore
        // maximum -> minHeight + maximum = top level
        // topOffset -> subtracted from the maximum to give actual top level
        // ore effectively exists from bottomOffset to bottomOffset + maximum - topOffset
        return new TopSolidRangeConfig(min, 0, max-min);
    }
}
