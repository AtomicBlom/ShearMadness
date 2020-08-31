package com.github.atomicblom.shearmadness.api.rendering;

import com.google.common.collect.Lists;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A version of ModelBox that converts BakedQuads to TexturedQuads (as used in entities).
 * Add BakedQuads using the addBakedQuads method.
 * Note that TexturedQuads only have Position and UV information, other data will be discarded.
 */
@OnlyIn(Dist.CLIENT)
@ParametersAreNonnullByDefault
public class EntityMesh extends ModelRenderer
{
    private final List<FutureQuad<BakedQuad>> allBakedQuads = new ArrayList<>(6);
    private final List<FutureQuad<ModelRenderer.TexturedQuad>> allTexturedQuads = new ArrayList<>(6);
    private ModelRenderer.TexturedQuad[] quadList = null;
    private boolean errored;

    public EntityMesh(ModelRenderer renderer)
    {
        super(renderer, 0, 0, 0, 0, 0, 0, 0, 0, 0, false);
    }

    /**
     * Add a BakedQuad to the mesh. It will be processed in batch when the model is rendered for the first time.
     * @param bakedQuads the list of quads to add to the model
     */
    public void addBakedQuads(Matrix4f positionTransform, Matrix3f textureTransform, Collection<BakedQuad> bakedQuads)
    {
        if (!bakedQuads.isEmpty()) {
            allBakedQuads.add(new FutureQuad<>(bakedQuads, positionTransform, textureTransform));
        }
    }

    public void addTexturedQuads(Matrix4f positionTransform, Matrix3f textureTransform, Collection<TexturedQuad> texturedQuads) {
        if (!texturedQuads.isEmpty()) {
            allTexturedQuads.add(new FutureQuad<>(texturedQuads, positionTransform, textureTransform));
        }
    }

    public void addTexturedQuads(Matrix4f positionTransform, Matrix3f textureTransform, TexturedQuad... texturedQuads) {
        if (texturedQuads.length > 0) {
            allTexturedQuads.add(new FutureQuad<>(Lists.newArrayList(texturedQuads), positionTransform, textureTransform));
        }
    }

    @SuppressWarnings("ObjectAllocationInLoop")
    @Override
    @OnlyIn(Dist.CLIENT)
    public void render(BufferBuilder renderer, float scale)
    {
        if (quadList == null)
        {
            final List<ModelRenderer.TexturedQuad> outputQuads = Lists.newArrayList();

            for (final FutureQuad<ModelRenderer.TexturedQuad> texturedQuads : allTexturedQuads) {
                for (final ModelRenderer.TexturedQuad texturedQuad : texturedQuads.quads) {
                    final ModelRenderer.PositionTextureVertex[] newPositions = new ModelRenderer.PositionTextureVertex[4];
                    for (int i = 0; i < texturedQuad.vertexPositions.length; i++)
                    {
                        final ModelRenderer.PositionTextureVertex vertexPosition = texturedQuad.vertexPositions[i];
                        @SuppressWarnings("NumericCastThatLosesPrecision")
                        final Vector4f position = new Vector4f((float) vertexPosition.vector3D.x,
                                (float) vertexPosition.vector3D.y,
                                (float) vertexPosition.vector3D.z, 1);

                        final Vector3f textureCoords = new Vector3f(
                                vertexPosition.texturePositionX,
                                vertexPosition.texturePositionY,
                                1);

                        final Vector4f transformedPosition = Matrix4f.transform(texturedQuads.positionTransform, position, null);
                        final Vector3f transformedTexture = Matrix3f.transform(texturedQuads.textureTransform, textureCoords, null);

                        newPositions[i] = new ModelRenderer.PositionTextureVertex(
                                transformedPosition.getX(),
                                transformedPosition.getY(),
                                transformedPosition.getZ(),
                                transformedTexture.getX(),
                                transformedTexture.getY()
                        );
                    }

                    outputQuads.add(new ModelRenderer.PositionTextureVertex(newPositions));
                }

            }

            for (final FutureQuad<BakedQuad> bakedQuads : allBakedQuads)
            {
                for (final BakedQuad bakedQuad : bakedQuads.quads) {
                    try {
                        final VertexConsumer consumer = new VertexConsumer(bakedQuad.getFormat(), bakedQuads.positionTransform, bakedQuads.textureTransform);
                        bakedQuad.pipe(consumer);
                        outputQuads.add(consumer.getOutputQuad());
                    } catch (Exception e) {
                        if (!errored) {
                            LogManager.getLogger("ShearMadnessAPI").log(Level.ERROR, "Error creating chiseled model", e);
                        }
                        errored = true;
                    }
                }
            }

            quadList = new ModelRenderer.PositionTextureVertex[outputQuads.size()];
            quadList = outputQuads.toArray(quadList);
        }

        for (final ModelRenderer.PositionTextureVertex texturedquad : quadList)
        {
            texturedquad.draw(renderer, scale);
        }
    }

    private static class FutureQuad<T> {
        private final Collection<T> quads;
        private final Matrix4f positionTransform;
        private final Matrix3f textureTransform;

        FutureQuad(Collection<T> quads, Matrix4f positionTransform, Matrix3f textureTransform) {
            this.quads = quads;
            this.positionTransform = positionTransform;
            this.textureTransform = textureTransform;
        }
    }

}
