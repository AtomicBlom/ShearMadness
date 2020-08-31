package com.github.atomicblom.shearmadness.variations.silly.visuals;

import com.github.atomicblom.shearmadness.api.CommonReference;
import com.github.atomicblom.shearmadness.api.modelmaker.IModelMaker;
import com.github.atomicblom.shearmadness.api.rendering.EntityMesh;
import com.github.atomicblom.shearmadness.api.rendering.PartDefinition;
import com.github.atomicblom.shearmadness.api.rendering.vector.Matrix4f;
import com.github.atomicblom.shearmadness.api.rendering.vector.Vector3f;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.QuadrupedModel;
import net.minecraft.client.renderer.entity.model.SheepWoolModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.ItemStack;

public class TestModelMaker implements IModelMaker {
    @Override
    public QuadrupedModel<SheepEntity> createModel(ItemStack itemStack, LivingEntity entity) {
        final QuadrupedModel<SheepEntity> model = new SheepWoolModel<>();

        final Matrix4f partATransform = new Matrix4f().translate(new Vector3f(-2, -1, -8));
        EntityMesh headMesh = new EntityMesh();

        final AtlasTexture blockTextureMap = Minecraft.getInstance().getTextureMap();
        final TextureAtlasSprite chickenSprite = blockTextureMap.getAtlasSprite(CommonReference.MOD_ID + ":chicken_nuggets");

        return model;
    }

    public ModelRenderer getModelRendererForBlockState(PartDefinition partDefinition, BlockState blockState, IBakedModel model, String partName) {
        return new ModelRenderer(new SheepWoolModel<>(), 0, 0);
    }
}
