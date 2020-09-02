package com.github.atomicblom.shearmadness.rendering;

import net.minecraft.client.renderer.entity.model.QuadrupedModel;
import net.minecraft.entity.passive.SheepEntity;

public class SheepWoolProperties {
    private final QuadrupedModel<SheepEntity> woolModel;
    private final boolean renderNormalWool;

    public SheepWoolProperties(QuadrupedModel<SheepEntity> woolModel, boolean renderNormalWool) {

        this.woolModel = woolModel;
        this.renderNormalWool = renderNormalWool;
    }

    public boolean shouldRenderNormalWool() {
        return renderNormalWool;
    }

    public QuadrupedModel<SheepEntity> getWoolModel() {
        return woolModel;
    }
}
