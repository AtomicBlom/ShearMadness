package com.github.atomicblom.shearmadness.api.modelmaker;

import com.github.atomicblom.shearmadness.api.rendering.EntityMesh;
import com.github.atomicblom.shearmadness.api.rendering.PartDefinition;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.QuadrupedModel;
import net.minecraft.client.renderer.entity.model.SheepWoolModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;

/**
 * Creates a custom Quadruped model
 */
@FunctionalInterface
@OnlyIn(Dist.CLIENT)
public interface IModelMaker
{
    /**
     * Create a model for a quadruped entity based on what has been chiseled onto it.
     * @param itemStack The item that was in the chisel when it was applied to the entity
     * @param entity The entity to create a model for
     * @return a model for the given itemStack
     */
    QuadrupedModel<SheepEntity> createModel(ItemStack itemStack, LivingEntity entity);

    /**
     * Utility method to convert an IBakedModel to a ModelRenderer for use on an entity body part.
     * @param partDefinition transforms for the body part
     * @param blockState block state to use when retrieving the model
     * @param model the IBakedModel to transform
     * @return a body part for the entity
     */
    default ModelRenderer getModelRendererForBlockState(PartDefinition partDefinition, BlockState blockState, IBakedModel model)
    {
        final ModelRenderer renderer = new ModelRenderer(new SheepWoolModel<>(), 0, 0);
        if (partDefinition == null) {
            return renderer;
        }
        renderer.setRotationPoint(
                partDefinition.getRotationPoint().x,
                partDefinition.getRotationPoint().y,
                partDefinition.getRotationPoint().z
        );

        final EntityMesh box = new EntityMesh(renderer);

        addBlockModelToEntityMesh(box, partDefinition, blockState, model);

        renderer.cubeList.add(box);
        return renderer;
    }

    default ModelRenderer createModelRenderer(PartDefinition partDefinition) {
        final ModelRenderer renderer = new ModelRenderer(new SheepWoolModel<>(), 0, 0);
        if (partDefinition == null) {
            return renderer;
        }
        renderer.setRotationPoint(
                partDefinition.getRotationPoint().x,
                partDefinition.getRotationPoint().y,
                partDefinition.getRotationPoint().z
        );

        return renderer;
    }

    default EntityMesh addBlockModelToEntityMesh(EntityMesh box, PartDefinition partDefinition, BlockState blockState, IBakedModel model) {
        final Matrix4f positionTransform = partDefinition.getPositionTransform();
        final Matrix3f textureTransform = partDefinition.getTextureTransform();
        //FIXME: When chisel start reporting their render layers correctly, we can improve this logic.
        //if (blockState == null || blockState.getBlock().canRenderInLayer(blockState, BlockRenderLayer.SOLID)) {
            ForgeHooksClient.setRenderLayer(RenderType.getSolid());
            for (final Direction value : Direction.values())
            {
                box.addBakedQuads(positionTransform, textureTransform, model.getQuads(blockState, value, 0));
            }
            box.addBakedQuads(positionTransform, textureTransform, model.getQuads(blockState, null, 0));
        //}
        //if (blockState != null && blockState.getBlock().canRenderInLayer(blockState, BlockRenderLayer.CUTOUT)) {
            ForgeHooksClient.setRenderLayer(RenderType.getCutout());
            for (final Direction value : Direction.values())
            {
                box.addBakedQuads(positionTransform, textureTransform, model.getQuads(blockState, value, 0));
            }
            box.addBakedQuads(positionTransform, textureTransform, model.getQuads(blockState, null, 0));
        //}
        //if (blockState != null && blockState.getBlock().canRenderInLayer(blockState, BlockRenderLayer.CUTOUT_MIPPED)) {
            ForgeHooksClient.setRenderLayer(RenderType.getCutoutMipped());
            for (final Direction value : Direction.values())
            {
                box.addBakedQuads(positionTransform, textureTransform, model.getQuads(blockState, value, 0));
            }
            box.addBakedQuads(positionTransform, textureTransform, model.getQuads(blockState, null, 0));
        //}
        /*if (blockState != null && blockState.getBlock().canRenderInLayer(blockState, BlockRenderLayer.TRANSLUCENT)) {
            ForgeHooksClient.setRenderLayer(BlockRenderLayer.TRANSLUCENT);
            for (final Direction value : Direction.values())
            {
                box.addBakedQuads(positionTransform, textureTransform, model.getQuads(blockState, value, 0));
            }
            box.addBakedQuads(positionTransform, textureTransform, model.getQuads(blockState, null, 0));
        //}*/

        ForgeHooksClient.setRenderLayer(null);
        return box;
    }

    default IBakedModel getBakedModelForItem(ItemStack item, LivingEntity entity) {
        final ItemRenderer renderItem = Minecraft.getInstance().getItemRenderer();
        IBakedModel itemModel = renderItem.getItemModelMesher().getItemModel(item);
        itemModel = itemModel.getOverrides().func_239290_a_(itemModel, item, Minecraft.getInstance().world, entity);
        return itemModel;
    }
}
