package com.github.atomicblom.chiselsheep.client.renderer.entity.layers;

import com.github.atomicblom.chiselsheep.client.renderer.entity.ModelBox2;
import com.github.atomicblom.chiselsheep.client.renderer.entity.RenderChiselSheep;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelSheep1;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import java.util.UUID;

@SideOnly(Side.CLIENT)
public class LayerSheepChiselWool implements LayerRenderer<EntitySheep>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/pig/pig.png");
    private final RenderChiselSheep sheepRenderer;
    private final ModelSheep1 sheepModel;

    public LayerSheepChiselWool(RenderChiselSheep sheepRendererIn)
    {
        this.sheepRenderer = sheepRendererIn;
        sheepModel  = new ModelSheep1();
        sheepModel.body = new ModelRenderer(sheepModel, 28, 8);
        sheepModel.body.setRotationPoint(0.0F, 5.0F, 2.0F);
        sheepModel.body.cubeList.add(new ModelBox2(sheepModel.body));
    }

    public void doRenderLayer(EntitySheep entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        if (!entitylivingbaseIn.getSheared() && !entitylivingbaseIn.isInvisible())
        {
            NetHandlerPlayClient handler = (NetHandlerPlayClient)FMLClientHandler.instance().getClientPlayHandler();

            //final ResourceLocation atomicBlom = handler.getPlayerInfo(UUID.fromString("58d506e2-7ee7-4774-8b22-c7a57eda488b")).getLocationSkin();
            this.sheepRenderer.bindTexture(new ResourceLocation("textures/entity/steve.png"));

            if (entitylivingbaseIn.hasCustomName() && "jeb_".equals(entitylivingbaseIn.getCustomNameTag()))
            {
                int i1 = 25;
                int i = entitylivingbaseIn.ticksExisted / 25 + entitylivingbaseIn.getEntityId();
                int j = EnumDyeColor.values().length;
                int k = i % j;
                int l = (i + 1) % j;
                float f = ((float)(entitylivingbaseIn.ticksExisted % 25) + partialTicks) / 25.0F;
                float[] afloat1 = EntitySheep.getDyeRgb(EnumDyeColor.byMetadata(k));
                float[] afloat2 = EntitySheep.getDyeRgb(EnumDyeColor.byMetadata(l));
                GlStateManager.color(afloat1[0] * (1.0F - f) + afloat2[0] * f, afloat1[1] * (1.0F - f) + afloat2[1] * f, afloat1[2] * (1.0F - f) + afloat2[2] * f);
            }
            else
            {
                float[] afloat = EntitySheep.getDyeRgb(entitylivingbaseIn.getFleeceColor());
                GlStateManager.color(afloat[0], afloat[1], afloat[2]);
            }

            this.sheepModel.setModelAttributes(this.sheepRenderer.getMainModel());
            this.sheepModel.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
            this.sheepModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    public boolean shouldCombineTextures()
    {
        return true;
    }
}