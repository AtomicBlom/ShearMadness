package com.github.atomicblom.shearmadness.api.rendering;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 * A set of transformations for a quadruped's body parts.
 */
@SideOnly(Side.CLIENT)
public class QuadrupedTransformDefinition
{
    protected static final float NintyDegrees = 3.141592653589793f;

    protected PartDefinition bodyPartDefinition;
    protected PartDefinition headPartDefinition;
    protected PartDefinition leg1PartDefinition;
    protected PartDefinition leg2PartDefinition;
    protected PartDefinition leg3PartDefinition;
    protected PartDefinition leg4PartDefinition;

    public QuadrupedTransformDefinition()
    {
        defineParts();
    }

    public void defineParts()
    {
        final Matrix4f rotate = new Matrix4f().rotate((float) Math.toRadians(-90), new Vector3f(1, 0, 0));
        bodyPartDefinition = new PartDefinition(
                new Vector3f(0.0f, 5.0f, 2.0f),
                Matrix4f.mul(
                        createPartMatrix(
                                new Vector3f(12, 20, 10),
                                new Vector3f(0, -2, -14)), rotate, null),
                new Matrix3f()
        );

        headPartDefinition = new PartDefinition(
                new Vector3f(0.0f, 6.0f, -8.0f),
                createPartMatrix(
                        new Vector3f(8, 8, 8),
                        new Vector3f(0, -1, -1)),
                new Matrix3f()
        );

        leg1PartDefinition = new PartDefinition(
                new Vector3f(-3.0f, 12.0f, 7.0f),
                createPartMatrix(
                        new Vector3f(5.6f, 7.4f, 5.6f),
                        new Vector3f(0, 3, 0.1f)),
                new Matrix3f()
        );

        leg2PartDefinition = new PartDefinition(
                new Vector3f(3.0f, 12.0f, 7.0f),
                createPartMatrix(
                        new Vector3f(5.6f, 7.4f, 5.6f),
                        new Vector3f(0, 3, 0.1f)),
                new Matrix3f()
        );

        leg3PartDefinition = new PartDefinition(
                new Vector3f(-3.0f, 12.0f, -5.0f),
                createPartMatrix(
                        new Vector3f(5.6f, 7.4f, 5.6f),
                        new Vector3f(0, 3, 0.1f)),
                new Matrix3f()
        );

        leg4PartDefinition = new PartDefinition(
                new Vector3f(3.0f, 12.0f, -5.0f),
                createPartMatrix(
                        new Vector3f(5.6f, 7.4f, 5.6f),
                        new Vector3f(0, 3, 0.1f)),
                new Matrix3f()
        );
    }

    protected Matrix4f createPartMatrix(Vector3f size, Vector3f additionalTranslate)
    {
        final Vector3f adjustedSize = size.translate(-0.5f, -0.5f, -0.5f);
        final Matrix4f matrix = new Matrix4f();

        matrix.rotate(NintyDegrees, new Vector3f(1, 0, 0));
        matrix.translate(
                (Vector3f) Vector3f.add(
                        (Vector3f) new Vector3f(adjustedSize).scale(0.5f),
                        additionalTranslate,
                        null
                ).negate());
        matrix.scale(adjustedSize);
        return matrix;
    }

    public PartDefinition getBodyPartDefinition()
    {
        return bodyPartDefinition;
    }

    public PartDefinition getHeadPartDefinition()
    {
        return headPartDefinition;
    }

    public PartDefinition getLeg1PartDefinition()
    {
        return leg1PartDefinition;
    }

    public PartDefinition getLeg2PartDefinition()
    {
        return leg2PartDefinition;
    }

    public PartDefinition getLeg3PartDefinition()
    {
        return leg3PartDefinition;
    }

    public PartDefinition getLeg4PartDefinition()
    {
        return leg4PartDefinition;
    }
}
