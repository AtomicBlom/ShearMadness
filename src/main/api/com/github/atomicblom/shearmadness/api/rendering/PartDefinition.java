package com.github.atomicblom.shearmadness.api.rendering;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 * Defines a set of transforms for a part of the quadruped.
 * texture transformation is not currently implemented.
 */
@SideOnly(Side.CLIENT)
public class PartDefinition
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

    public Vector3f getRotationPoint()
    {
        return rotationPoint;
    }

    public Matrix4f getPositionTransform()
    {
        return positionTransform;
    }

    public Matrix3f getTextureTransform()
    {
        return textureTransform;
    }
}
