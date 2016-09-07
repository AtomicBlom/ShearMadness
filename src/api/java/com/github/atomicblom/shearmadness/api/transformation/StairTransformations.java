package com.github.atomicblom.shearmadness.api.transformation;

import com.github.atomicblom.shearmadness.api.rendering.PartDefinition;
import com.github.atomicblom.shearmadness.api.rendering.QuadrupedTransformDefinition;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class StairTransformations extends QuadrupedTransformDefinition
{

    @Override
    public void defineParts()
    {
        super.defineParts();
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
        leg = leg1PartDefinition.get();
        leg.setPositionTransform(Matrix4f.mul(leg.getPositionTransform(), matrix, null));
        leg = leg2PartDefinition.get();
        leg.setPositionTransform(Matrix4f.mul(leg.getPositionTransform(), matrix, null));
        leg = leg3PartDefinition.get();
        leg.setPositionTransform(Matrix4f.mul(leg.getPositionTransform(), matrix, null));
        leg = leg4PartDefinition.get();
        leg.setPositionTransform(Matrix4f.mul(leg.getPositionTransform(), matrix, null));
    }
}
