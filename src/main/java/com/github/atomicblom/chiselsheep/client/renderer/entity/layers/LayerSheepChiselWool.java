package com.github.atomicblom.chiselsheep.client.renderer.entity.layers;

import com.github.atomicblom.chiselsheep.ChiselSheepMod;
import com.github.atomicblom.chiselsheep.capability.IChiseledSheepCapability;
import com.github.atomicblom.chiselsheep.client.renderer.entity.ModelBox2;
import com.github.atomicblom.chiselsheep.client.renderer.entity.RenderChiselSheep;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelSheep1;
import net.minecraft.client.model.TextureOffset;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.layers.LayerSheepWool;
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
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/cow/cow.png");
    private final RenderChiselSheep sheepRenderer;
    private final ModelSheep1 sheepModel;

    private ModelBox bodyBox;

    public LayerSheepChiselWool(RenderChiselSheep sheepRendererIn)
    {
        this.sheepRenderer = sheepRendererIn;
        sheepModel  = new ModelSheep1();
    }

    public void doRenderLayer(EntitySheep entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        if (!entitylivingbaseIn.getSheared() && !entitylivingbaseIn.isInvisible())
        {
            IChiseledSheepCapability capability = entitylivingbaseIn.getCapability(ChiselSheepMod.CHISELED_SHEEP_CAPABILITY, null);
            if (capability.isChiseled()) {
                if (!(bodyBox instanceof ModelBox2)) {
                    sheepModel.body = new ModelRenderer(sheepModel, 28, 8);
                    sheepModel.body.setRotationPoint(0.0F, 5.0F, 2.0F);
                    bodyBox = new ModelBox2(sheepModel.body);
                    sheepModel.body.cubeList.add(bodyBox);
                }
                this.sheepRenderer.bindTexture(new ResourceLocation("textures/entity/steve.png"));
            } else {
                if ((bodyBox instanceof ModelBox2)) {
                    sheepModel.body = new ModelRenderer(sheepModel, 28, 8);
                    sheepModel.body.addBox(-4.0F, -10.0F, -7.0F, 8, 16, 6, 1.75F);
                    bodyBox = new ModelBox(sheepModel.body, 28, 8, -4.0F, -10.0F, -7.0F, 8, 16, 6, 1.75F);
                    sheepModel.body.setRotationPoint(0.0F, 5.0F, 2.0F);
                    sheepModel.body.cubeList.add(bodyBox);
                }
                this.sheepRenderer.bindTexture(TEXTURE);
            }

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