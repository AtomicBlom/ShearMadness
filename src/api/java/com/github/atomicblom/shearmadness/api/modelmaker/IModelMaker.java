package com.github.atomicblom.shearmadness.api.modelmaker;

import com.github.atomicblom.shearmadness.api.rendering.EntityMesh;
import com.github.atomicblom.shearmadness.api.rendering.PartDefinition;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelQuadruped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelSheep1;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Creates a custom Quadruped model
 */
@FunctionalInterface
@SideOnly(Side.CLIENT)
public interface IModelMaker
{

    /**
     * Create a model for a quadruped entity based on what has been chiseled onto it.
     * @param itemStack The item that was in the chisel when it was applied to the entity
     * @param entity The entity to create a model for
     * @return a model for the given itemStack
     */
    ModelQuadruped createModel(ItemStack itemStack, EntityLivingBase entity);

    /**
     * Utility method to convert an IBakedModel to a ModelRenderer for use on an entity body part.
     * @param partDefinition transforms for the body part
     * @param blockState block state to use when retrieving the model
     * @param model the IBakedModel to transform
     * @return a body part for the entity
     */
    default ModelRenderer getModelRendererForBlockState(PartDefinition partDefinition, IBlockState blockState, IBakedModel model)
    {
        final ModelRenderer renderer = new ModelRenderer(new ModelSheep1(), 0, 0);
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
        final ModelRenderer renderer = new ModelRenderer(new ModelSheep1(), 0, 0);
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

    default EntityMesh addBlockModelToEntityMesh(EntityMesh box, PartDefinition partDefinition, IBlockState blockState, IBakedModel model) {
        ForgeHooksClient.setRenderLayer(BlockRenderLayer.SOLID);
        for (final EnumFacing value : EnumFacing.VALUES)
        {
            box.addBakedQuads(partDefinition.getPositionTransform(), partDefinition.getTextureTransform(), model.getQuads(blockState, value, 0));
        }
        box.addBakedQuads(partDefinition.getPositionTransform(), partDefinition.getTextureTransform(), model.getQuads(blockState, null, 0));
        ForgeHooksClient.setRenderLayer(null);
        return box;
    }

    default IBakedModel getBakedModelForItem(ItemStack item, EntityLivingBase entity) {
        final RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        IBakedModel itemModel = renderItem.getItemModelMesher().getItemModel(item);
        itemModel = itemModel.getOverrides().handleItemState(itemModel, item, Minecraft.getMinecraft().theWorld, entity);
        return itemModel;
    }
}
