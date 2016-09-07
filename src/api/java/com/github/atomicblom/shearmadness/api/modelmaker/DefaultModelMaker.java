package com.github.atomicblom.shearmadness.api.modelmaker;

import com.github.atomicblom.shearmadness.api.rendering.PartDefinition;
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
        transforms.getBodyPartDefinition().ifPresent(definition -> quadrupedModel.body = getChiselBodyModelRenderer(itemStack, entity, definition));
        transforms.getHeadPartDefinition().ifPresent(definition -> quadrupedModel.head = getChiselBodyModelRenderer(itemStack, entity, definition));
        transforms.getLeg1PartDefinition().ifPresent(definition -> quadrupedModel.leg1 = getChiselBodyModelRenderer(itemStack, entity, definition));
        transforms.getLeg2PartDefinition().ifPresent(definition -> quadrupedModel.leg2 = getChiselBodyModelRenderer(itemStack, entity, definition));
        transforms.getLeg3PartDefinition().ifPresent(definition -> quadrupedModel.leg3 = getChiselBodyModelRenderer(itemStack, entity, definition));
        transforms.getLeg4PartDefinition().ifPresent(definition -> quadrupedModel.leg4 = getChiselBodyModelRenderer(itemStack, entity, definition));
        return quadrupedModel;
    }

    protected ModelRenderer getChiselBodyModelRenderer(ItemStack item, EntityLivingBase entity, PartDefinition partDefinition)
    {
        final RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        IBakedModel itemModel = renderItem.getItemModelMesher().getItemModel(item);
        itemModel = itemModel.getOverrides().handleItemState(itemModel, item, Minecraft.getMinecraft().theWorld, entity);

        return getModelRendererForBlockState(partDefinition, null, itemModel);
    }
}
