package net.sknv.nkmod.blocks.machines.base;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.sknv.nkmod.Naschkatze;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class MachineScreenFactory<C extends Container> implements ScreenManager.IScreenFactory<C, ContainerScreen<C>> {

    private final String textureLocation;

    public MachineScreenFactory(String textureLocation) {
        this.textureLocation = textureLocation;
    }

    @Nonnull
    @Override
    @ParametersAreNonnullByDefault
    public ContainerScreen<C> create(C p_create_1_, PlayerInventory p_create_2_, ITextComponent p_create_3_) {
        return new ContainerScreen<C>(p_create_1_, p_create_2_, p_create_3_) {

            private final ResourceLocation GUI = new ResourceLocation(Naschkatze.MODID, textureLocation);

            @Override
            public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
                this.renderBackground(matrixStack);
                super.render(matrixStack, mouseX, mouseY, partialTicks);
                this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
            }

            @Override
            protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
            }

            @Override
            protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
                RenderSystem.color4f(1f, 1f, 1f, 1f);
                this.minecraft.getTextureManager().bindTexture(GUI);
                int relX = (this.width - this.xSize) / 2;
                int relY = (this.height - this.ySize) / 2;
                this.blit(matrixStack, relX, relY, 0, 0, this.xSize, this.ySize);
            }
        };
    }
}
