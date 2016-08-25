package com.github.atomicblom.shearmadness.modelmaker;

import com.github.atomicblom.shearmadness.api.IModelMaker;
import com.github.atomicblom.shearmadness.api.rendering.PartDefinition;
import com.github.atomicblom.shearmadness.api.rendering.QuadrupedTransformDefinition;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelQuadruped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelSheep1;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.ICarvingVariation;

public class DefaultChiselModelMaker extends IModelMaker
{
    private final QuadrupedTransformDefinition transforms;

    public DefaultChiselModelMaker() {
        transforms = new QuadrupedTransformDefinition();
    }

    @Override
    public ModelQuadruped createModel(ItemStack itemStack, EntityLivingBase entity)
    {
        final ICarvingVariation variation = CarvingUtils.getChiselRegistry().getVariation(itemStack);

        final ModelQuadruped quadrupedModel = new ModelSheep1();
        quadrupedModel.body = getChiselBodyModelRenderer(variation, transforms.getBodyPartDefinition());
        quadrupedModel.head = getChiselBodyModelRenderer(variation, transforms.getHeadPartDefinition());
        quadrupedModel.leg1 = getChiselBodyModelRenderer(variation, transforms.getLeg1PartDefinition());
        quadrupedModel.leg2 = getChiselBodyModelRenderer(variation, transforms.getLeg2PartDefinition());
        quadrupedModel.leg3 = getChiselBodyModelRenderer(variation, transforms.getLeg3PartDefinition());
        quadrupedModel.leg4 = getChiselBodyModelRenderer(variation, transforms.getLeg4PartDefinition());
        return quadrupedModel;
    }

    private ModelRenderer getChiselBodyModelRenderer(ICarvingVariation variation, PartDefinition partDefinition)
    {
        final IBlockState blockState = variation.getBlockState();
        final BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
        final IBakedModel model = blockRenderer.getModelForState(blockState);

        return getModelRenderer(partDefinition, blockState, model);
    }
}
