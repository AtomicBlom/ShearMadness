package com.github.atomicblom.shearmadness.rendering;

import com.github.atomicblom.shearmadness.capability.CapabilityProvider;
import com.github.atomicblom.shearmadness.capability.IChiseledSheepCapability;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelSheep1;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.passive.EntitySheep;
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
import team.chisel.api.carving.ICarvingVariation;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("StaticNonFinalField")
@SideOnly(Side.CLIENT)
public class LayerSheepChiselWool implements LayerRenderer<EntitySheep>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/sheep/sheep_fur.png");
    private static final float NintyDegrees = 3.141592653589793f;
    private static PartDefinition bodyPartDefinition;
    private static PartDefinition headPartDefinition;
    private static PartDefinition leg1PartDefinition;
    private static PartDefinition leg2PartDefinition;
    private static PartDefinition leg3PartDefinition;
    private static PartDefinition leg4PartDefinition;

    static
    {
        createPartDefinitions();
    }

    private final RenderChiselSheep sheepRenderer;
    private final Map<ICarvingVariation, ModelSheep1> modelMap = new HashMap<>(16);
    private final ModelSheep1 defaultBody;
    private ModelSheep1 sheepModel;
    public LayerSheepChiselWool(RenderChiselSheep sheepRendererIn)
    {
        sheepRenderer = sheepRendererIn;
        defaultBody = new ModelSheep1();
        sheepModel = defaultBody;
    }

    private static Matrix4f createPartMatrix(Vector3f size, Vector3f additionalTranslate)
    {
        final Vector3f adjustedSize = size.translate(-0.5f, -0.5f, -0.5f);
        final Matrix4f matrix = new Matrix4f();

        matrix.rotate(NintyDegrees, new Vector3f(1, 0, 0));
        matrix.translate(
                (Vector3f) Vector3f.add(
                        (Vector3f) new Vector3f(adjustedSize).scale(0.5f),
                        additionalTranslate,
                        null
                ).negate());
        matrix.scale(adjustedSize);
        return matrix;
    }

    private static void createPartDefinitions()
    {
        final Matrix4f rotate = new Matrix4f().rotate((float) Math.toRadians(-90), new Vector3f(1, 0, 0));
        bodyPartDefinition = new PartDefinition(
                new Vector3f(0.0f, 5.0f, 2.0f),
                Matrix4f.mul(
                        createPartMatrix(
                                new Vector3f(12, 20, 10),
                                new Vector3f(0, -2, -14)), rotate, null),
                new Matrix3f()
        );

        headPartDefinition = new PartDefinition(
                new Vector3f(0.0f, 6.0f, -8.0f),
                createPartMatrix(
                        new Vector3f(8, 8, 8),
                        new Vector3f(0, -1, -1)),
                new Matrix3f()
        );

        leg1PartDefinition = new PartDefinition(
                new Vector3f(-3.0f, 12.0f, 7.0f),
                createPartMatrix(
                        new Vector3f(5.6f, 7.4f, 5.6f),
                        new Vector3f(0, 3, 0.1f)),
                new Matrix3f()
        );

        leg2PartDefinition = new PartDefinition(
                new Vector3f(3.0f, 12.0f, 7.0f),
                createPartMatrix(
                        new Vector3f(5.6f, 7.4f, 5.6f),
                        new Vector3f(0, 3, 0.1f)),
                new Matrix3f()
        );

        leg3PartDefinition = new PartDefinition(
                new Vector3f(-3.0f, 12.0f, -5.0f),
                createPartMatrix(
                        new Vector3f(5.6f, 7.4f, 5.6f),
                        new Vector3f(0, 3, 0.1f)),
                new Matrix3f()
        );

        leg4PartDefinition = new PartDefinition(
                new Vector3f(3.0f, 12.0f, -5.0f),
                createPartMatrix(
                        new Vector3f(5.6f, 7.4f, 5.6f),
                        new Vector3f(0, 3, 0.1f)),
                new Matrix3f()
        );
    }

    @Override
    @SuppressWarnings({"ConstantConditions", "OverlyLongMethod"})
    public void doRenderLayer(EntitySheep sheep, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        if (!sheep.getSheared() && !sheep.isInvisible())
        {
            final IChiseledSheepCapability capability = sheep.getCapability(CapabilityProvider.CHISELED_SHEEP, null);
            if (capability.isChiseled())
            {
                final ItemStack itemStack = capability.getChiselItemStack();
                final ICarvingVariation variation = CarvingUtils.getChiselRegistry().getVariation(itemStack);

                createPartDefinitions();

                ModelSheep1 bodyModelRenderer;
                if (variation == null)
                {
                    //TODO: got to figure out how to store this kind of model.
                    bodyModelRenderer = new ModelSheep1();

                    bodyModelRenderer.body = getChiselBodyModelRenderer(itemStack, sheep, bodyPartDefinition);
                    bodyModelRenderer.head = getChiselBodyModelRenderer(itemStack, sheep, headPartDefinition);
                    bodyModelRenderer.leg1 = getChiselBodyModelRenderer(itemStack, sheep, leg1PartDefinition);
                    bodyModelRenderer.leg2 = getChiselBodyModelRenderer(itemStack, sheep, leg2PartDefinition);
                    bodyModelRenderer.leg3 = getChiselBodyModelRenderer(itemStack, sheep, leg3PartDefinition);
                    bodyModelRenderer.leg4 = getChiselBodyModelRenderer(itemStack, sheep, leg4PartDefinition);
                } else
                {
                    bodyModelRenderer = modelMap.get(variation);
                    if (bodyModelRenderer == null)
                    {
                        bodyModelRenderer = new ModelSheep1();

                        bodyModelRenderer.body = getChiselBodyModelRenderer(variation, bodyPartDefinition);
                        bodyModelRenderer.head = getChiselBodyModelRenderer(variation, headPartDefinition);
                        bodyModelRenderer.leg1 = getChiselBodyModelRenderer(variation, leg1PartDefinition);
                        bodyModelRenderer.leg2 = getChiselBodyModelRenderer(variation, leg2PartDefinition);
                        bodyModelRenderer.leg3 = getChiselBodyModelRenderer(variation, leg3PartDefinition);
                        bodyModelRenderer.leg4 = getChiselBodyModelRenderer(variation, leg4PartDefinition);
                        modelMap.put(variation, bodyModelRenderer);
                    }
                }
                sheepModel = bodyModelRenderer;
                sheepRenderer.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

                final Item item = itemStack.getItem();
                if (item instanceof ItemBlock)
                {
                    final int colorMultiplier = Minecraft.getMinecraft().getBlockColors().colorMultiplier(((ItemBlock) item).block.getDefaultState(), sheep.worldObj, sheep.getPosition(), 0);
                    float red = (float)(colorMultiplier >> 16 & 255) / 255.0F;
                    float green = (float)(colorMultiplier >> 8 & 255) / 255.0F;
                    float blue = (float)(colorMultiplier & 255) / 255.0F;
                    GlStateManager.color(red, green, blue);
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

    private ModelRenderer getChiselBodyModelRenderer(ItemStack item, EntitySheep entity, PartDefinition partDefinition)
    {
        final RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        IBakedModel itemModel = renderItem.getItemModelMesher().getItemModel(item);
        itemModel = itemModel.getOverrides().handleItemState(itemModel, item, Minecraft.getMinecraft().theWorld, entity);

        return getModelRenderer(partDefinition, null, itemModel);
    }

    private ModelRenderer getChiselBodyModelRenderer(ICarvingVariation variation, PartDefinition partDefinition)
    {
        final IBlockState blockState = variation.getBlockState();
        final BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
        final IBakedModel model = blockRenderer.getModelForState(blockState);

        return getModelRenderer(partDefinition, blockState, model);
    }

    private ModelRenderer getModelRenderer(PartDefinition partDefinition, IBlockState blockState, IBakedModel model)
    {
        final ModelRenderer renderer = new ModelRenderer(sheepModel, 28, 8);
        renderer.setRotationPoint(
                partDefinition.getRotationPoint().x,
                partDefinition.getRotationPoint().y,
                partDefinition.getRotationPoint().z
        );

        final EntityMesh box = new EntityMesh(
                renderer,
                partDefinition.getPositionTransform(),
                partDefinition.getTextureTransform());

        renderer.cubeList.add(box);

        ForgeHooksClient.setRenderLayer(BlockRenderLayer.SOLID);
        for (final EnumFacing value : EnumFacing.VALUES)
        {
            box.addCustomQuads(model.getQuads(blockState, value, 0));
        }
        box.addCustomQuads(model.getQuads(blockState, null, 0));
        ForgeHooksClient.setRenderLayer(null);

        return renderer;
    }
}

