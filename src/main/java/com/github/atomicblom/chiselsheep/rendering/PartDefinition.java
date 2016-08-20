package com.github.atomicblom.chiselsheep.rendering;

import com.sun.javafx.geom.Vec3f;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;

/**
 * Created by codew on 19/08/2016.
 */
class PartDefinition {
    final Vec3f rotationPoint;
    final Matrix4f positionTransform;
    final Matrix3f textureTransform;

    PartDefinition(Vec3f rotationPoint, Matrix4f positionTransform, Matrix3f textureTransform) {

        this.rotationPoint = rotationPoint;
        this.positionTransform = positionTransform;
        this.textureTransform = textureTransform;
    }
}
