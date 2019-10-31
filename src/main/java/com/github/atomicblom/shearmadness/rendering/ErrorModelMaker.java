package com.github.atomicblom.shearmadness.rendering;

import com.github.atomicblom.shearmadness.api.modelmaker.IModelMaker;
import com.github.atomicblom.shearmadness.utility.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.QuadrupedModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.entity.model.SheepWoolModel;
import net.minecraft.client.renderer.model.PositionTextureVertex;
import net.minecraft.client.renderer.model.TexturedQuad;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.ItemStack;
import com.github.atomicblom.shearmadness.api.rendering.EntityMesh;
import com.github.atomicblom.shearmadness.api.rendering.QuadrupedTransformDefinition;
import com.github.atomicblom.shearmadness.api.rendering.vector.*;

class ErrorModelMaker implements IModelMaker {

    private final float fifteenDegrees = (float) Math.toRadians(15);

    private final QuadrupedTransformDefinition transforms;

    private static final RendererModel defaultRenderer = new RendererModel(new SheepWoolModel<>(), 0, 0);

    ErrorModelMaker() {
        transforms = new QuadrupedTransformDefinition();
    }

    @Override
    public QuadrupedModel<SheepEntity> createModel(ItemStack itemStack, LivingEntity entity) {

        transforms.defineParts(entity);
        final QuadrupedModel<SheepEntity> quadrupedModel = new SheepWoolModel<>();

        quadrupedModel.body = createModelRenderer(transforms.getBodyPartDefinition().get());
        quadrupedModel.headModel = createModelRenderer(transforms.getHeadPartDefinition().get());
        quadrupedModel.legBackLeft = defaultRenderer;
        quadrupedModel.legBackRight = defaultRenderer;
        quadrupedModel.legFrontLeft = defaultRenderer;
        quadrupedModel.legFrontRight = defaultRenderer;

        final AtlasTexture blockTextureMap = Minecraft.getInstance().getTextureMap();
        final TextureAtlasSprite errorModelSprite = blockTextureMap.getAtlasSprite(Reference.Textures.BAD_RENDER.toString());

        final EntityMesh bodyMesh = new EntityMesh(quadrupedModel.body);
        final EntityMesh headMesh = new EntityMesh(quadrupedModel.headModel);
        quadrupedModel.body.cubeList.add(bodyMesh);
        quadrupedModel.headModel.cubeList.add(headMesh);

        final Matrix4f bodyTransformRight = new Matrix4f()
                .translate(new Vector3f(-5.0f, -7, -6));
        bodyMesh.addTexturedQuads(bodyTransformRight, new Matrix3f(),
                new TexturedQuad(
                        new PositionTextureVertex[]{
                                new PositionTextureVertex(0, 0, 6, errorModelSprite.getInterpolatedU(9.75), errorModelSprite.getInterpolatedV(0.625)),
                                new PositionTextureVertex(0, 10, 6, errorModelSprite.getInterpolatedU(0), errorModelSprite.getInterpolatedV(0.625)),
                                new PositionTextureVertex(0, 10, 0, errorModelSprite.getInterpolatedU(0), errorModelSprite.getInterpolatedV(3)),
                                new PositionTextureVertex(0, 0, 0, errorModelSprite.getInterpolatedU(9.75), errorModelSprite.getInterpolatedV(3))
                        })
        );

        final Matrix4f bodyTransformLeft = new Matrix4f()
                .translate(new Vector3f(5.0f, -7, -6));
        bodyMesh.addTexturedQuads(bodyTransformLeft, new Matrix3f(),
                new TexturedQuad(
                        new PositionTextureVertex[]{
                                new PositionTextureVertex(0, 0, 6, errorModelSprite.getInterpolatedU(0), errorModelSprite.getInterpolatedV(0.625)),
                                new PositionTextureVertex(0, 10, 6, errorModelSprite.getInterpolatedU(9.75), errorModelSprite.getInterpolatedV(0.625)),
                                new PositionTextureVertex(0, 10, 0, errorModelSprite.getInterpolatedU(9.75), errorModelSprite.getInterpolatedV(3)),
                                new PositionTextureVertex(0, 0, 0, errorModelSprite.getInterpolatedU(0), errorModelSprite.getInterpolatedV(3))
                        })
        );

        final Matrix4f headTransform1 = new Matrix4f()
                .rotate(-fifteenDegrees, new Vector3f(0, 0, 1))
                .translate(new Vector3f(-8, -10, -8));
        headMesh.addTexturedQuads(headTransform1, new Matrix3f(),
                new TexturedQuad(
                        new PositionTextureVertex[]{
                                new PositionTextureVertex(0, 0, 0, errorModelSprite.getInterpolatedU(0), errorModelSprite.getInterpolatedV(5.5)),
                                new PositionTextureVertex(8, 0, 0, errorModelSprite.getInterpolatedU(14.25), errorModelSprite.getInterpolatedV(5.5)),
                                new PositionTextureVertex(8, 4, 0, errorModelSprite.getInterpolatedU(14.25), errorModelSprite.getInterpolatedV(10.875)),
                                new PositionTextureVertex(0, 4, 0, errorModelSprite.getInterpolatedU(0), errorModelSprite.getInterpolatedV(10.875))
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
                                new PositionTextureVertex(0, 0, 0, errorModelSprite.getInterpolatedU(0), errorModelSprite.getInterpolatedV(3.25)),
                                new PositionTextureVertex(8, 0, 0, errorModelSprite.getInterpolatedU(16), errorModelSprite.getInterpolatedV(3.25)),
                                new PositionTextureVertex(8, 3, 0, errorModelSprite.getInterpolatedU(16), errorModelSprite.getInterpolatedV(5.375)),
                                new PositionTextureVertex(0, 3, 0, errorModelSprite.getInterpolatedU(0), errorModelSprite.getInterpolatedV(5.375))
                        })
        );

        final Matrix4f headTransform3 = new Matrix4f()
                .rotate(1.5f * fifteenDegrees, new Vector3f(0, 0, 1))
                .translate(new Vector3f(3, -4, -8));
        headMesh.addTexturedQuads(headTransform3, new Matrix3f(),
                new TexturedQuad(
                        new PositionTextureVertex[]{
                                new PositionTextureVertex(0, 0, 0, errorModelSprite.getInterpolatedU(0), errorModelSprite.getInterpolatedV(11.375)),
                                new PositionTextureVertex(8, 0, 0, errorModelSprite.getInterpolatedU(14.5), errorModelSprite.getInterpolatedV(11.375)),
                                new PositionTextureVertex(8, 3, 0, errorModelSprite.getInterpolatedU(14.5), errorModelSprite.getInterpolatedV(13.75)),
                                new PositionTextureVertex(0, 3, 0, errorModelSprite.getInterpolatedU(0), errorModelSprite.getInterpolatedV(13.75))
                        })
        );

        return quadrupedModel;
    }
}
