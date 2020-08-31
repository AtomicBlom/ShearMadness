package com.github.atomicblom.shearmadness.rendering;

import com.github.atomicblom.shearmadness.utility.Reference;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.SheepModel;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@OnlyIn(Dist.CLIENT)
@ParametersAreNonnullByDefault
public class RenderChiselSheep extends MobRenderer<SheepEntity, SheepModel<SheepEntity>> {
    public RenderChiselSheep(EntityRendererManager renderManager) {
        super(renderManager, new SheepModel<>(), 0.7F);
        addLayer(new LayerSheepChiselWool(this));
    }

    @Nullable
    @Override
    public ResourceLocation getEntityTexture(SheepEntity entity) {
        return Reference.Textures.SHEARED_SHEEP_TEXTURE;
    }
}
