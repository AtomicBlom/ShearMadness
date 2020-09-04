package com.github.atomicblom.shearmadness.variations.immersiveengineering.visuals;

import com.github.atomicblom.shearmadness.api.modelmaker.DefaultModelMaker;
import com.github.atomicblom.shearmadness.api.rendering.EntityMesh;
import com.github.atomicblom.shearmadness.api.rendering.PartDefinition;
import com.github.atomicblom.shearmadness.api.rendering.vector.Matrix3f;
import com.github.atomicblom.shearmadness.api.rendering.vector.Matrix4f;
import com.github.atomicblom.shearmadness.api.rendering.vector.Vector3f;
import net.minecraft.client.renderer.entity.model.QuadrupedModel;
import net.minecraft.client.renderer.entity.model.SheepWoolModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.SpriteMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.ItemStack;

/**
 * Created by codew on 16/09/2016.
 */
public class ImmersiveEngineeringWallMountModelMaker extends DefaultModelMaker {

    @Override
    public QuadrupedModel<SheepEntity> createModel(ItemStack itemStack, LivingEntity entity, SpriteMap spriteMap)
    {
        QuadrupedModel<SheepEntity> quadruped = super.createModel(itemStack, entity, spriteMap);

        final float nintyDegrees = (float) Math.toRadians(90);
        final float oneEightyDegrees = (float) Math.toRadians(180);

        quadruped.body = new ModelRenderer(new SheepWoolModel<>(), 0, 0);
        quadruped.headModel = createModelRenderer(getTransforms().getHeadPartDefinition().get());

        final EntityMesh mesh = new EntityMesh();
        quadruped.headModel.cubeList.add(mesh);

        final Matrix4f antenna1Matrix = new Matrix4f();

        antenna1Matrix.translate(new Vector3f(5.8f, 0, -2));
        antenna1Matrix.scale(new Vector3f(6, 6, 6));
        antenna1Matrix.rotate(-oneEightyDegrees, new Vector3f(1, 0, 0));
        antenna1Matrix.rotate(-nintyDegrees, new Vector3f(0, 1, 0));
        antenna1Matrix.translate(new Vector3f(-0.5f, -0.5f, -0.5f));

        final IBakedModel bakedModelForItem = getBakedModelForItem(itemStack, entity);
        addBlockModelToEntityMesh(mesh,
                new PartDefinition(
                        new Vector3f(0, 0, 0),
                        antenna1Matrix,
                        new Matrix3f()),
                null,
                bakedModelForItem
        );

        final Matrix4f antenna2Matrix = new Matrix4f();

        antenna2Matrix.translate(new Vector3f(-5.8f, 0, -2));
        antenna2Matrix.scale(new Vector3f(6, 6, 6));
        antenna2Matrix.rotate(-oneEightyDegrees, new Vector3f(1, 0, 0));
        antenna2Matrix.rotate(nintyDegrees, new Vector3f(0, 1, 0));
        antenna2Matrix.translate(new Vector3f(-0.5f, -0.5f, -0.5f));

        addBlockModelToEntityMesh(mesh,
                new PartDefinition(
                        new Vector3f(0, 0, 0),
                        antenna2Matrix,
                        new Matrix3f()),
                null,
                bakedModelForItem
        );

        return quadruped;
    }

    @Override
    public boolean shouldRenderWool(ItemStack itemStack) {
        return true;
    }
}