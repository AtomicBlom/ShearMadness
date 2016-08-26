package com.github.atomicblom.shearmadness.rendering;

import com.github.atomicblom.shearmadness.api.IModelMaker;
import com.github.atomicblom.shearmadness.api.VariationRegistry;
import com.github.atomicblom.shearmadness.capability.CapabilityProvider;
import com.github.atomicblom.shearmadness.capability.IChiseledSheepCapability;
import com.github.atomicblom.shearmadness.configuration.Settings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelQuadruped;
import net.minecraft.client.model.ModelSheep1;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("StaticNonFinalField")
@SideOnly(Side.CLIENT)
public class LayerSheepChiselWool implements LayerRenderer<EntitySheep>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/sheep/sheep_fur.png");

    private final RenderChiselSheep sheepRenderer;
    private final Map<Integer, ModelQuadruped> modelMap = new HashMap<>(16);
    private final ModelQuadruped defaultBody;
    private ModelQuadruped sheepModel;
    public LayerSheepChiselWool(RenderChiselSheep sheepRendererIn)
    {
        sheepRenderer = sheepRendererIn;
        defaultBody = new ModelSheep1();
        sheepModel = defaultBody;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void doRenderLayer(EntitySheep sheep, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        if (!sheep.getSheared() && !sheep.isInvisible())
        {
            final IChiseledSheepCapability capability = sheep.getCapability(CapabilityProvider.CHISELED_SHEEP, null);
            if (capability.isChiseled())
            {
                final ItemStack itemStack = capability.getChiselItemStack();

                ModelQuadruped quadrupedModel = modelMap.get(capability.getItemIdentifier());
                if (quadrupedModel == null)
                {
                    final IModelMaker variationModelMaker = VariationRegistry.INSTANCE.getVariationModelMaker(itemStack);
                    quadrupedModel = variationModelMaker.createModel(itemStack, sheep);
                    if (!Settings.debugModels())
                    {
                        modelMap.put(capability.getItemIdentifier(), quadrupedModel);
                    }
                }
                sheepModel = quadrupedModel;
                sheepRenderer.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

                final Item item = itemStack.getItem();
                if (item instanceof ItemBlock)
                {
                    final int colorMultiplier = Minecraft.getMinecraft().getBlockColors().colorMultiplier(((ItemBlock) item).block.getDefaultState(), sheep.worldObj, sheep.getPosition(), 0);
                    GlStateManager.color(
                            (colorMultiplier >> 16 & 255) / 255.0F, //Red
                            (colorMultiplier >> 8 & 255) / 255.0F, //Green
                            (colorMultiplier & 255) / 255.0F); //Blue
                }

            } else
            {
                sheepModel = defaultBody;
                sheepRenderer.bindTexture(TEXTURE);

                final float[] afloat = EntitySheep.getDyeRgb(sheep.getFleeceColor());
                GlStateManager.color(afloat[0], afloat[1], afloat[2]);
            }

            sheepModel.setModelAttributes(sheepRenderer.getMainModel());
            sheepModel.setLivingAnimations(sheep, limbSwing, limbSwingAmount, partialTicks);
            sheepModel.render(sheep, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    @Override
    public boolean shouldCombineTextures()
    {
        return true;
    }


}

