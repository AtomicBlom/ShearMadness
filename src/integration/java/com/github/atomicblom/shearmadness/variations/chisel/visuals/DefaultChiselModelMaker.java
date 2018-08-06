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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.ICarvingVariation;
import team.chisel.ctm.client.state.CTMExtendedState;


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

        final BlockPos position = entity.getPosition();
        final World world = entity.getEntityWorld();

        final ModelQuadruped quadrupedModel = new ModelSheep1();
        transforms.getBodyPartDefinition().ifPresent(definition -> quadrupedModel.body = getChiselBodyModelRenderer(variation, definition, world, position));
        transforms.getHeadPartDefinition().ifPresent(definition -> quadrupedModel.head = getChiselBodyModelRenderer(variation, definition, world, position));
        transforms.getLeg1PartDefinition().ifPresent(definition -> quadrupedModel.leg1 = getChiselBodyModelRenderer(variation, definition, world, position));
        transforms.getLeg2PartDefinition().ifPresent(definition -> quadrupedModel.leg2 = getChiselBodyModelRenderer(variation, definition, world, position));
        transforms.getLeg3PartDefinition().ifPresent(definition -> quadrupedModel.leg3 = getChiselBodyModelRenderer(variation, definition, world, position));
        transforms.getLeg4PartDefinition().ifPresent(definition -> quadrupedModel.leg4 = getChiselBodyModelRenderer(variation, definition, world, position));
        return quadrupedModel;
    }

    private ModelRenderer getChiselBodyModelRenderer(ICarvingVariation variation, PartDefinition partDefinition, World world, BlockPos position)
    {
        final IBlockState blockState = variation.getBlockState();
        final BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
        CTMExtendedState chiselState = new CTMExtendedState(blockState, world, position);
        final IBakedModel model = blockRenderer.getModelForState(blockState);


        return getModelRendererForBlockState(partDefinition, chiselState, model);
    }
}
