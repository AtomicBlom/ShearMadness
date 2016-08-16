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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by codew on 15/08/2016.
 */
public class ModelBox2 extends ModelBox
{
    private TexturedQuad[] quadList;
    List<BakedQuad> allBakedQuads = new ArrayList<BakedQuad>();

    public ModelBox2(ModelRenderer renderer)
    {
        super(renderer, 0, 0, 0, 0, 0, 0, 0, 0, 0, false);
    }

    public void addCustomQuads(List<BakedQuad> bakedQuads)
    {
        allBakedQuads.addAll(bakedQuads);
    }

    @SideOnly(Side.CLIENT)
    public void render(VertexBuffer renderer, float scale)
    {
        if (quadList == null) {

            List<TexturedQuad> outputQuads = new ArrayList<TexturedQuad>();

            for (final BakedQuad bakedQuad : allBakedQuads)
            {
                VertexConsumer consumer = new VertexConsumer(renderer.getVertexFormat());
                bakedQuad.pipe(consumer);
                outputQuads.add(consumer.getOutputQuad());
            }

            quadList = new TexturedQuad[outputQuads.size()];
            quadList = outputQuads.toArray(quadList);
        }

        for (TexturedQuad texturedquad : quadList)
        {
            texturedquad.draw(renderer, scale);
        }
    }

    private class VertexConsumer implements IVertexConsumer {

        private final VertexFormat vertexFormat;
        private PositionTextureVertex[] vertices = new PositionTextureVertex[4];
        //private final List<TexturedQuad> outputQuads = new ArrayList<TexturedQuad>();

        private int currentVertexIndex = 0;
        private Vec3d currentPosition;
        private float currentU;
        private float currentV;

        public TexturedQuad getOutputQuad() {
            return new TexturedQuad(vertices);
        }

        public VertexConsumer(VertexFormat vertexFormat)
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
            final VertexFormatElement element1 = vertexFormat.getElement(element);
            switch (element1.getUsage()) {
                case POSITION:
                    if (data.length < 3) {
                        currentPosition = null;
                    }
                    currentPosition = new Vec3d(data[0], data[1], data[2]);
                    break;
                case UV:
                    if (data.length != 2) {
                        currentU = 0;
                    }
                    currentU = data[0];
                    currentV = data[1];
                    break;
                case PADDING:
                    //Finished?
                    vertices[currentVertexIndex] = new PositionTextureVertex(currentPosition, currentU, currentV);
                    currentPosition = null;
                    currentU = 0.0f;
                    currentV = 0.0f;
                    ++currentVertexIndex;
                    break;
            }
        }
    }

}
