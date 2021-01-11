package net.secknv.nkmod.datagen;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.data.DataGenerator;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.secknv.nkmod.Naschkatze;
import net.secknv.nkmod.RegistryHandler;

import java.util.function.Function;

public class BlockStates extends BlockStateProvider {
    public BlockStates(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Naschkatze.MODID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(RegistryHandler.URANINITE_ORE.get());
        registerGrinder();
    }


    private void registerGrinder() {
        ResourceLocation[] sides = getSidesArray("grinder_bot", "grinder_top", "grinder_front", "grinder_back", "grinder_sides", "grinder_sides");
        BlockModelBuilder modelGrinder = models().cube("grinder", sides[0], sides[1], sides[2], sides[3], sides[4], sides[5]);
        orientedBlock(RegistryHandler.GRINDER.get(), state -> modelGrinder);

        // Powered block example
        /*
        BlockModelBuilder modelGrinderPowered = models().cube("grinder_powered", txt, txt, new ResourceLocation(Naschkatze.MODID, "block/firstblock_powered"), txt, txt, txt);
        orientedBlock(RegistryHandler.GRINDER.get(), state -> {
            if (state.get(BlockStateProperties.POWERED)) {
                return modelGrinder;
            } else {
                return modelGrinderPowered;
            }
        });*/
    }

    // Directional block because vanilla one has directions swapped
    private void orientedBlock(Block block, Function<BlockState, ModelFile> modelFunc) {
        getVariantBuilder(block)
                .forAllStates(state -> {
                    Direction dir = state.get(BlockStateProperties.HORIZONTAL_FACING);
                    return ConfiguredModel.builder()
                            .modelFile(modelFunc.apply(state))
                            .rotationX(dir.getAxis() == Direction.Axis.Y ?  dir.getAxisDirection().getOffset() * -90 : 0)
                            .rotationY(dir.getAxis() != Direction.Axis.Y ? ((dir.getHorizontalIndex() + 2) % 4) * 90 : 0)
                            .build();
                });
    }

    private ResourceLocation[] getSidesArray(String bot, String top, String front, String back, String left, String right) {
        return new ResourceLocation[]{
                new ResourceLocation(Naschkatze.MODID, "block/" + bot),
                new ResourceLocation(Naschkatze.MODID, "block/" + top),
                new ResourceLocation(Naschkatze.MODID, "block/" + front),
                new ResourceLocation(Naschkatze.MODID, "block/" + back),
                new ResourceLocation(Naschkatze.MODID, "block/" + left),
                new ResourceLocation(Naschkatze.MODID, "block/" + right),
        };
    }

}
