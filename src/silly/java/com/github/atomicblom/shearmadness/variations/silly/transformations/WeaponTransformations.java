package com.github.atomicblom.shearmadness.variations.silly.transformations;

import com.github.atomicblom.shearmadness.api.rendering.PartDefinition;
import com.github.atomicblom.shearmadness.api.rendering.QuadrupedTransformDefinition;
import com.github.atomicblom.shearmadness.api.rendering.vector.Matrix3f;
import com.github.atomicblom.shearmadness.api.rendering.vector.Matrix4f;
import com.github.atomicblom.shearmadness.api.rendering.vector.Vector3f;
import net.minecraft.entity.LivingEntity;

import java.util.Optional;
import java.util.Random;

public class WeaponTransformations extends QuadrupedTransformDefinition
{
    @Override
    public void defineParts(LivingEntity entity)
    {
        int seed = 45345439;
        if (entity != null) {
            seed = entity.getUniqueID().hashCode();
        }

        Random r = new Random(seed);

        if (r.nextBoolean()) {
            headPartDefinition = Optional.of(new PartDefinition(
                    new Vector3f(0.0f, 6.0f, -8.0f),
                    Matrix4f.mul(
                            new Matrix4f().rotate((float) Math.toRadians(90), new Vector3f(1, 0, 0)),
                            Matrix4f.mul(
                                    new Matrix4f().translate(new Vector3f(0f, -10f, -1f)).rotate((float) Math.toRadians(-45), new Vector3f(0, 0, 1)),
                                    createPartMatrix(
                                            new Vector3f(8, 8, 8),
                                            new Vector3f(0, -0, 0f))
                                    , null
                            ),
                            null

                    ),
                    new Matrix3f()
            ));
        } else {
            headPartDefinition = Optional.of(new PartDefinition(
                    new Vector3f(0.0f, 6.0f, -8.0f),
                    Matrix4f.mul(
                            new Matrix4f().rotate((float) Math.toRadians(90), new Vector3f(1, 0, 0)),
                            Matrix4f.mul(
                                    new Matrix4f().translate(new Vector3f(1.5f, -7.5f, -1f)).rotate((float) Math.toRadians(30), new Vector3f(0, 0, 1)),
                                    createPartMatrix(
                                            new Vector3f(8, 8, 8),
                                            new Vector3f(0, -0, 0f))
                                    , null
                            ),
                            null

                    ),
                    new Matrix3f()
            ));
        }
    }
}
