package com.github.atomicblom.shearmadness.rendering;

import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

class VertexConsumer implements IVertexConsumer
{

    private final VertexFormat vertexFormat;
    private final Matrix4f positionTransform;
    private final Matrix3f textureTransform;
    private final PositionTextureVertex[] vertices = new PositionTextureVertex[4];

    private int currentVertexIndex = -1;
    private Vec3d currentPosition = null;
    private Vector3f currentTexture = null;

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
    public void setQuadOrientation(EnumFacing orientation)
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

        final VertexFormatElement element1 = vertexFormat.getElement(element);
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
                currentPosition = new Vec3d(transform.x, transform.y, transform.z);

                break;
            case UV:
                if (data.length < 2)
                {
                    currentTexture = null;
                    return;
                }

                final Vector3f uvs = new Vector3f(data[0], data[1], 1);
                final Vector3f transformedTexture = Matrix3f.transform(textureTransform, uvs, null);
                currentTexture = new Vector3f(transformedTexture.x, transformedTexture.y, transformedTexture.z);
                break;
            default:
                break;
        }

        if (element == vertexFormat.getElementCount() - 1)
        {
            vertices[currentVertexIndex] = new PositionTextureVertex(currentPosition, currentTexture.x, currentTexture.y);
            currentPosition = null;
            currentTexture = null;
        }
    }
}
