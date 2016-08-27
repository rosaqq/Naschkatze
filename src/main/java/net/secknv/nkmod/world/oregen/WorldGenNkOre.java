package net.secknv.nkmod.world.oregen;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

/**
 * Created by secknv on 23/08/2016.
 */

public class WorldGenNkOre implements IWorldGenerator{

    private WorldGenMinable oreGen;
    private int veinsPerChunk;
    private int minHeight;
    private int maxHeight;

    public WorldGenNkOre(Block block, int orePerVein, int veinsPerChunk, int minHeight, int maxHeight) {

        this.oreGen = new WorldGenMinable(block.getDefaultState(), orePerVein);
        this.veinsPerChunk = veinsPerChunk;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
    }

    private void runGenerator(WorldGenerator generator, World world, Random rand,
                              int chunk_x, int chunk_z, int veinsPerChunk, int minHeight, int maxHeight) {


        if(minHeight<0||maxHeight>256||minHeight>maxHeight) {

            throw new IllegalArgumentException("Illegal Height Arguments");
        }

        int heightDiff = (maxHeight - minHeight + 1);

        for (int i=0; i<veinsPerChunk; i++) {

            int x = chunk_x * 16 + rand.nextInt(16);
            int y = minHeight + rand.nextInt(heightDiff);
            int z = chunk_z * 16 + rand.nextInt(16);
            BlockPos tempPos = new BlockPos(x, y, z);
            generator.generate(world, rand, tempPos);
        }
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {

        switch (world.provider.getDimension()) {

            case -1:
                break;
            case 0:
                runGenerator(this.oreGen, world, random, chunkX, chunkZ, veinsPerChunk, minHeight, maxHeight);
                break;
            case 1:
                break;
        }
    }
}
