package com.github.atomicblom.shearmadness.api.rendering;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import java.util.Optional;

/**
 * A set of transformations for a quadruped's body parts.
 */
@SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "ProtectedField"})
@SideOnly(Side.CLIENT)
public class QuadrupedTransformDefinition
{
    public QuadrupedTransformDefinition()
    {
        defineParts();
    }

    protected static final float NintyDegrees = 3.141592653589793f;

    protected Optional<PartDefinition> bodyPartDefinition = Optional.empty();
    protected Optional<PartDefinition> headPartDefinition = Optional.empty();
    protected Optional<PartDefinition> leg1PartDefinition = Optional.empty();
    protected Optional<PartDefinition> leg2PartDefinition = Optional.empty();
    protected Optional<PartDefinition> leg3PartDefinition = Optional.empty();
    protected Optional<PartDefinition> leg4PartDefinition = Optional.empty();

    public void defineParts()
    {
        final Matrix4f rotate = new Matrix4f().rotate((float) Math.toRadians(-90), new Vector3f(1, 0, 0));
        bodyPartDefinition = Optional.of(new PartDefinition(
                new Vector3f(0.0f, 5.0f, 2.0f),
                Matrix4f.mul(
                        createPartMatrix(
                                new Vector3f(12, 20, 10),
                                new Vector3f(0, -2, -14)), rotate, null),
                new Matrix3f()
        ));

        headPartDefinition = Optional.of(new PartDefinition(
                new Vector3f(0.0f, 6.0f, -8.0f),
                createPartMatrix(
                        new Vector3f(8, 8, 8),
                        new Vector3f(0, -1, -1)),
                new Matrix3f()
        ));

        leg1PartDefinition = Optional.of(new PartDefinition(
                new Vector3f(-3.0f, 12.0f, 7.0f),
                createPartMatrix(
                        new Vector3f(5.6f, 7.4f, 5.6f),
                        new Vector3f(0, 3, 0.1f)),
                new Matrix3f()
        ));

        leg2PartDefinition = Optional.of(new PartDefinition(
                new Vector3f(3.0f, 12.0f, 7.0f),
                createPartMatrix(
                        new Vector3f(5.6f, 7.4f, 5.6f),
                        new Vector3f(0, 3, 0.1f)),
                new Matrix3f()
        ));

        leg3PartDefinition = Optional.of(new PartDefinition(
                new Vector3f(-3.0f, 12.0f, -5.0f),
                createPartMatrix(
                        new Vector3f(5.6f, 7.4f, 5.6f),
                        new Vector3f(0, 3, 0.1f)),
                new Matrix3f()
        ));

        leg4PartDefinition = Optional.of(new PartDefinition(
                new Vector3f(3.0f, 12.0f, -5.0f),
                createPartMatrix(
                        new Vector3f(5.6f, 7.4f, 5.6f),
                        new Vector3f(0, 3, 0.1f)),
                new Matrix3f()
        ));
    }

    protected static Matrix4f createPartMatrix(Vector3f size, Vector3f additionalTranslate)
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

    public Optional<PartDefinition> getBodyPartDefinition()
    {
        return bodyPartDefinition;
    }

    public Optional<PartDefinition> getHeadPartDefinition()
    {
        return headPartDefinition;
    }

    public Optional<PartDefinition> getLeg1PartDefinition()
    {
        return leg1PartDefinition;
    }

    public Optional<PartDefinition> getLeg2PartDefinition()
    {
        return leg2PartDefinition;
    }

    public Optional<PartDefinition> getLeg3PartDefinition()
    {
        return leg3PartDefinition;
    }

    public Optional<PartDefinition> getLeg4PartDefinition()
    {
        return leg4PartDefinition;
    }
}
