package com.github.atomicblom.shearmadness.api.rendering;

import net.minecraft.client.renderer.model.ModelRenderer.PositionTextureVertex;
import net.minecraft.client.renderer.model.ModelRenderer.TexturedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;
import com.github.atomicblom.shearmadness.api.rendering.vector.*;

import javax.annotation.Nullable;

/**
 * Pipes a BakedQuad into a TexturedQuad
 */
@OnlyIn(Dist.CLIENT)
class VertexConsumer implements IVertexConsumer
{

    private final VertexFormat vertexFormat;
    private final Matrix4f positionTransform;
    private final Matrix3f textureTransform;
    private final PositionTextureVertex[] vertices = new PositionTextureVertex[4];

    private int currentVertexIndex = -1;
    @Nullable
    private net.minecraft.util.math.vector.Vector3f currentPosition = null;
    @Nullable
    private net.minecraft.util.math.vector.Vector3f currentTexture = null;

    VertexConsumer(VertexFormat vertexFormat, Matrix4f positionTransform, Matrix3f textureTransform)
    {
        this.vertexFormat = vertexFormat;
        this.positionTransform = positionTransform;
        this.textureTransform = textureTransform;
    }

    TexturedQuad getOutputQuad()
    {
        return new TexturedQuad(vertices);
    }

    @Override
    public VertexFormat getVertexFormat()
    {
        return vertexFormat;
    }

    @Override
    public void setQuadTint(int tint)
    {

    }

    @Override
    public void setQuadOrientation(Direction orientation)
    {

    }

    @Override
    public void setApplyDiffuseLighting(boolean diffuse)
    {

    }

    @Override
    public void setTexture(TextureAtlasSprite texture)
    {

    }

    @Override
    public void put(int element, float... data)
    {
        if (element == 0)
        {
            ++currentVertexIndex;
        }

        final VertexFormatElement element1 = vertexFormat.getElements().get(element);
        switch (element1.getUsage())
        {
            case POSITION:
                if (data.length < 3)
                {
                    currentPosition = null;
                    return;
                }
                final Vector4f position = new Vector4f(data[0], data[1], data[2], 1);
                final Vector4f transform = Matrix4f.transform(positionTransform, position, null);
                currentPosition = new net.minecraft.util.math.vector.Vector3f(transform.x, transform.y, transform.z);

                break;
            case UV:
                if (data.length < 2)
                {
                    currentTexture = null;
                    return;
                }

                final Vector3f uvs = new Vector3f(data[0], data[1], 1);
                final Vector3f transformedTexture = Matrix3f.transform(textureTransform, uvs, null);
                currentTexture = new net.minecraft.util.math.vector.Vector3f(transformedTexture.x, transformedTexture.y, transformedTexture.z);
                break;
            default:
                break;
        }

        if (element == vertexFormat.getElements().size() - 1)
        {
            if (currentPosition == null || currentTexture == null) {
                String vertexInfo = "";
                for (final VertexFormatElement vertexFormatElement : vertexFormat.getElements()) {
                    vertexInfo += String.format("Element %d - %s\n", vertexFormatElement.getIndex(), vertexFormatElement.getUsage());
                }

                throw new RuntimeException("Unexpected Vertex Format:\n" + vertexInfo);
            }

            vertices[currentVertexIndex] = new PositionTextureVertex(currentPosition, currentTexture.getX(), currentTexture.getY());
            currentPosition = null;
            currentTexture = null;
        }
    }
}
