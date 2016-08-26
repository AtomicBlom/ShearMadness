package com.github.atomicblom.shearmadness.modelmaker;

import com.github.atomicblom.shearmadness.api.IModelMaker;
import com.github.atomicblom.shearmadness.api.rendering.PartDefinition;
import com.github.atomicblom.shearmadness.api.rendering.QuadrupedTransformDefinition;
import com.github.atomicblom.shearmadness.configuration.Settings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelQuadruped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelSheep1;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class DefaultModelMaker extends IModelMaker
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
        if (Settings.debugModels()) {
            transforms.defineParts();
        }

        final ModelQuadruped quadrupedModel = new ModelSheep1();
        quadrupedModel.body = getChiselBodyModelRenderer(itemStack, entity, transforms.getBodyPartDefinition());
        quadrupedModel.head = getChiselBodyModelRenderer(itemStack, entity, transforms.getHeadPartDefinition());
        quadrupedModel.leg1 = getChiselBodyModelRenderer(itemStack, entity, transforms.getLeg1PartDefinition());
        quadrupedModel.leg2 = getChiselBodyModelRenderer(itemStack, entity, transforms.getLeg2PartDefinition());
        quadrupedModel.leg3 = getChiselBodyModelRenderer(itemStack, entity, transforms.getLeg3PartDefinition());
        quadrupedModel.leg4 = getChiselBodyModelRenderer(itemStack, entity, transforms.getLeg4PartDefinition());
        return quadrupedModel;
    }

    private ModelRenderer getChiselBodyModelRenderer(ItemStack item, EntityLivingBase entity, PartDefinition partDefinition)
    {
        final RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        IBakedModel itemModel = renderItem.getItemModelMesher().getItemModel(item);
        itemModel = itemModel.getOverrides().handleItemState(itemModel, item, Minecraft.getMinecraft().theWorld, entity);

        return getModelRenderer(partDefinition, null, itemModel);
    }
}
