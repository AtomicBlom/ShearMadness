package com.github.atomicblom.chiselsheep.client.renderer.entity.layers;

import com.github.atomicblom.chiselsheep.ChiselSheepMod;
import com.github.atomicblom.chiselsheep.capability.IChiseledSheepCapability;
import com.github.atomicblom.chiselsheep.client.renderer.entity.FakeWorld;
import com.github.atomicblom.chiselsheep.client.renderer.entity.ModelBox2;
import com.github.atomicblom.chiselsheep.client.renderer.entity.RenderChiselSheep;
import com.google.common.collect.Iterables;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelSheep1;
import net.minecraft.client.model.TextureOffset;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.layers.LayerSheepWool;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad.Builder;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.chisel.api.block.ICarvable;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.ICarvingVariation;
import team.chisel.api.render.IBlockRenderContext;
import team.chisel.api.render.IChiselFace;
import team.chisel.api.render.IChiselTexture;
import team.chisel.api.render.IRenderContextProvider;
import team.chisel.client.BlockFaceData;
import team.chisel.client.BlockFaceData.VariationFaceData;

import javax.vecmath.Vector4f;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@SideOnly(Side.CLIENT)
public class LayerSheepChiselWool implements LayerRenderer<EntitySheep>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/sheep/sheep_fur.png");
    private final RenderChiselSheep sheepRenderer;
    private final ModelSheep1 sheepModel;

    private final Map<ICarvingVariation, ModelRenderer> modelMap = new HashMap<ICarvingVariation, ModelRenderer>();
    final FakeWorld world = new FakeWorld();

    private final ModelRenderer defaultBody;

    public LayerSheepChiselWool(RenderChiselSheep sheepRendererIn)
    {
        this.sheepRenderer = sheepRendererIn;
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
        final ModelBox2 bodyBox = new ModelBox2(bodyModelRenderer);
        bodyModelRenderer.cubeList.add(bodyBox);
        final Block block = variation.getBlock();

        final ICarvable carvable = (ICarvable) block;
        final IBlockState blockState = variation.getBlockState();
        final int metaFromState = block.getMetaFromState(blockState);
        final BlockFaceData blockFaceData = carvable.getBlockFaceData();
        final VariationFaceData variationFaceData = blockFaceData.getForMeta(metaFromState);

        final BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
        final IBakedModel model = blockRenderer.getModelForState(blockState);

        ForgeHooksClient.setRenderLayer(BlockRenderLayer.SOLID);
        for (final EnumFacing value : EnumFacing.VALUES)
        {
            final IChiselFace chiselFace = variationFaceData.getFaceForSide(value);
            final List<BakedQuad> northQuads = model.getQuads(blockState, value, 0);

            for (final IChiselTexture<?> chiselTexture : chiselFace.getTextureList())
            {
                final IBlockRenderContext blockRenderContext = chiselTexture.getType().getBlockRenderContext(world, new BlockPos(1, 1, 1));
                for (final BakedQuad quad : northQuads)
                {
                    final List<BakedQuad> bakedQuads = chiselTexture.transformQuad(quad, blockRenderContext, 1);
                    bodyBox.addCustomQuads(bakedQuads);
                }
            }
        }
        ForgeHooksClient.setRenderLayer(null);

        return bodyModelRenderer;
    }

    public boolean shouldCombineTextures()
    {
        return true;
    }

    private static void putVertex(UnpackedBakedQuad.Builder builder, VertexFormat format, TRSRTransformation transform, EnumFacing side,
                                  float x, float y, float z, float u, float v, int color)
    {
        Vector4f vec = new Vector4f();
        for (int e = 0; e < format.getElementCount(); e++)
        {
            switch (format.getElement(e).getUsage())
            {
                case POSITION:
                    if (transform == TRSRTransformation.identity())
                    {
                        builder.put(e, x, y, z, 1);
                    }
                    // only apply the transform if it's not identity
                    else
                    {
                        vec.x = x;
                        vec.y = y;
                        vec.z = z;
                        vec.w = 1;
                        transform.getMatrix().transform(vec);
                        builder.put(e, vec.x, vec.y, vec.z, vec.w);
                    }
                    break;
                case COLOR:
                    float r = ((color >> 16) & 0xFF) / 255f; // red
                    float g = ((color >> 8) & 0xFF) / 255f; // green
                    float b = ((color >> 0) & 0xFF) / 255f; // blue
                    float a = ((color >> 24) & 0xFF) / 255f; // alpha
                    builder.put(e, r, g, b, a);
                    break;
                case UV:
                    if (format.getElement(e).getIndex() == 0)
                    {
                        builder.put(e, u, v, 0f, 1f);
                        break;
                    }
                case NORMAL:
                    builder.put(e, (float) side.getFrontOffsetX(), (float) side.getFrontOffsetY(), (float) side.getFrontOffsetZ(), 0f);
                    break;
                default:
                    builder.put(e);
                    break;
            }
        }
    }
}