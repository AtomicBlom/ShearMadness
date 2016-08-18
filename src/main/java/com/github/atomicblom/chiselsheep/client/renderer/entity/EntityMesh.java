package com.github.atomicblom.chiselsheep.client.renderer.entity;

import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import java.util.ArrayList;
import java.util.List;

public class EntityMesh extends ModelBox
{
    private final Matrix4f positionTransform;
    private Matrix3f textureTransform;
    private TexturedQuad[] quadList = null;
    private List<BakedQuad> allBakedQuads = new ArrayList<>();

    public EntityMesh(ModelRenderer renderer, Matrix4f positionTransform, Matrix3f textureTransform)
    {
        super(renderer, 0, 0, 0, 0, 0, 0, 0, 0, 0, false);
        this.positionTransform = positionTransform;
        this.textureTransform = textureTransform;
    }

    public void addCustomQuads(List<BakedQuad> bakedQuads)
    {
        allBakedQuads.addAll(bakedQuads);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void render(VertexBuffer renderer, float scale)
    {
        if (quadList == null) {

            final List<TexturedQuad> outputQuads = new ArrayList<>();

            for (final BakedQuad bakedQuad : allBakedQuads)
            {
                final VertexConsumer consumer = new VertexConsumer(renderer.getVertexFormat());
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

    private class VertexConsumer implements IVertexConsumer {

        private final VertexFormat vertexFormat;
        private final PositionTextureVertex[] vertices = new PositionTextureVertex[4];

        private int currentVertexIndex = -1;
        private Vec3d currentPosition = null;
        private Vector3f currentTexture = null;

        TexturedQuad getOutputQuad() {
            return new TexturedQuad(vertices);
        }

        VertexConsumer(VertexFormat vertexFormat)
        {
            this.vertexFormat = vertexFormat;
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
            if (element == 0) {
                ++currentVertexIndex;
            }

            final VertexFormatElement element1 = vertexFormat.getElement(element);
            switch (element1.getUsage()) {
                case POSITION:
                    if (data.length < 3) {
                        currentPosition = null;
                        return;
                    }
                    final Vector4f position = new Vector4f(data[0], data[1], data[2], 1);
                    final Vector4f transform = Matrix4f.transform(positionTransform, position, null);
                    currentPosition = new Vec3d(transform.x, transform.y, transform.z);

                    break;
                case UV:
                    if (data.length < 2) {
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

            if (element == vertexFormat.getElementCount() - 1) {
                vertices[currentVertexIndex] = new PositionTextureVertex(currentPosition, currentTexture.x, currentTexture.y);
                currentPosition = null;
                currentTexture = null;
            }
        }
    }

}
