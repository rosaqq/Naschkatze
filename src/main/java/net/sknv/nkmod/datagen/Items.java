package net.sknv.nkmod.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.sknv.nkmod.Naschkatze;
import net.sknv.nkmod.RegistrateHandler;

public class Items extends ItemModelProvider {

    public Items(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Naschkatze.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        singleTexture(RegistrateHandler.CRUSHED_URANINITE.get().getRegistryName().getPath(), new ResourceLocation("item/handheld"), "layer0", new ResourceLocation(Naschkatze.MODID, "item/crushed_uraninite"));
        singleTexture(RegistrateHandler.YELLOWCAKE.get().getRegistryName().getPath(), new ResourceLocation("item/handheld"), "layer0", new ResourceLocation(Naschkatze.MODID, "item/yellowcake"));
        singleTexture(RegistrateHandler.URANIUM_INGOT.get().getRegistryName().getPath(), new ResourceLocation("item/handheld"), "layer0", new ResourceLocation(Naschkatze.MODID, "item/uranium_ingot"));
        singleTexture(RegistrateHandler.UO2_INGOT.get().getRegistryName().getPath(), new ResourceLocation("item/handheld"), "layer0", new ResourceLocation(Naschkatze.MODID, "item/uo2_ingot"));
        singleTexture(RegistrateHandler.GRAPHITE_INGOT.get().getRegistryName().getPath(), new ResourceLocation("item/handheld"), "layer0", new ResourceLocation(Naschkatze.MODID, "item/graphite_ingot"));

        withExistingParent(RegistrateHandler.URANINITE_ORE.get().asItem().getRegistryName().getPath(), new ResourceLocation(Naschkatze.MODID, "block/uraninite_ore"));
        withExistingParent(RegistrateHandler.GRINDER.get().asItem().getRegistryName().getPath(), new ResourceLocation(Naschkatze.MODID, "block/grinder"));
    }
}
