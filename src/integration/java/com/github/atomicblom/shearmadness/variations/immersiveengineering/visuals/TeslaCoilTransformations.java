package com.github.atomicblom.shearmadness.variations.immersiveengineering.visuals;

import com.github.atomicblom.shearmadness.api.rendering.PartDefinition;
import com.github.atomicblom.shearmadness.api.rendering.QuadrupedTransformDefinition;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Created by codew on 16/09/2016.
 */
public class TeslaCoilTransformations extends QuadrupedTransformDefinition {
    private final Supplier<Integer> offsetY;

    public TeslaCoilTransformations(Supplier<Integer> offsetY) {
        this.offsetY = offsetY;
    }

    @Override
    public void defineParts() {
        final Matrix4f rotate = new Matrix4f().rotate((float) Math.toRadians(-90), new Vector3f(1, 0, 0));
        bodyPartDefinition = Optional.of(new PartDefinition(
                new Vector3f(0.0f, 5.0f, 2.0f),
                Matrix4f.mul(
                        createPartMatrix(
                                new Vector3f(32, 32, 32),
                                new Vector3f(0, -1, offsetY == null ? 0 : offsetY.get())), rotate, null),
                new Matrix3f()
        ));
    }
}
