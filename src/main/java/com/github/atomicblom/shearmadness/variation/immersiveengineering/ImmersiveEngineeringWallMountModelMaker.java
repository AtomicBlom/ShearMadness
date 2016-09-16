package com.github.atomicblom.shearmadness.variation.immersiveengineering;

import com.github.atomicblom.shearmadness.api.modelmaker.DefaultModelMaker;
import com.github.atomicblom.shearmadness.api.modelmaker.IModelMaker;
import com.github.atomicblom.shearmadness.api.rendering.EntityMesh;
import com.github.atomicblom.shearmadness.api.rendering.PartDefinition;
import com.github.atomicblom.shearmadness.api.rendering.QuadrupedTransformDefinition;
import net.minecraft.client.model.ModelQuadruped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelSheep1;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by codew on 16/09/2016.
 */
public class ImmersiveEngineeringWallMountModelMaker extends DefaultModelMaker {

    @Override
    public ModelQuadruped createModel(ItemStack itemStack, EntityLivingBase entity)
    {
        ModelQuadruped quadruped = super.createModel(itemStack, entity);

        final float nintyDegrees = (float) Math.toRadians(90);

        quadruped.body = new ModelRenderer(new ModelSheep1(), 0, 0);
        quadruped.head = createModelRenderer(getTransforms().getHeadPartDefinition().get());

        final EntityMesh mesh = new EntityMesh(quadruped.head);
        quadruped.head.cubeList.add(mesh);

        final Matrix4f antenna1Matrix = new Matrix4f();

        antenna1Matrix.translate(new Vector3f(8, 0, -2));
        antenna1Matrix.scale(new Vector3f(20, -20, 20));
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

        antenna2Matrix.translate(new Vector3f(-8, 0, -2));
        antenna2Matrix.scale(new Vector3f(20, -20, 20));
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
}
