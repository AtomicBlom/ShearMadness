package com.github.atomicblom.shearmadness.rendering;

import com.github.atomicblom.shearmadness.ShearMadnessMod;
import com.github.atomicblom.shearmadness.api.Capability;
import com.github.atomicblom.shearmadness.api.VariationRegistry;
import com.github.atomicblom.shearmadness.api.capability.IChiseledSheepCapability;
import com.github.atomicblom.shearmadness.api.modelmaker.IModelMaker;
import com.github.atomicblom.shearmadness.capability.ChiseledSheepCapability;
import com.github.atomicblom.shearmadness.configuration.Settings;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Sets;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.QuadrupedModel;
import net.minecraft.client.renderer.entity.model.SheepModel;
import net.minecraft.client.renderer.entity.model.SheepWoolModel;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import static com.github.atomicblom.shearmadness.utility.Reference.Textures.SHEEP_WOOL_TEXTURE;

@OnlyIn(Dist.CLIENT)
@ParametersAreNonnullByDefault
public class LayerSheepChiselWool extends LayerRenderer<SheepEntity, SheepModel<SheepEntity>> {
    private final RenderChiselSheep sheepRenderer;
    private final Cache<Integer, QuadrupedModel<SheepEntity>> modelCache = CacheBuilder.newBuilder()
            .expireAfterAccess(5, TimeUnit.MINUTES)
            .build();
    private final HashSet<Integer> badModels = Sets.newHashSet();

    private final QuadrupedModel<SheepEntity> defaultBody;
    private final IModelMaker errorModelMaker = new ErrorModelMaker();
    private QuadrupedModel<SheepEntity> sheepModel;

    public LayerSheepChiselWool(RenderChiselSheep renderChiselSheep) {
        super(renderChiselSheep);
        sheepRenderer = renderChiselSheep;
        defaultBody = new SheepWoolModel<>();
        sheepModel = defaultBody;
    }

    @Override
    public void render(SheepEntity sheep, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (!sheep.getSheared() && !sheep.isInvisible()) {
            final LazyOptional<IChiseledSheepCapability> possibleCapability = sheep.getCapability(Capability.CHISELED_SHEEP);

            IChiseledSheepCapability capability = possibleCapability.orElse(new ChiseledSheepCapability());
            if (capability.isChiseled()) {
                final int itemIdentifier = capability.getItemIdentifier();
                final ItemStack itemStack = capability.getChiselItemStack();
                try {
                    if (Settings.debugModels()) {
                        modelCache.invalidate(itemIdentifier);
                    }

                    sheepModel = modelCache.get(itemIdentifier, () ->
                    {
                        try {
                            if (badModels.contains(itemIdentifier)) {
                                return errorModelMaker.createModel(null, sheep);
                            }

                            final IModelMaker variationModelMaker = VariationRegistry.INSTANCE.getVariationModelMaker(itemStack);
                            return variationModelMaker.createModel(itemStack, sheep);
                        } catch (Exception e) {
                            ShearMadnessMod.LOGGER.error("Error creating chiseled sheep model, item stack was {} - {}", itemStack, e);
                            return errorModelMaker.createModel(null, sheep);
                        }
                    });
                    Minecraft.getInstance().getTextureMap().bindTexture();

                    final Item item = itemStack.getItem();
                    if (item instanceof BlockItem) {
                        final int colorMultiplier = Minecraft.getInstance()
                                .getBlockColors()
                                .getColor(
                                        ((BlockItem) item).getBlock().getDefaultState(),
                                        sheep.getEntityWorld(),
                                        sheep.getPosition(),
                                        0);
                        GlStateManager.color3f(
                                (colorMultiplier >> 16 & 255) / 255.0F, //Red
                                (colorMultiplier >> 8 & 255) / 255.0F, //Green
                                (colorMultiplier & 255) / 255.0F); //Blue
                    }

                    renderModel(sheep, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
                } catch (final Exception exception) {
                    badModels.add(itemIdentifier);
                    modelCache.put(itemIdentifier, errorModelMaker.createModel(null, sheep));
                    ShearMadnessMod.LOGGER.warn("Error rendering chiselled sheep with item {} - {}", itemStack, exception);
                }
            } else {
                sheepModel = defaultBody;
                sheepRenderer.bindTexture(SHEEP_WOOL_TEXTURE);

                final float[] afloat = SheepEntity.getDyeRgb(sheep.getFleeceColor());
                GlStateManager.color3f(afloat[0], afloat[1], afloat[2]);

                renderModel(sheep, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
            }
        }
    }

    public void renderModel(SheepEntity sheep, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.getEntityModel().setModelAttributes(this.sheepModel);
        sheepModel.setLivingAnimations(sheep, limbSwing, limbSwingAmount, partialTicks);
        sheepModel.render(sheep, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    }

    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
}
