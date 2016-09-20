package com.github.atomicblom.shearmadness.gui.ui;

import com.github.atomicblom.shearmadness.gui.container.ConfigureBreedingContainer;
import com.github.atomicblom.shearmadness.utility.Reference;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class ConfigureBreedingGui extends GuiContainer {
    private static final ResourceLocation BREEDING_GUI_TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/gui/container/breeding.png");

    public ConfigureBreedingGui(InventoryPlayer inventorySlotsIn) {
        super(new ConfigureBreedingContainer(inventorySlotsIn));
        this.xSize = 308;
        this.ySize = 222;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(BREEDING_GUI_TEXTURE);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, this.width, this.height, 512, 512);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        guiLeft = (this.width - this.xSize) / 2 + 66;
        guiTop = (this.height - this.ySize) / 2 + 56;
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
