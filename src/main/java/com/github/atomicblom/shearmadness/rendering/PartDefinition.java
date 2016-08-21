package com.github.atomicblom.shearmadness.rendering;

import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

class PartDefinition
{
    private final Vector3f rotationPoint;
    private final Matrix4f positionTransform;
    private final Matrix3f textureTransform;

    PartDefinition(Vector3f rotationPoint, Matrix4f positionTransform, Matrix3f textureTransform)
    {

        this.rotationPoint = rotationPoint;
        this.positionTransform = positionTransform;
        this.textureTransform = textureTransform;
    }

    Vector3f getRotationPoint()
    {
        return rotationPoint;
    }

    Matrix4f getPositionTransform()
    {
        return positionTransform;
    }

    Matrix3f getTextureTransform()
    {
        return textureTransform;
    }
}
