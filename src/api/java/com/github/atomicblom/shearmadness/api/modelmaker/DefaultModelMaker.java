package com.github.atomicblom.shearmadness.api.modelmaker;

import net.minecraft.client.renderer.entity.model.QuadrupedModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.entity.model.SheepWoolModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.ItemStack;
import com.github.atomicblom.shearmadness.api.rendering.QuadrupedTransformDefinition;

@SuppressWarnings("ClassHasNoToStringMethod")
public class DefaultModelMaker implements IModelMaker
{
    private final QuadrupedTransformDefinition transforms;

    public static RendererModel defaultRenderer = new RendererModel(new SheepWoolModel<>(), 0, 0);

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
        quadrupedModel.headModel = defaultRenderer;
        quadrupedModel.legFrontRight = defaultRenderer;
        quadrupedModel.legFrontLeft = defaultRenderer;
        quadrupedModel.legBackRight = defaultRenderer;
        quadrupedModel.legBackLeft = defaultRenderer;
        final IBakedModel bakedModelForItem = getBakedModelForItem(itemStack, entity);
        transforms.getBodyPartDefinition().ifPresent(definition -> quadrupedModel.body = getModelRendererForBlockState(definition, null, bakedModelForItem));
        transforms.getHeadPartDefinition().ifPresent(definition -> quadrupedModel.headModel = getModelRendererForBlockState(definition, null, bakedModelForItem));
        transforms.getLegFrontRightPartDefinition().ifPresent(definition -> quadrupedModel.legFrontRight = getModelRendererForBlockState(definition, null, bakedModelForItem));
        transforms.getLegFrontLeftPartDefinition().ifPresent(definition -> quadrupedModel.legFrontLeft = getModelRendererForBlockState(definition, null, bakedModelForItem));
        transforms.getLegBackRightPartDefinition().ifPresent(definition -> quadrupedModel.legBackRight = getModelRendererForBlockState(definition, null, bakedModelForItem));
        transforms.getLegBackLeftPartDefinition().ifPresent(definition -> quadrupedModel.legBackLeft = getModelRendererForBlockState(definition, null, bakedModelForItem));
        return quadrupedModel;
    }

    protected QuadrupedTransformDefinition getTransforms() {
        return transforms;
    }
}
