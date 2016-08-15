package com.github.atomicblom.chiselsheep.client.renderer.entity;

import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.VertexBuffer;

/**
 * Created by codew on 15/08/2016.
 */
public class ModelBox2 extends ModelBox
{
    public ModelBox2(ModelRenderer renderer)
    {
        super(renderer, 0, 0, 0, 0, 0, 0, 0, 0, 0, false);
    }

    @Override
    public void render(VertexBuffer renderer, float scale)
    {
        super.render(renderer, scale);



        //new TexturedQuad(vertices, 0, 1, 0, 1, 64, 64);
    }
}
