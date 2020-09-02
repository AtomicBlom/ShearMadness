package com.github.atomicblom.shearmadness.api.transformation;

import com.github.atomicblom.shearmadness.api.rendering.PartDefinition;
import com.github.atomicblom.shearmadness.api.rendering.QuadrupedTransformDefinition;
import com.github.atomicblom.shearmadness.api.rendering.vector.Matrix4f;
import com.github.atomicblom.shearmadness.api.rendering.vector.Vector3f;
import net.minecraft.entity.LivingEntity;

public class StairTransformations extends QuadrupedTransformDefinition
{

    @Override
    public void defineParts(LivingEntity entity)
    {
        super.defineParts(entity);
        final float nintyDegrees = (float) Math.toRadians(90);

        Matrix4f matrix = new Matrix4f();
        matrix.translate(new Vector3f(1, 0, 0));
        matrix.rotate((float)Math.toRadians(-90), new Vector3f(0, 1, 0));
        final PartDefinition body = bodyPartDefinition.get();
        body.setPositionTransform(Matrix4f.mul(body.getPositionTransform(), matrix, null));

        matrix = new Matrix4f();
        matrix.translate(new Vector3f(0, 0.2f, 0.2f));

        matrix.rotate(nintyDegrees, new Vector3f(0, 1, 0));
        matrix.rotate(nintyDegrees, new Vector3f(0, 0, 1));
        final PartDefinition head = headPartDefinition.get();
        head.setPositionTransform(Matrix4f.mul(head.getPositionTransform(), matrix, null));

        matrix = new Matrix4f();
        matrix.translate(new Vector3f(0, -0.83f, 1));
        matrix.rotate(nintyDegrees, new Vector3f(0, 1, 0));
        PartDefinition leg;
        leg = frontRightLegPartDefinition.get();
        leg.setPositionTransform(Matrix4f.mul(leg.getPositionTransform(), matrix, null));
        leg = frontLeftLegPartDefinition.get();
        leg.setPositionTransform(Matrix4f.mul(leg.getPositionTransform(), matrix, null));
        leg = rearRightLegPartDefinition.get();
        leg.setPositionTransform(Matrix4f.mul(leg.getPositionTransform(), matrix, null));
        leg = rearLeftLegPartDefinition.get();
        leg.setPositionTransform(Matrix4f.mul(leg.getPositionTransform(), matrix, null));
    }
}
