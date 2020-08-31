package com.github.atomicblom.shearmadness.api.modelmaker;

import com.github.atomicblom.shearmadness.api.rendering.QuadrupedTransformDefinition;
import net.minecraft.client.renderer.entity.model.QuadrupedModel;
import net.minecraft.client.renderer.entity.model.SheepWoolModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.ItemStack;

@SuppressWarnings("ClassHasNoToStringMethod")
public class DefaultModelMaker implements IModelMaker
{
    private final QuadrupedTransformDefinition transforms;

    public static ModelRenderer defaultRenderer = new ModelRenderer(new SheepWoolModel<>(), 0, 0);

    public DefaultModelMaker(QuadrupedTransformDefinition transforms) {
        this.transforms = transforms;
    }
    public DefaultModelMaker() {
        transforms = new QuadrupedTransformDefinition();
    }

    @Override
    public QuadrupedModel<SheepEntity> createModel(ItemStack itemStack, LivingEntity entity)
    {
        transforms.defineParts();
        final QuadrupedModel<SheepEntity> quadrupedModel = new SheepWoolModel<>();
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
