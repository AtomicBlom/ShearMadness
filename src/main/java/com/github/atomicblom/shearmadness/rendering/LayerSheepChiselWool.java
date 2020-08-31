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
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.inventory.container.PlayerContainer;
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
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, SheepEntity sheep, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (sheep.getSheared()) return;
        Minecraft minecraft = Minecraft.getInstance();

        boolean flag = sheepRenderer.isVisible(sheep);
        boolean flag1 = !flag && !sheep.isInvisibleToPlayer(minecraft.player);
        boolean flag2 = minecraft.func_238206_b_(sheep);
        RenderType rendertype = sheepRenderer.func_230496_a_(sheep, flag, flag1, flag2);
        if (rendertype == null) rendertype = RenderType.func_239268_f_(SHEEP_WOOL_TEXTURE);

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
                    sheepRenderer.getRenderManager().textureManager.bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);

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
                        blue = (colorMultiplier & 255) / 255.0F; //Blue
                    }

                    this.getEntityModel().copyModelAttributesTo(this.sheepModel);
                    sheepModel.setLivingAnimations(sheep, limbSwing, limbSwingAmount, partialTicks);

                    IVertexBuilder vertexBuilder = bufferIn.getBuffer(rendertype);
                    sheepModel.render(matrixStackIn, vertexBuilder, packedLightIn, OverlayTexture.NO_OVERLAY, red, green, blue, alpha);
                } catch (final Exception exception) {
                    badModels.add(itemIdentifier);
                    modelCache.put(itemIdentifier, errorModelMaker.createModel(null, sheep));
                    ShearMadnessMod.LOGGER.warn("Error rendering chiselled sheep with item {} - {}", itemStack, exception);
                }
            } else {
                sheepModel = defaultBody;
                sheepRenderer.getRenderManager().textureManager.bindTexture(SHEEP_WOOL_TEXTURE);

                this.getEntityModel().copyModelAttributesTo(this.sheepModel);
                sheepModel.setLivingAnimations(sheep, limbSwing, limbSwingAmount, partialTicks);
                IVertexBuilder vertexBuilder = bufferIn.getBuffer(rendertype);
                sheepModel.render(matrixStackIn, vertexBuilder, packedLightIn, OverlayTexture.NO_OVERLAY, red, green, blue, alpha);
            }
        }
    }
}
