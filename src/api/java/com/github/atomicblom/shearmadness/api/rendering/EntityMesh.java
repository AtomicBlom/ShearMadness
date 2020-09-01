package com.github.atomicblom.shearmadness.api.rendering;

import com.google.common.collect.Lists;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.ModelRenderer.ModelBox;
import net.minecraft.client.renderer.model.ModelRenderer.PositionTextureVertex;
import net.minecraft.client.renderer.model.ModelRenderer.TexturedQuad;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import com.github.atomicblom.shearmadness.api.rendering.vector.Matrix3f;
import com.github.atomicblom.shearmadness.api.rendering.vector.Matrix4f;
import com.github.atomicblom.shearmadness.api.rendering.vector.Vector3f;
import com.github.atomicblom.shearmadness.api.rendering.vector.Vector4f;

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
public class EntityMesh extends ModelBox
{
    private final List<FutureQuad<BakedQuad>> allBakedQuads = new ArrayList<>(6);
    private final List<FutureQuad<TexturedQuad>> allTexturedQuads = new ArrayList<>(6);
    private boolean errored;
    private boolean built;

    public EntityMesh()
    {
        super(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, false, 0, 0);
    }

    /**
     * Add a BakedQuad to the mesh. It will be processed in batch when the model is rendered for the first time.
     * @param bakedQuads the list of quads to add to the model
     */
    public void addBakedQuads(Matrix4f positionTransform, Matrix3f textureTransform, Collection<BakedQuad> bakedQuads)
    {
        if (!bakedQuads.isEmpty()) {
            allBakedQuads.add(new FutureQuad<>(bakedQuads, positionTransform, textureTransform));
            built = false;
        }
        apply();
    }

    public void addTexturedQuads(Matrix4f positionTransform, Matrix3f textureTransform, Collection<TexturedQuad> texturedQuads) {
        if (!texturedQuads.isEmpty()) {
            allTexturedQuads.add(new FutureQuad<>(texturedQuads, positionTransform, textureTransform));
            built = false;
        }
        apply();
    }

    public void addTexturedQuads(Matrix4f positionTransform, Matrix3f textureTransform, TexturedQuad... texturedQuads) {
        if (texturedQuads.length > 0) {
            allTexturedQuads.add(new FutureQuad<>(Lists.newArrayList(texturedQuads), positionTransform, textureTransform));
            built = false;
        }
        apply();
    }

    @OnlyIn(Dist.CLIENT)
    public void apply()
    {
        if (!built)
        {
            final List<TexturedQuad> outputQuads = Lists.newArrayList();

            for (final FutureQuad<TexturedQuad> texturedQuads : allTexturedQuads) {
                for (final TexturedQuad texturedQuad : texturedQuads.quads) {
                    final PositionTextureVertex[] newPositions = new PositionTextureVertex[4];
                    for (int i = 0; i < texturedQuad.vertexPositions.length; i++)
                    {
                        final PositionTextureVertex vertexPosition = texturedQuad.vertexPositions[i];
                        @SuppressWarnings("NumericCastThatLosesPrecision")
                        final Vector4f position = new Vector4f(vertexPosition.position.getX(),
                                vertexPosition.position.getY(),
                                vertexPosition.position.getZ(), 1);

                        final Vector3f textureCoords = new Vector3f(
                                vertexPosition.textureU,
                                vertexPosition.textureV,
                                1);

                        final Vector4f transformedPosition = Matrix4f.transform(texturedQuads.positionTransform, position, null);
                        final Vector3f transformedTexture = Matrix3f.transform(texturedQuads.textureTransform, textureCoords, null);

                        newPositions[i] = new PositionTextureVertex(
                                transformedPosition.getX(),
                                transformedPosition.getY(),
                                transformedPosition.getZ(),
                                transformedTexture.getX(),
                                transformedTexture.getY()
                        );
                    }

                    outputQuads.add(TexturedQuadBuilder.TexturedQuad(newPositions));
                }

            }

            for (final FutureQuad<BakedQuad> bakedQuads : allBakedQuads)
            {
                for (final BakedQuad bakedQuad : bakedQuads.quads) {
                    try {
                        final VertexConsumer consumer = new VertexConsumer(DefaultVertexFormats.POSITION_TEX, bakedQuads.positionTransform, bakedQuads.textureTransform);
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

            quads = new TexturedQuad[outputQuads.size()];
            quads = outputQuads.toArray(quads);

            built = true;
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
