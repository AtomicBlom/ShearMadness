package com.github.atomicblom.shearmadness.api.rendering;

import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;

public class TexturedQuadBuilder {
    public static ModelRenderer.TexturedQuad TexturedQuad(ModelRenderer.PositionTextureVertex[] vertices) {
        ModelRenderer.PositionTextureVertex[] hackTempVertices = new ModelRenderer.PositionTextureVertex[] {
            new ModelRenderer.PositionTextureVertex(0, 0, 0, 0, 0),
            new ModelRenderer.PositionTextureVertex(0, 0, 0, 0, 0),
            new ModelRenderer.PositionTextureVertex(0, 0, 0, 0, 0),
            new ModelRenderer.PositionTextureVertex(0, 0, 0, 0, 0)
        };

        ModelRenderer.TexturedQuad t = new ModelRenderer.TexturedQuad(hackTempVertices, 0, 0, 0, 0, 1, 1, false, Direction.NORTH);
        t.vertexPositions = vertices;

        Vector3f p2 = vertices[1].position.copy();
        Vector3f p3 = vertices[3].position.copy();
        p2.sub(vertices[0].position);
        p3.sub(vertices[0].position);
        p2.cross(p3);

        t.normal = p2;
        return t;
    }
}
