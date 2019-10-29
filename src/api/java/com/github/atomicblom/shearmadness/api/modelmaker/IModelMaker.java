package com.github.atomicblom.shearmadness.api.modelmaker;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.model.QuadrupedModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.entity.model.SheepWoolModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;
import com.github.atomicblom.shearmadness.api.rendering.EntityMesh;
import com.github.atomicblom.shearmadness.api.rendering.PartDefinition;
import com.github.atomicblom.shearmadness.api.rendering.vector.*;

import java.util.Random;

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
    default RendererModel getModelRendererForBlockState(PartDefinition partDefinition, BlockState blockState, IBakedModel model, String partName)
    {
        final RendererModel renderer = new RendererModel(new SheepWoolModel<>(), 0, 0);
        if (partDefinition == null) {
            return renderer;
        }
        renderer.setRotationPoint(
                partDefinition.getRotationPoint().x,
                partDefinition.getRotationPoint().y,
                partDefinition.getRotationPoint().z
        );

        final EntityMesh box = new EntityMesh(renderer);
        box.boxName = partName;
        addBlockModelToEntityMesh(box, partDefinition, blockState, model);

        renderer.cubeList.add(box);
        return renderer;
    }

    default RendererModel createModelRenderer(PartDefinition partDefinition) {
        final RendererModel renderer = new RendererModel(new SheepWoolModel<>(), 0, 0);
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

    @SuppressWarnings("deprecation")
    default EntityMesh addBlockModelToEntityMesh(EntityMesh box, PartDefinition partDefinition, BlockState blockState, IBakedModel model) {
        final Matrix4f positionTransform = partDefinition.getPositionTransform();
        final Matrix3f textureTransform = partDefinition.getTextureTransform();

        Random random = new Random();

        //FIXME: When chisel start reporting their render layers correctly, we can improve this logic.
        //if (blockState == null || blockState.getBlock().canRenderInLayer(blockState, BlockRenderLayer.SOLID)) {
            ForgeHooksClient.setRenderLayer(BlockRenderLayer.SOLID);
            for (final Direction value : Direction.values())
            {
                box.addBakedQuads(positionTransform, textureTransform, model.getQuads(blockState, value, random));
            }
            box.addBakedQuads(positionTransform, textureTransform, model.getQuads(blockState, null, random));
        //}
        //if (blockState != null && blockState.getBlock().canRenderInLayer(blockState, BlockRenderLayer.CUTOUT)) {
            ForgeHooksClient.setRenderLayer(BlockRenderLayer.CUTOUT);
            for (final Direction value : Direction.values())
            {
                box.addBakedQuads(positionTransform, textureTransform, model.getQuads(blockState, value, random));
            }
            box.addBakedQuads(positionTransform, textureTransform, model.getQuads(blockState, null, random));
        //}
        //if (blockState != null && blockState.getBlock().canRenderInLayer(blockState, BlockRenderLayer.CUTOUT_MIPPED)) {
            ForgeHooksClient.setRenderLayer(BlockRenderLayer.CUTOUT_MIPPED);
            for (final Direction value : Direction.values())
            {
                box.addBakedQuads(positionTransform, textureTransform, model.getQuads(blockState, value, random));
            }
            box.addBakedQuads(positionTransform, textureTransform, model.getQuads(blockState, null, random));
        //}
        /*if (blockState != null && blockState.getBlock().canRenderInLayer(blockState, BlockRenderLayer.TRANSLUCENT)) {
            ForgeHooksClient.setRenderLayer(BlockRenderLayer.TRANSLUCENT);
            for (final EnumFacing value : EnumFacing.VALUES)
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
        itemModel = itemModel.getOverrides().getModelWithOverrides(itemModel, item, Minecraft.getInstance().world, entity);
        return itemModel;
    }
}
