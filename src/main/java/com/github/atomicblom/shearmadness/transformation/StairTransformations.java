package com.github.atomicblom.shearmadness.transformation;

import com.github.atomicblom.shearmadness.api.rendering.QuadrupedTransformDefinition;
import org.lwjgl.util.vector.Matrix;
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
        bodyPartDefinition.setPositionTransform(Matrix4f.mul(bodyPartDefinition.getPositionTransform(), matrix, null));

        matrix = new Matrix4f();
        matrix.translate(new Vector3f(0, 0.2f, 0.2f));

        matrix.rotate(nintyDegrees, new Vector3f(0, 1, 0));
        matrix.rotate(nintyDegrees, new Vector3f(0, 0, 1));
        headPartDefinition.setPositionTransform(Matrix4f.mul(headPartDefinition.getPositionTransform(), matrix, null));

        matrix = new Matrix4f();
        matrix.translate(new Vector3f(0, -0.83f, 1));
        matrix.rotate(nintyDegrees, new Vector3f(0, 1, 0));
        leg1PartDefinition.setPositionTransform(Matrix4f.mul(leg1PartDefinition.getPositionTransform(), matrix, null));
        leg2PartDefinition.setPositionTransform(Matrix4f.mul(leg2PartDefinition.getPositionTransform(), matrix, null));
        leg3PartDefinition.setPositionTransform(Matrix4f.mul(leg3PartDefinition.getPositionTransform(), matrix, null));
        leg4PartDefinition.setPositionTransform(Matrix4f.mul(leg4PartDefinition.getPositionTransform(), matrix, null));
    }
}
