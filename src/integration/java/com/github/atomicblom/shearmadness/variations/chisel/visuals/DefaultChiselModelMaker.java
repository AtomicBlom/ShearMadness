package com.github.atomicblom.shearmadness.variations.chisel.visuals;

import com.github.atomicblom.shearmadness.api.modelmaker.IModelMaker;
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
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.ICarvingVariation;
import team.chisel.client.ChiselExtendedState;

public class DefaultChiselModelMaker implements IModelMaker
{
    private final QuadrupedTransformDefinition transforms;

    public DefaultChiselModelMaker() {
        transforms = new QuadrupedTransformDefinition();
    }

    @Override
    public ModelQuadruped createModel(ItemStack itemStack, EntityLivingBase entity)
    {
        transforms.defineParts();
        final ICarvingVariation variation = CarvingUtils.getChiselRegistry().getVariation(itemStack);

        final ModelQuadruped quadrupedModel = new ModelSheep1();
        transforms.getBodyPartDefinition().ifPresent(definition -> quadrupedModel.body = getChiselBodyModelRenderer(variation, definition));
        transforms.getHeadPartDefinition().ifPresent(definition -> quadrupedModel.head = getChiselBodyModelRenderer(variation, definition));
        transforms.getLeg1PartDefinition().ifPresent(definition -> quadrupedModel.leg1 = getChiselBodyModelRenderer(variation, definition));
        transforms.getLeg2PartDefinition().ifPresent(definition -> quadrupedModel.leg2 = getChiselBodyModelRenderer(variation, definition));
        transforms.getLeg3PartDefinition().ifPresent(definition -> quadrupedModel.leg3 = getChiselBodyModelRenderer(variation, definition));
        transforms.getLeg4PartDefinition().ifPresent(definition -> quadrupedModel.leg4 = getChiselBodyModelRenderer(variation, definition));
        return quadrupedModel;
    }

    private ModelRenderer getChiselBodyModelRenderer(ICarvingVariation variation, PartDefinition partDefinition)
    {
        final IBlockState blockState = variation.getBlockState();
        final BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
        ChiselExtendedState chiselState = new ChiselExtendedState(blockState, null, null);
        final IBakedModel model = blockRenderer.getModelForState(blockState);


        return getModelRendererForBlockState(partDefinition, chiselState, model);
    }
}
