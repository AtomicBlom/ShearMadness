package com.github.atomicblom.shearmadness.client.gui;

import com.github.atomicblom.shearmadness.inventory.container.NotAChiselContainer;
import com.github.atomicblom.shearmadness.utility.Reference;
import com.mojang.blaze3d.platform.GlStateManager;
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
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(Reference.Textures.NOT_A_CHISEL_GUI_BACKGROUND);
        int i = this.guiLeft;
        int j = this.guiTop;
        this.blit(i, j, 0, 0, this.xSize, this.ySize);
    }


}
