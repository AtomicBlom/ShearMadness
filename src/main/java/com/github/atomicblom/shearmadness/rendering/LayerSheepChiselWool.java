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
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.QuadrupedModel;
import net.minecraft.client.renderer.entity.model.SheepModel;
import net.minecraft.client.renderer.entity.model.SheepWoolModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.SpriteMap;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import static com.github.atomicblom.shearmadness.utility.Reference.Textures.SHEEP_WOOL_TEXTURE;

@OnlyIn(Dist.CLIENT)
@ParametersAreNonnullByDefault
public class LayerSheepChiselWool extends LayerRenderer<SheepEntity, SheepModel<SheepEntity>> {
    private final RenderChiselSheep sheepRenderer;
    private final Cache<Integer, SheepWoolProperties> modelCache = CacheBuilder.newBuilder()
            .expireAfterAccess(5, TimeUnit.MINUTES)
            .build();
    private final HashSet<Integer> badModels = Sets.newHashSet();

    private final QuadrupedModel<SheepEntity> defaultBody;
    private final IModelMaker errorModelMaker = new ErrorModelMaker();

    public LayerSheepChiselWool(RenderChiselSheep renderChiselSheep) {
        super(renderChiselSheep);
        sheepRenderer = renderChiselSheep;
        defaultBody = new SheepWoolModel<>();
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, SheepEntity sheep, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (sheep.getSheared()) return;
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) return;

        if (!sheep.isInvisible()) {
            final LazyOptional<IChiseledSheepCapability> possibleCapability = sheep.getCapability(Capability.CHISELED_SHEEP);

            final float[] afloat = SheepEntity.getDyeRgb(sheep.getFleeceColor());
            float red = afloat[0];
            float green = afloat[1];
            float blue = afloat[2];
            float alpha = 1;

            IChiseledSheepCapability capability = possibleCapability.orElse(new ChiseledSheepCapability());
            if (capability.isChiseled()) {
                final int itemIdentifier = capability.getItemIdentifier();
                final ItemStack itemStack = capability.getChiselItemStack();

                final ModelLoader modelLoader = ModelLoader.instance();
                assert modelLoader != null;
                final SpriteMap spriteMap = modelLoader.getSpriteMap();

                try {
                    if (Settings.debugModels()) {
                        modelCache.invalidate(itemIdentifier);
                    }

                    SheepWoolProperties sheepWoolProperties = modelCache.get(itemIdentifier, () ->
                    {
                        try {
                            if (badModels.contains(itemIdentifier)) {
                                return getErrorModel(sheep, spriteMap);
                            }

                            final IModelMaker variationModelMaker = VariationRegistry.INSTANCE.getVariationModelMaker(itemStack);
                            return new SheepWoolProperties(
                                    variationModelMaker.createModel(itemStack, sheep, spriteMap),
                                    variationModelMaker.shouldRenderWool(itemStack)
                            );
                        } catch (Exception e) {
                            ShearMadnessMod.LOGGER.error("Error creating chiseled sheep model, item stack was {} - {}", itemStack, e);
                            return getErrorModel(sheep, spriteMap);
                        }
                    });

                    if (sheepWoolProperties.shouldRenderNormalWool()) {
                        renderNormalWool(matrixStackIn, bufferIn, packedLightIn, sheep, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, red, green, blue, alpha);
                    }

                    RenderType rendertype = RenderType.getEntityTranslucentCull(PlayerContainer.LOCATION_BLOCKS_TEXTURE);

                    final Item item = itemStack.getItem();
                    if (item instanceof BlockItem) {
                        final int colorMultiplier = Minecraft.getInstance()
                                .getBlockColors()
                                .getColor(
                                        ((BlockItem) item).getBlock().getDefaultState(),
                                        sheep.getEntityWorld(),
                                        sheep.getPosition(),
                                        0);
                        red = (colorMultiplier >> 16 & 255) / 255.0F;
                        green = (colorMultiplier >> 8 & 255) / 255.0F;
                        blue = (colorMultiplier & 255) / 255.0F;
                    }

                    QuadrupedModel<SheepEntity> sheepModel = sheepWoolProperties.getWoolModel();

                    this.getEntityModel().copyModelAttributesTo(sheepModel);
                    sheepModel.setLivingAnimations(sheep, limbSwing, limbSwingAmount, partialTicks);
                    sheepModel.setRotationAngles(sheep, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

                    IVertexBuilder vertexBuilder = bufferIn.getBuffer(rendertype);

                    sheepModel.render(matrixStackIn, vertexBuilder, packedLightIn, OverlayTexture.NO_OVERLAY, red, green, blue, alpha);
                } catch (final Exception exception) {
                    badModels.add(itemIdentifier);
                    modelCache.put(itemIdentifier, getErrorModel(sheep, spriteMap));
                    ShearMadnessMod.LOGGER.warn("Error rendering chiselled sheep with item {} - {}", itemStack, exception);
                }
            } else {
                renderNormalWool(matrixStackIn, bufferIn, packedLightIn, sheep, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, red, green, blue, alpha);
            }
        }
    }

    private void renderNormalWool(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, SheepEntity sheep, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float red, float green, float blue, float alpha) {
        RenderType rendertype = RenderType.func_239268_f_(SHEEP_WOOL_TEXTURE);
        this.getEntityModel().copyModelAttributesTo(this.defaultBody);
        defaultBody.setLivingAnimations(sheep, limbSwing, limbSwingAmount, partialTicks);
        defaultBody.setRotationAngles(sheep, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        IVertexBuilder vertexBuilder = bufferIn.getBuffer(rendertype);
        defaultBody.render(matrixStackIn, vertexBuilder, packedLightIn, OverlayTexture.NO_OVERLAY, red, green, blue, alpha);
    }

    private SheepWoolProperties getErrorModel(SheepEntity sheep, SpriteMap spriteMap) {
        return new SheepWoolProperties(
                errorModelMaker.createModel(null, sheep, spriteMap),
                true);
    }
}
