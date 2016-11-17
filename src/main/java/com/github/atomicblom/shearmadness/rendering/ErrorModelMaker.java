package com.github.atomicblom.shearmadness.rendering;

import com.github.atomicblom.shearmadness.api.modelmaker.IModelMaker;
import com.github.atomicblom.shearmadness.api.rendering.EntityMesh;
import com.github.atomicblom.shearmadness.api.rendering.QuadrupedTransformDefinition;
import com.github.atomicblom.shearmadness.variations.CommonReference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

class ErrorModelMaker implements IModelMaker {

    private final float fifteenDegrees = (float) Math.toRadians(15);

    private final QuadrupedTransformDefinition transforms;

    private static final ModelRenderer defaultRenderer = new ModelRenderer(new ModelSheep1(), 0, 0);

    ErrorModelMaker() {
        transforms = new QuadrupedTransformDefinition();
    }

    @Override
    public ModelQuadruped createModel(ItemStack itemStack, EntityLivingBase entity) {

        transforms.defineParts();
        final ModelQuadruped quadrupedModel = new ModelSheep1();

        quadrupedModel.body = createModelRenderer(transforms.getBodyPartDefinition().get());
        quadrupedModel.head = createModelRenderer(transforms.getHeadPartDefinition().get());
        quadrupedModel.leg1 = defaultRenderer;
        quadrupedModel.leg2 = defaultRenderer;
        quadrupedModel.leg3 = defaultRenderer;
        quadrupedModel.leg4 = defaultRenderer;

        final TextureMap blockTextureMap = Minecraft.getMinecraft().getTextureMapBlocks();
        final TextureAtlasSprite chickenSprite = blockTextureMap.getAtlasSprite(CommonReference.MOD_ID + ":bad_render");

        final EntityMesh bodyMesh = new EntityMesh(quadrupedModel.body);
        final EntityMesh headMesh = new EntityMesh(quadrupedModel.head);
        quadrupedModel.body.cubeList.add(bodyMesh);
        quadrupedModel.head.cubeList.add(headMesh);

        final Matrix4f bodyTransformRight = new Matrix4f()
                .translate(new Vector3f(-5.0f, -7, -6));
        bodyMesh.addTexturedQuads(bodyTransformRight, new Matrix3f(),
                new TexturedQuad(
                        new PositionTextureVertex[]{
                                new PositionTextureVertex(0, 0, 6, chickenSprite.getInterpolatedU(9.75), chickenSprite.getInterpolatedV(0.625)),
                                new PositionTextureVertex(0, 10, 6, chickenSprite.getInterpolatedU(0), chickenSprite.getInterpolatedV(0.625)),
                                new PositionTextureVertex(0, 10, 0, chickenSprite.getInterpolatedU(0), chickenSprite.getInterpolatedV(3)),
                                new PositionTextureVertex(0, 0, 0, chickenSprite.getInterpolatedU(9.75), chickenSprite.getInterpolatedV(3))
                        })
        );

        final Matrix4f bodyTransformLeft = new Matrix4f()
                .translate(new Vector3f(5.0f, -7, -6));
        bodyMesh.addTexturedQuads(bodyTransformLeft, new Matrix3f(),
                new TexturedQuad(
                        new PositionTextureVertex[]{
                                new PositionTextureVertex(0, 0, 6, chickenSprite.getInterpolatedU(0), chickenSprite.getInterpolatedV(0.625)),
                                new PositionTextureVertex(0, 10, 6, chickenSprite.getInterpolatedU(9.75), chickenSprite.getInterpolatedV(0.625)),
                                new PositionTextureVertex(0, 10, 0, chickenSprite.getInterpolatedU(9.75), chickenSprite.getInterpolatedV(3)),
                                new PositionTextureVertex(0, 0, 0, chickenSprite.getInterpolatedU(0), chickenSprite.getInterpolatedV(3))
                        })
        );

        final Matrix4f headTransform1 = new Matrix4f()
                .rotate(-fifteenDegrees, new Vector3f(0, 0, 1))
                .translate(new Vector3f(-8, -10, -8));
        headMesh.addTexturedQuads(headTransform1, new Matrix3f(),
                new TexturedQuad(
                        new PositionTextureVertex[]{
                                new PositionTextureVertex(0, 0, 0, chickenSprite.getInterpolatedU(0), chickenSprite.getInterpolatedV(5.5)),
                                new PositionTextureVertex(8, 0, 0, chickenSprite.getInterpolatedU(14.25), chickenSprite.getInterpolatedV(5.5)),
                                new PositionTextureVertex(8, 4, 0, chickenSprite.getInterpolatedU(14.25), chickenSprite.getInterpolatedV(10.875)),
                                new PositionTextureVertex(0, 4, 0, chickenSprite.getInterpolatedU(0), chickenSprite.getInterpolatedV(10.875))
                        })
        );

        final Matrix4f headTransform2 = new Matrix4f()
                .rotate(-3f * fifteenDegrees, new Vector3f(0, 1, 0))
                .rotate(-0.5f * fifteenDegrees, new Vector3f(1, 0, 0))
                .rotate(1.5f * fifteenDegrees, new Vector3f(0, 0, 1))
                .translate(new Vector3f(-3, -8, -8));
        headMesh.addTexturedQuads(headTransform2, new Matrix3f(),
                new TexturedQuad(
                        new PositionTextureVertex[]{
                                new PositionTextureVertex(0, 0, 0, chickenSprite.getInterpolatedU(0), chickenSprite.getInterpolatedV(3.25)),
                                new PositionTextureVertex(8, 0, 0, chickenSprite.getInterpolatedU(16), chickenSprite.getInterpolatedV(3.25)),
                                new PositionTextureVertex(8, 3, 0, chickenSprite.getInterpolatedU(16), chickenSprite.getInterpolatedV(5.375)),
                                new PositionTextureVertex(0, 3, 0, chickenSprite.getInterpolatedU(0), chickenSprite.getInterpolatedV(5.375))
                        })
        );

        final Matrix4f headTransform3 = new Matrix4f()
                .rotate(1.5f * fifteenDegrees, new Vector3f(0, 0, 1))
                .translate(new Vector3f(3, -4, -8));
        headMesh.addTexturedQuads(headTransform3, new Matrix3f(),
                new TexturedQuad(
                        new PositionTextureVertex[]{
                                new PositionTextureVertex(0, 0, 0, chickenSprite.getInterpolatedU(0), chickenSprite.getInterpolatedV(11.375)),
                                new PositionTextureVertex(8, 0, 0, chickenSprite.getInterpolatedU(14.5), chickenSprite.getInterpolatedV(11.375)),
                                new PositionTextureVertex(8, 3, 0, chickenSprite.getInterpolatedU(14.5), chickenSprite.getInterpolatedV(13.75)),
                                new PositionTextureVertex(0, 3, 0, chickenSprite.getInterpolatedU(0), chickenSprite.getInterpolatedV(13.75))
                        })
        );

        return quadrupedModel;
    }
}
