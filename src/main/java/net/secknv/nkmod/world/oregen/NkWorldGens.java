package net.secknv.nkmod.world.oregen;

import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.secknv.nkmod.block.NkBlocks;

/**
 * Created by secknv on 23/08/2016.
 */

public class NkWorldGens {

    public static void register() {

        registerWorldGen(new WorldGenNkOre(NkBlocks.URANIUM_ORE, 10, 6, 0, 60), 0);
    }

    public static void registerWorldGen(IWorldGenerator generator, int modGenerationWeight) {

        GameRegistry.registerWorldGenerator(generator, modGenerationWeight);
    }

}
