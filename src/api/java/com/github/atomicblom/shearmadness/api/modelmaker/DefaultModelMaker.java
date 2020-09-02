package com.github.atomicblom.shearmadness.api.modelmaker;

import net.minecraft.client.renderer.entity.model.QuadrupedModel;
import net.minecraft.client.renderer.entity.model.SheepWoolModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.SpriteMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import com.github.atomicblom.shearmadness.api.rendering.QuadrupedTransformDefinition;

import java.util.Optional;

@SuppressWarnings("ClassHasNoToStringMethod")
public class DefaultModelMaker implements IModelMaker
{
    private final QuadrupedTransformDefinition transforms;

    public static ModelRenderer defaultRenderer = new ModelRenderer(new SheepWoolModel<>());
    private final Optional<Boolean> renderWool;

    public DefaultModelMaker(QuadrupedTransformDefinition transforms, boolean renderWool) {
        this.transforms = transforms;
        this.renderWool = Optional.of(renderWool);
    }
    public DefaultModelMaker() {
        transforms = new QuadrupedTransformDefinition();
        renderWool = Optional.empty();
    }

    @Override
    public QuadrupedModel<SheepEntity> createModel(ItemStack itemStack, LivingEntity entity, SpriteMap spriteMap)
    {
        transforms.defineParts(entity);
        final QuadrupedModel<SheepEntity> quadrupedModel = new SheepWoolModel<>();

        quadrupedModel.body = defaultRenderer;
        quadrupedModel.headModel = defaultRenderer;
        quadrupedModel.legFrontRight = defaultRenderer;
        quadrupedModel.legFrontLeft = defaultRenderer;
        quadrupedModel.legBackRight = defaultRenderer;
        quadrupedModel.legBackLeft = defaultRenderer;
        final IBakedModel bakedModelForItem = getBakedModelForItem(itemStack, entity);
        transforms.getBodyPartDefinition().ifPresent(definition -> quadrupedModel.body = getModelRendererForBlockState(definition, null, bakedModelForItem, "body"));
        transforms.getHeadPartDefinition().ifPresent(definition -> quadrupedModel.headModel = getModelRendererForBlockState(definition, null, bakedModelForItem, "head"));
        transforms.getLegFrontRightPartDefinition().ifPresent(definition -> quadrupedModel.legFrontRight = getModelRendererForBlockState(definition, null, bakedModelForItem, "legFrontRight"));
        transforms.getLegFrontLeftPartDefinition().ifPresent(definition -> quadrupedModel.legFrontLeft = getModelRendererForBlockState(definition, null, bakedModelForItem, "legFrontLeft"));
        transforms.getLegBackRightPartDefinition().ifPresent(definition -> quadrupedModel.legBackRight = getModelRendererForBlockState(definition, null, bakedModelForItem, "legBackRight"));
        transforms.getLegBackLeftPartDefinition().ifPresent(definition -> quadrupedModel.legBackLeft = getModelRendererForBlockState(definition, null, bakedModelForItem, "legBackLeft"));
        return quadrupedModel;
    }

    protected QuadrupedTransformDefinition getTransforms() {
        return transforms;
    }

    @Override
    public boolean shouldRenderWool(ItemStack itemStack) {
        return renderWool.orElseGet(() -> !(itemStack.getItem() instanceof BlockItem));
    }
}
