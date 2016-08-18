package com.github.atomicblom.chiselsheep.client.renderer.entity.layers;

import com.github.atomicblom.chiselsheep.ChiselSheepMod;
import com.github.atomicblom.chiselsheep.capability.IChiseledSheepCapability;
import com.github.atomicblom.chiselsheep.client.renderer.entity.FakeWorld;
import com.github.atomicblom.chiselsheep.client.renderer.entity.ModelBox2;
import com.github.atomicblom.chiselsheep.client.renderer.entity.RenderChiselSheep;
import com.sun.javafx.geom.Vec3f;
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
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.ICarvingGroup;
import team.chisel.api.carving.ICarvingVariation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class LayerSheepChiselWool implements LayerRenderer<EntitySheep>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/sheep/sheep_fur.png");
    private final RenderChiselSheep sheepRenderer;
    private ModelSheep1 sheepModel;

    private final Map<ICarvingVariation, ModelSheep1> modelMap = new HashMap<>();
    final FakeWorld world = new FakeWorld();

    private final ModelSheep1 defaultBody;

    public LayerSheepChiselWool(RenderChiselSheep sheepRendererIn)
    {
        sheepRenderer = sheepRendererIn;
        defaultBody = new ModelSheep1();
        sheepModel = defaultBody;
    }

    public void doRenderLayer(EntitySheep sheep, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        if (!sheep.getSheared() && !sheep.isInvisible())
        {
            IChiseledSheepCapability capability = sheep.getCapability(ChiselSheepMod.CHISELED_SHEEP_CAPABILITY, null);
            if (capability.isChiseled()) {
                ItemStack itemStack = capability.getChiselItemStack();
                ICarvingVariation variation = CarvingUtils.getChiselRegistry().getVariation(itemStack);
                Item item = itemStack.getItem();
                if (item instanceof ItemBlock) {

                }

                if (variation == null) {
                    ICarvingGroup group = CarvingUtils.getChiselRegistry().getGroup(itemStack);
                    List<ICarvingVariation> variations = group.getVariations();
                }

                ModelSheep1 bodyModelRenderer = modelMap.get(variation);
                if (bodyModelRenderer == null) {
                    bodyModelRenderer = new ModelSheep1();
                    //if (variation.getBlock() instanceof ICarvable) {
                    bodyModelRenderer.body = getChiselBodyModelRenderer(variation, bodyPartDefinition);
                    bodyModelRenderer.head = getChiselBodyModelRenderer(variation, headPartDefinition);
                    bodyModelRenderer.leg1 = getChiselBodyModelRenderer(variation, leg1PartDefinition);
                    bodyModelRenderer.leg2 = getChiselBodyModelRenderer(variation, leg2PartDefinition);
                    bodyModelRenderer.leg3 = getChiselBodyModelRenderer(variation, leg3PartDefinition);
                    bodyModelRenderer.leg4 = getChiselBodyModelRenderer(variation, leg4PartDefinition);
                    modelMap.put(variation, bodyModelRenderer);
                    //} else {
                    //    sheepModel = defaultBody;
                    //    sheepRenderer.bindTexture(TEXTURE);
                    //}
                }
                sheepModel = bodyModelRenderer;
                sheepRenderer.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            } else {
                sheepModel = defaultBody;
                sheepRenderer.bindTexture(TEXTURE);

                float[] afloat = EntitySheep.getDyeRgb(sheep.getFleeceColor());
                GlStateManager.color(afloat[0], afloat[1], afloat[2]);
            }

            sheepModel.setModelAttributes(sheepRenderer.getMainModel());
            sheepModel.setLivingAnimations(sheep, limbSwing, limbSwingAmount, partialTicks);
            sheepModel.render(sheep, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    private static Matrix4f createPartMatrix(Vector3f size, Vector3f additionalTranslate) {
        size = size.translate(-0.5f, -0.5f, -0.5f);
        Matrix4f testMatrix = new Matrix4f();
        testMatrix.translate((Vector3f)Vector3f.add((Vector3f)new Vector3f(size).scale(0.5f), additionalTranslate, null).negate());
        testMatrix.scale(size);
        return testMatrix;
    }

    private ModelRenderer getChiselBodyModelRenderer(ICarvingVariation variation, PartDefinition partDefinition)
    {
        final ModelRenderer bodyModelRenderer = new ModelRenderer(sheepModel, 28, 8);
        bodyModelRenderer.setRotationPoint(
                partDefinition.rotationPoint.x,
                partDefinition.rotationPoint.y,
                partDefinition.rotationPoint.z
        );

        final ModelBox2 bodyBox = new ModelBox2(
                bodyModelRenderer,
                partDefinition.positionTransform,
                partDefinition.textureTransform);
        bodyModelRenderer.cubeList.add(bodyBox);
        final IBlockState blockState = variation.getBlockState();

        final BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
        final IBakedModel model = blockRenderer.getModelForState(blockState);

        ForgeHooksClient.setRenderLayer(BlockRenderLayer.SOLID);
        for (final EnumFacing value : EnumFacing.VALUES)
        {
            final List<BakedQuad> northQuads = model.getQuads(blockState, value, 0);
            bodyBox.addCustomQuads(northQuads);
        }
        ForgeHooksClient.setRenderLayer(null);

        return bodyModelRenderer;
    }

    private static final PartDefinition bodyPartDefinition;
    private static final PartDefinition headPartDefinition;
    private static final PartDefinition leg1PartDefinition;
    private static final PartDefinition leg2PartDefinition;
    private static final PartDefinition leg3PartDefinition;
    private static final PartDefinition leg4PartDefinition;

    static {
        bodyPartDefinition = new PartDefinition(
                new Vec3f(0.0f, 5.0f, 2.0f),
                createPartMatrix(
                        new Vector3f(12, 20, 10),
                        new Vector3f(0, 2, 4)),
                new Matrix3f()
        );

        headPartDefinition = new PartDefinition(
                new Vec3f(0.0f, 6.0f, -8.0f),
                createPartMatrix(
                        new Vector3f(8, 8, 8),
                        new Vector3f(0, 1, 1)),
                new Matrix3f()
        );

        leg1PartDefinition = new PartDefinition(
                new Vec3f(-3.0f, 12.0f, 7.0f),
                createPartMatrix(
                        new Vector3f(5.6f, 8, 5.6f),
                        new Vector3f(0, -2.75f, 0.1f)),
                new Matrix3f()
        );

        leg2PartDefinition = new PartDefinition(
                new Vec3f(3.0f, 12.0f, 7.0f),
                createPartMatrix(
                        new Vector3f(5.6f, 8, 5.6f),
                        new Vector3f(0, -2.75f, 0.1f)),
                new Matrix3f()
        );

        leg3PartDefinition = new PartDefinition(
                new Vec3f(-3.0f, 12.0f, -5.0f),
                createPartMatrix(
                        new Vector3f(5.6f, 8, 5.6f),
                        new Vector3f(0, -2.75f, 0.1f)),
                new Matrix3f()
        );

        leg4PartDefinition = new PartDefinition(
                new Vec3f(3.0f, 12.0f, -5.0f),
                createPartMatrix(
                        new Vector3f(5.6f, 8, 5.6f),
                        new Vector3f(0, -2.75f, 0.1f)),
                new Matrix3f()
        );
    }

    public boolean shouldCombineTextures()
    {
        return true;
    }

    private class SheepVariant {
        public ModelRenderer body;
    }
}

class PartDefinition {
    final Vec3f rotationPoint;
    final Matrix4f positionTransform;
    final Matrix3f textureTransform;

    PartDefinition(Vec3f rotationPoint, Matrix4f positionTransform, Matrix3f textureTransform) {

        this.rotationPoint = rotationPoint;
        this.positionTransform = positionTransform;
        this.textureTransform = textureTransform;
    }
}