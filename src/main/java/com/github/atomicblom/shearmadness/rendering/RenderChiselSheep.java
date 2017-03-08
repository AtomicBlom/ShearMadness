package com.github.atomicblom.shearmadness.rendering;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.ParametersAreNonnullByDefault;

@SideOnly(Side.CLIENT)
@ParametersAreNonnullByDefault
public class RenderChiselSheep extends RenderLiving<EntitySheep>
{
    private static final ResourceLocation SHEARED_SHEEP_TEXTURES = new ResourceLocation("textures/entity/sheep/sheep.png");

    @SuppressWarnings("ThisEscapedInObjectConstruction")
    public RenderChiselSheep(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn)
    {
        super(renderManagerIn, modelBaseIn, shadowSizeIn);
        addLayer(new LayerSheepChiselWool(this));
    }

    /**
     * Returns the location of an rendering's texture. Doesn't seem to be called unless you call
     * Render.bindEntityTexture.
     */
    @Override
    protected ResourceLocation getEntityTexture(EntitySheep entity)
    {
        return SHEARED_SHEEP_TEXTURES;
    }
}