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
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;
import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by codew on 15/08/2016.
 */
public class ModelBox2 extends ModelBox
{
    private final Matrix4f quadTransform;
    private TexturedQuad[] quadList = null;
    private List<BakedQuad> allBakedQuads = new ArrayList<>();

    public ModelBox2(ModelRenderer renderer, Matrix4f quadTransform)
    {
        super(renderer, 0, 0, 0, 0, 0, 0, 0, 0, 0, false);
        this.quadTransform = quadTransform;
    }

    public void addCustomQuads(List<BakedQuad> bakedQuads)
    {
        allBakedQuads.addAll(bakedQuads);
    }

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
        private float currentU;
        private float currentV;

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
                    final Vector4f tempVec3 = new Vector4f(data[0], data[1], data[2], 1);
                    final Vector4f transform = Matrix4f.transform(quadTransform, tempVec3, null);
                    currentPosition = new Vec3d(transform.x, transform.y, transform.z);

                    break;
                case UV:
                    if (data.length != 4) {
                        currentU = 0;
                        currentV = 0;
                        return;
                    }
                    currentU = data[0];
                    currentV = data[1];
                    break;
                default:
                    break;
            }

            if (element == vertexFormat.getElementCount() - 1) {
                vertices[currentVertexIndex] = new PositionTextureVertex(currentPosition, currentU, currentV);
                currentPosition = null;
                currentU = 0.0f;
                currentV = 0.0f;
            }
        }
    }

}
