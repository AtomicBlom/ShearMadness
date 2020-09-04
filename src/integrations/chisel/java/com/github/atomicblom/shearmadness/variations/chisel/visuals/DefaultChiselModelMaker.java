package com.github.atomicblom.shearmadness.variations.chisel.visuals;

import com.github.atomicblom.shearmadness.api.modelmaker.IModelMaker;
import com.github.atomicblom.shearmadness.api.rendering.PartDefinition;
import com.github.atomicblom.shearmadness.api.rendering.QuadrupedTransformDefinition;
import net.minecraft.block.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelQuadruped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelSheep1;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.entity.model.QuadrupedModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.entity.model.SheepWoolModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.SpriteMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.SheepEntity;
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
    public QuadrupedModel<SheepEntity> createModel(ItemStack itemStack, LivingEntity entity, SpriteMap spriteMap)
    {
        transforms.defineParts(entity);
        final ICarvingVariation variation = CarvingUtils.getChiselRegistry().getVariation(itemStack);

        final BlockPos position = entity.getPosition();
        final World world = entity.getEntityWorld();

        final QuadrupedModel<SheepEntity> quadrupedModel = new SheepWoolModel<>();
        transforms.getBodyPartDefinition().ifPresent(definition -> quadrupedModel.body = getChiselBodyModelRenderer(variation, definition, world, position));
        transforms.getHeadPartDefinition().ifPresent(definition -> quadrupedModel.headModel = getChiselBodyModelRenderer(variation, definition, world, position));
        transforms.getLegFrontRightPartDefinition().ifPresent(definition -> quadrupedModel.legFrontRight = getChiselBodyModelRenderer(variation, definition, world, position));
        transforms.getLegFrontLeftPartDefinition().ifPresent(definition -> quadrupedModel.legFrontLeft = getChiselBodyModelRenderer(variation, definition, world, position));
        transforms.getLegBackRightPartDefinition().ifPresent(definition -> quadrupedModel.legBackRight = getChiselBodyModelRenderer(variation, definition, world, position));
        transforms.getLegBackLeftPartDefinition().ifPresent(definition -> quadrupedModel.legBackLeft = getChiselBodyModelRenderer(variation, definition, world, position));
        return quadrupedModel;
    }

    private ModelRenderer getChiselBodyModelRenderer(ICarvingVariation variation, PartDefinition partDefinition, World world, BlockPos position)
    {
        final BlockState blockState = variation.getBlockState();
        final BlockRendererDispatcher blockRenderer = Minecraft.getInstance().getBlockRendererDispatcher();
        CTMExtendedState chiselState = new CTMExtendedState(blockState, world, position);
        final IBakedModel model = blockRenderer.getModelForState(blockState);


        return getModelRendererForBlockState(partDefinition, chiselState, model);
    }
}