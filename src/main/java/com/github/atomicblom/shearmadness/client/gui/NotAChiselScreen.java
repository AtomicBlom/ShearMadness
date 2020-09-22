package com.github.atomicblom.shearmadness.client.gui;

import com.github.atomicblom.shearmadness.inventory.container.NotAChiselContainer;
import com.github.atomicblom.shearmadness.utility.Reference;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class NotAChiselScreen extends ContainerScreen<NotAChiselContainer> {
    public NotAChiselScreen(NotAChiselContainer container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();
        playerInventoryTitleY = 40;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        assert minecraft != null;

        minecraft.textureManager.bindTexture(Reference.Textures.NOT_A_CHISEL_GUI_BACKGROUND);
        int i = this.guiLeft;
        int j = this.guiTop;
        this.blit(matrixStack, i, j, 0, 0, this.xSize, this.ySize);
    }
}
