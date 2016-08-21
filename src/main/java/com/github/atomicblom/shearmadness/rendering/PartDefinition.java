package com.github.atomicblom.shearmadness.rendering;

import com.sun.javafx.geom.Vec3f;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;

class PartDefinition
{
    private final Vec3f rotationPoint;
    private final Matrix4f positionTransform;
    private final Matrix3f textureTransform;

    PartDefinition(Vec3f rotationPoint, Matrix4f positionTransform, Matrix3f textureTransform)
    {

        this.rotationPoint = rotationPoint;
        this.positionTransform = positionTransform;
        this.textureTransform = textureTransform;
    }

    Vec3f getRotationPoint()
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
