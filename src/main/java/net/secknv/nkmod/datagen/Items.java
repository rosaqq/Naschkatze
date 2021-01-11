package net.secknv.nkmod.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.secknv.nkmod.Naschkatze;
import net.secknv.nkmod.RegistryHandler;

public class Items extends ItemModelProvider {

    public Items(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Naschkatze.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        singleTexture(RegistryHandler.CRUSHED_URANINITE.get().getRegistryName().getPath(), new ResourceLocation("item/handheld"), "layer0", new ResourceLocation(Naschkatze.MODID, "item/crushed_uraninite"));
        singleTexture(RegistryHandler.YELLOWCAKE.get().getRegistryName().getPath(), new ResourceLocation("item/handheld"), "layer0", new ResourceLocation(Naschkatze.MODID, "item/yellowcake"));
        singleTexture(RegistryHandler.URANIUM_INGOT.get().getRegistryName().getPath(), new ResourceLocation("item/handheld"), "layer0", new ResourceLocation(Naschkatze.MODID, "item/uranium_ingot"));
        singleTexture(RegistryHandler.UO2_INGOT.get().getRegistryName().getPath(), new ResourceLocation("item/handheld"), "layer0", new ResourceLocation(Naschkatze.MODID, "item/uo2_ingot"));
        singleTexture(RegistryHandler.GRAPHITE_INGOT.get().getRegistryName().getPath(), new ResourceLocation("item/handheld"), "layer0", new ResourceLocation(Naschkatze.MODID, "item/graphite_ingot"));

        withExistingParent(RegistryHandler.URANINITE_ORE_ITEM.get().getRegistryName().getPath(), new ResourceLocation(Naschkatze.MODID, "block/uraninite_ore"));
        withExistingParent(RegistryHandler.GRINDER_ITEM.get().getRegistryName().getPath(), new ResourceLocation(Naschkatze.MODID, "block/grinder"));
    }
}
