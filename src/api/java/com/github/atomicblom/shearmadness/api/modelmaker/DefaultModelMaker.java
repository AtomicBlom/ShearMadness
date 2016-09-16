package com.github.atomicblom.shearmadness.api.modelmaker;

import com.github.atomicblom.shearmadness.api.rendering.QuadrupedTransformDefinition;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelQuadruped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelSheep1;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

@SuppressWarnings("ClassHasNoToStringMethod")
public class DefaultModelMaker implements IModelMaker
{
    private final QuadrupedTransformDefinition transforms;

    public static ModelRenderer defaultRenderer = new ModelRenderer(new ModelSheep1(), 0, 0);

    public DefaultModelMaker(QuadrupedTransformDefinition transforms) {
        this.transforms = transforms;
    }
    public DefaultModelMaker() {
        transforms = new QuadrupedTransformDefinition();
    }

    @Override
    public ModelQuadruped createModel(ItemStack itemStack, EntityLivingBase entity)
    {
        transforms.defineParts();
        final ModelQuadruped quadrupedModel = new ModelSheep1();
        quadrupedModel.body = defaultRenderer;
        quadrupedModel.head = defaultRenderer;
        quadrupedModel.leg1 = defaultRenderer;
        quadrupedModel.leg2 = defaultRenderer;
        quadrupedModel.leg3 = defaultRenderer;
        quadrupedModel.leg4 = defaultRenderer;
        final IBakedModel bakedModelForItem = getBakedModelForItem(itemStack, entity);
        transforms.getBodyPartDefinition().ifPresent(definition -> quadrupedModel.body = getModelRendererForBlockState(definition, null, bakedModelForItem));
        transforms.getHeadPartDefinition().ifPresent(definition -> quadrupedModel.head = getModelRendererForBlockState(definition, null, bakedModelForItem));
        transforms.getLeg1PartDefinition().ifPresent(definition -> quadrupedModel.leg1 = getModelRendererForBlockState(definition, null, bakedModelForItem));
        transforms.getLeg2PartDefinition().ifPresent(definition -> quadrupedModel.leg2 = getModelRendererForBlockState(definition, null, bakedModelForItem));
        transforms.getLeg3PartDefinition().ifPresent(definition -> quadrupedModel.leg3 = getModelRendererForBlockState(definition, null, bakedModelForItem));
        transforms.getLeg4PartDefinition().ifPresent(definition -> quadrupedModel.leg4 = getModelRendererForBlockState(definition, null, bakedModelForItem));
        return quadrupedModel;
    }

    protected QuadrupedTransformDefinition getTransforms() {
        return transforms;
    }
}
