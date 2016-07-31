package net.secknv.nkmod.world.oregen;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.secknv.nkmod.block.NkBlocks;

public class CopperWorldGen implements IWorldGenerator{
	
	private WorldGenerator gen_copper_ore;
	
	public CopperWorldGen()
	{
		gen_copper_ore = new WorldGenMinable(NkBlocks.COPPER_ORE.getDefaultState(), 12);
	}
	
	private void runGenerator(WorldGenerator generator, World world, Random rand,
			int chunk_x, int chunk_z, int veinsPerChunk, int minHeight, int maxHeight)
	{
		
		if(minHeight<0||maxHeight>256||minHeight>maxHeight)
		{
			throw new IllegalArgumentException("Illegal Height Arguments");
		}
		
		int heightDiff = (maxHeight - minHeight + 1);
		for (int i=0; i<veinsPerChunk; i++){
			int x = chunk_x * 16 + rand.nextInt(16);
			int y = minHeight + rand.nextInt(heightDiff);
			int z = chunk_z * 16 + rand.nextInt(16);
			BlockPos tempPos = new BlockPos(x, y, z);
			generator.generate(world, rand, tempPos);
		}
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		switch (world.provider.getDimension())
		{
			case -1:
				break;
			case 0:
				runGenerator(gen_copper_ore, world, random, chunkX, chunkZ, 14, 0, 80);
				break;
			case 1:
				break;
		}
		
	}
}
