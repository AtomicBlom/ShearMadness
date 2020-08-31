package com.github.atomicblom.shearmadness.api.rendering;

import net.minecraft.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import com.github.atomicblom.shearmadness.api.rendering.vector.Matrix3f;
import com.github.atomicblom.shearmadness.api.rendering.vector.Matrix4f;
import com.github.atomicblom.shearmadness.api.rendering.vector.Vector3f;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * A set of transformations for a quadruped's body parts.
 */
@SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "ProtectedField"})
@OnlyIn(Dist.CLIENT)
public class QuadrupedTransformDefinition
{
    public QuadrupedTransformDefinition()
    {
        bodyPartDefinition = Optional.empty();
        headPartDefinition = Optional.empty();
        leg1PartDefinition = Optional.empty();
        leg2PartDefinition = Optional.empty();
        leg3PartDefinition = Optional.empty();
        leg4PartDefinition = Optional.empty();
        defineParts(null);
    }

    protected static final float NintyDegrees = 3.141592653589793f;

    protected Optional<PartDefinition> bodyPartDefinition = Optional.empty();
    protected Optional<PartDefinition> headPartDefinition = Optional.empty();
    protected Optional<PartDefinition> leg1PartDefinition = Optional.empty();
    protected Optional<PartDefinition> leg2PartDefinition = Optional.empty();
    protected Optional<PartDefinition> leg3PartDefinition = Optional.empty();
    protected Optional<PartDefinition> leg4PartDefinition = Optional.empty();

    public void defineParts(@Nullable LivingEntity entity)
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

    public static Matrix4f createPartMatrix(Vector3f size, Vector3f additionalTranslate)
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

    public Optional<PartDefinition> getLegFrontRightPartDefinition()
    {
        return leg1PartDefinition;
    }

    public Optional<PartDefinition> getLegFrontLeftPartDefinition()
    {
        return leg2PartDefinition;
    }

    public Optional<PartDefinition> getLegBackRightPartDefinition()
    {
        return leg3PartDefinition;
    }

    public Optional<PartDefinition> getLegBackLeftPartDefinition()
    {
        return leg4PartDefinition;
    }
}
