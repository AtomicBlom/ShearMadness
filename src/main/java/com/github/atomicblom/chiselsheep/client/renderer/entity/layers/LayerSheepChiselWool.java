package com.github.atomicblom.chiselsheep.client.renderer.entity.layers;

import com.github.atomicblom.chiselsheep.ChiselSheepMod;
import com.github.atomicblom.chiselsheep.capability.IChiseledSheepCapability;
import com.github.atomicblom.chiselsheep.client.renderer.entity.FakeWorld;
import com.github.atomicblom.chiselsheep.client.renderer.entity.ModelBox2;
import com.github.atomicblom.chiselsheep.client.renderer.entity.RenderChiselSheep;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelSheep1;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import team.chisel.api.block.ICarvable;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.ICarvingVariation;
import team.chisel.client.BlockFaceData;
import team.chisel.client.BlockFaceData.VariationFaceData;
import javax.vecmath.Vector4f;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class LayerSheepChiselWool implements LayerRenderer<EntitySheep>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/sheep/sheep_fur.png");
    private final RenderChiselSheep sheepRenderer;
    private final ModelSheep1 sheepModel;

    private final Map<ICarvingVariation, ModelRenderer> modelMap = new HashMap<>();
    final FakeWorld world = new FakeWorld();

    private final ModelRenderer defaultBody;

    public LayerSheepChiselWool(RenderChiselSheep sheepRendererIn)
    {
        sheepRenderer = sheepRendererIn;
        sheepModel  = new ModelSheep1();
        defaultBody = sheepModel.body;
    }

    public void doRenderLayer(EntitySheep entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        if (!entitylivingbaseIn.getSheared() && !entitylivingbaseIn.isInvisible())
        {
            IChiseledSheepCapability capability = entitylivingbaseIn.getCapability(ChiselSheepMod.CHISELED_SHEEP_CAPABILITY, null);
            if (capability.isChiseled()) {

                ItemStack itemStack = capability.getChiselItemStack();
                ICarvingVariation variation = CarvingUtils.getChiselRegistry().getVariation(itemStack);

                ModelRenderer bodyModelRenderer = modelMap.get(variation);
                if (bodyModelRenderer == null) {
                    if (variation.getBlock() instanceof ICarvable) {
                        bodyModelRenderer = getChiselBodyModelRenderer(variation);
                        //modelMap.put(variation, bodyModelRenderer);
                    } else {
                        sheepModel.body = defaultBody;
                        sheepRenderer.bindTexture(TEXTURE);
                    }
                }
                sheepModel.body = bodyModelRenderer;
                sheepRenderer.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            } else {
                sheepModel.body = defaultBody;
                sheepRenderer.bindTexture(TEXTURE);

                if (entitylivingbaseIn.hasCustomName() && "jeb_".equals(entitylivingbaseIn.getCustomNameTag()))
                {
                    int i1 = 25;
                    int i = entitylivingbaseIn.ticksExisted / 25 + entitylivingbaseIn.getEntityId();
                    int j = EnumDyeColor.values().length;
                    int k = i % j;
                    int l = (i + 1) % j;
                    float f = ((entitylivingbaseIn.ticksExisted % 25) + partialTicks) / 25.0F;
                    float[] afloat1 = EntitySheep.getDyeRgb(EnumDyeColor.byMetadata(k));
                    float[] afloat2 = EntitySheep.getDyeRgb(EnumDyeColor.byMetadata(l));
                    GlStateManager.color(afloat1[0] * (1.0F - f) + afloat2[0] * f, afloat1[1] * (1.0F - f) + afloat2[1] * f, afloat1[2] * (1.0F - f) + afloat2[2] * f);
                }
                else
                {
                    float[] afloat = EntitySheep.getDyeRgb(entitylivingbaseIn.getFleeceColor());
                    GlStateManager.color(afloat[0], afloat[1], afloat[2]);
                }
            }

            sheepModel.setModelAttributes(sheepRenderer.getMainModel());
            sheepModel.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
            sheepModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    private ModelRenderer getChiselBodyModelRenderer(ICarvingVariation variation)
    {
        final ModelRenderer bodyModelRenderer = new ModelRenderer(sheepModel, 28, 8);
        bodyModelRenderer.setRotationPoint(0.0F, 5.0F, 2.0F);

        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();
        matrix.translate(new Vector3f(-5.75f, -11.75f, -8.75f));
        matrix.scale(new Vector3f(11.5f, 19.5f, 9.5f));

        final ModelBox2 bodyBox = new ModelBox2(bodyModelRenderer, matrix);
        bodyModelRenderer.cubeList.add(bodyBox);
        //final Block block = variation.getBlock();

        //final ICarvable carvable = (ICarvable) block;
        final IBlockState blockState = variation.getBlockState();
        //final int metaFromState = block.getMetaFromState(blockState);
        //final BlockFaceData blockFaceData = carvable.getBlockFaceData();
        //final VariationFaceData variationFaceData = blockFaceData.getForMeta(metaFromState);

        final BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
        final IBakedModel model = blockRenderer.getModelForState(blockState);

        ForgeHooksClient.setRenderLayer(BlockRenderLayer.SOLID);
        for (final EnumFacing value : EnumFacing.VALUES)
        {
            //final IChiselFace chiselFace = variationFaceData.getFaceForSide(value);
            final List<BakedQuad> northQuads = model.getQuads(blockState, value, 0);
            bodyBox.addCustomQuads(northQuads);
            /*for (final IChiselTexture<?> chiselTexture : chiselFace.getTextureList())
            {
                final IBlockRenderContext blockRenderContext = chiselTexture.getType().getBlockRenderContext(world, new BlockPos(1, 1, 1));
                for (final BakedQuad quad : northQuads)
                {
                    final List<BakedQuad> bakedQuads = chiselTexture.transformQuad(quad, blockRenderContext, 1);
                    bodyBox.addCustomQuads(bakedQuads);
                }
            }*/
        }
        ForgeHooksClient.setRenderLayer(null);

        return bodyModelRenderer;
    }

    public boolean shouldCombineTextures()
    {
        return true;
    }

}