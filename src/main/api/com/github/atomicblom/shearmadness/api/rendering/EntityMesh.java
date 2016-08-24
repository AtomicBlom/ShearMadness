package com.github.atomicblom.shearmadness.api.rendering;

import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;
import java.util.ArrayList;
import java.util.List;

/**
 * A version of ModelBox that converts BakedQuads to TexturedQuads (as used in entities).
 * Add BakedQuads using the addCustomQuads method.
 * Note that TexturedQuads only have Position and UV information, other data will be discarded.
 */
@SideOnly(Side.CLIENT)
public class EntityMesh extends ModelBox
{
    private final Matrix4f positionTransform;
    private final Matrix3f textureTransform;
    private final List<BakedQuad> allBakedQuads = new ArrayList<>(6);
    private TexturedQuad[] quadList = null;

    public EntityMesh(ModelRenderer renderer, Matrix4f positionTransform, Matrix3f textureTransform)
    {
        super(renderer, 0, 0, 0, 0, 0, 0, 0, 0, 0, false);
        this.positionTransform = positionTransform;
        this.textureTransform = textureTransform;
    }

    /**
     * Add a BakedQuad to the mesh. It will be processed in batch when the model is rendered for the first time.
     * @param bakedQuads
     */
    public void addCustomQuads(List<BakedQuad> bakedQuads)
    {
        allBakedQuads.addAll(bakedQuads);
    }

    @SuppressWarnings("ObjectAllocationInLoop")
    @Override
    @SideOnly(Side.CLIENT)
    public void render(VertexBuffer renderer, float scale)
    {
        if (quadList == null)
        {

            final List<TexturedQuad> outputQuads = new ArrayList<>(6);

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
