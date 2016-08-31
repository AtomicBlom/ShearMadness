package com.github.atomicblom.shearmadness.api.rendering;

import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A version of ModelBox that converts BakedQuads to TexturedQuads (as used in entities).
 * Add BakedQuads using the addBakedQuads method.
 * Note that TexturedQuads only have Position and UV information, other data will be discarded.
 */
@SideOnly(Side.CLIENT)
public class EntityMesh extends ModelBox
{
    private final Matrix4f positionTransform;
    private final Matrix3f textureTransform;
    private final List<BakedQuad> allBakedQuads = new ArrayList<>(6);
    private final List<TexturedQuad> allTexturedQuads = new ArrayList<>(6);
    private TexturedQuad[] quadList = null;

    public EntityMesh(ModelRenderer renderer, Matrix4f positionTransform, Matrix3f textureTransform)
    {
        super(renderer, 0, 0, 0, 0, 0, 0, 0, 0, 0, false);
        this.positionTransform = positionTransform;
        this.textureTransform = textureTransform;
    }

    /**
     * Add a BakedQuad to the mesh. It will be processed in batch when the model is rendered for the first time.
     * @param bakedQuads the list of quads to add to the model
     */
    public void addBakedQuads(Collection<BakedQuad> bakedQuads)
    {
        allBakedQuads.addAll(bakedQuads);
    }

    public void addTexturedQuads(Collection<TexturedQuad> texturedQuads) {
        allTexturedQuads.addAll(texturedQuads);
    }
    public void addTexturedQuads(TexturedQuad... texturedQuads) {
        for (final TexturedQuad texturedQuad : texturedQuads)
        {
            allTexturedQuads.add(texturedQuad);
        }
    }

    public void addTexturedQuad(TexturedQuad texturedQuad) {
        allTexturedQuads.add(texturedQuad);
    }

    @SuppressWarnings("ObjectAllocationInLoop")
    @Override
    @SideOnly(Side.CLIENT)
    public void render(VertexBuffer renderer, float scale)
    {
        if (quadList == null)
        {

            final List<TexturedQuad> outputQuads = new ArrayList<>();

            for (final TexturedQuad texturedQuad : allTexturedQuads) {
                PositionTextureVertex[] newPositions = new PositionTextureVertex[4];
                for (int i = 0; i < texturedQuad.vertexPositions.length; i++)
                {
                    final PositionTextureVertex vertexPosition = texturedQuad.vertexPositions[i];
                    final Vector4f position = new Vector4f((float) vertexPosition.vector3D.xCoord,
                            (float) vertexPosition.vector3D.yCoord,
                            (float) vertexPosition.vector3D.zCoord, 1);

                    final Vector3f textureCoords = new Vector3f(
                            vertexPosition.texturePositionX,
                            vertexPosition.texturePositionY,
                            1);

                    final Vector4f transformedPosition = Matrix4f.transform(positionTransform, position, null);
                    final Vector3f transformedTexture = Matrix3f.transform(textureTransform, textureCoords, null);

                    newPositions[i] = new PositionTextureVertex(
                            transformedPosition.getX(),
                            transformedPosition.getY(),
                            transformedPosition.getZ(),
                            transformedTexture.getX(),
                            transformedTexture.getY()
                    );
                }

                outputQuads.add(new TexturedQuad(newPositions));
            }

            for (final BakedQuad bakedQuad : allBakedQuads)
            {
                final VertexConsumer consumer = new VertexConsumer(renderer.getVertexFormat(), positionTransform, textureTransform);
                bakedQuad.pipe(consumer);
                outputQuads.add(consumer.getOutputQuad());
            }

            quadList = new TexturedQuad[outputQuads.size()];
            quadList = outputQuads.toArray(quadList);
        }

        for (final TexturedQuad texturedquad : quadList)
        {
            texturedquad.draw(renderer, scale);
        }
    }

}
