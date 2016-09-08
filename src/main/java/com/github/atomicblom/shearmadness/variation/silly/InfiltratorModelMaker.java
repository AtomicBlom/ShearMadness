package com.github.atomicblom.shearmadness.variation.silly;

import com.github.atomicblom.shearmadness.api.modelmaker.DefaultModelMaker;
import com.github.atomicblom.shearmadness.api.rendering.EntityMesh;
import com.github.atomicblom.shearmadness.utility.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelQuadruped;
import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class InfiltratorModelMaker extends DefaultModelMaker
{
    @Override
    public ModelQuadruped createModel(ItemStack itemStack, EntityLivingBase entity)
    {
        final ModelQuadruped model = super.createModel(itemStack, entity);

        final Matrix4f partATransform = new Matrix4f().translate(new Vector3f(-2, -1, -8));
        EntityMesh partA = new EntityMesh(model.head);

        final TextureMap blockTextureMap = Minecraft.getMinecraft().getTextureMapBlocks();
        final TextureAtlasSprite chickenSprite = blockTextureMap.getAtlasSprite(Reference.MOD_ID + ":chicken-nuggets");

        partA.addTexturedQuads(partATransform, new Matrix3f(),
                new TexturedQuad(
                        new PositionTextureVertex[]{
                                new PositionTextureVertex(0, 0, 0, chickenSprite.getInterpolatedU(4), chickenSprite.getInterpolatedV(2)),
                                new PositionTextureVertex(4, 0, 0, chickenSprite.getInterpolatedU(8), chickenSprite.getInterpolatedV(2)),
                                new PositionTextureVertex(4, 0, 2, chickenSprite.getInterpolatedU(8), chickenSprite.getInterpolatedV(0)),
                                new PositionTextureVertex(0, 0, 2, chickenSprite.getInterpolatedU(4), chickenSprite.getInterpolatedV(0))
                        }),
                new TexturedQuad(
                        new PositionTextureVertex[]{
                                new PositionTextureVertex(0, 0, 0, chickenSprite.getInterpolatedU(4), chickenSprite.getInterpolatedV(2)),
                                new PositionTextureVertex(4, 0, 0, chickenSprite.getInterpolatedU(8), chickenSprite.getInterpolatedV(2)),
                                new PositionTextureVertex(4, 2, 0, chickenSprite.getInterpolatedU(8), chickenSprite.getInterpolatedV(4)),
                                new PositionTextureVertex(0, 2, 0, chickenSprite.getInterpolatedU(4), chickenSprite.getInterpolatedV(4))
                        }),
                new TexturedQuad(
                        new PositionTextureVertex[]{
                                new PositionTextureVertex(0, 0, 0, chickenSprite.getInterpolatedU(2), chickenSprite.getInterpolatedV(2)),
                                new PositionTextureVertex(0, 0, 2, chickenSprite.getInterpolatedU(4), chickenSprite.getInterpolatedV(2)),
                                new PositionTextureVertex(0, 2, 2, chickenSprite.getInterpolatedU(4), chickenSprite.getInterpolatedV(4)),
                                new PositionTextureVertex(0, 2, 0, chickenSprite.getInterpolatedU(2), chickenSprite.getInterpolatedV(4))
                        }),
                new TexturedQuad(
                        new PositionTextureVertex[]{
                                new PositionTextureVertex(4, 0, 0, chickenSprite.getInterpolatedU(8), chickenSprite.getInterpolatedV(2)),
                                new PositionTextureVertex(4, 0, 2, chickenSprite.getInterpolatedU(10), chickenSprite.getInterpolatedV(2)),
                                new PositionTextureVertex(4, 2, 2, chickenSprite.getInterpolatedU(10), chickenSprite.getInterpolatedV(4)),
                                new PositionTextureVertex(4, 2, 0, chickenSprite.getInterpolatedU(8), chickenSprite.getInterpolatedV(4))
                        }),
                new TexturedQuad(
                        new PositionTextureVertex[]{
                                new PositionTextureVertex(0, 2, 0, chickenSprite.getInterpolatedU(10), chickenSprite.getInterpolatedV(2)),
                                new PositionTextureVertex(0, 2, 2, chickenSprite.getInterpolatedU(14), chickenSprite.getInterpolatedV(2)),
                                new PositionTextureVertex(4, 2, 2, chickenSprite.getInterpolatedU(14), chickenSprite.getInterpolatedV(4)),
                                new PositionTextureVertex(4, 2, 0, chickenSprite.getInterpolatedU(10), chickenSprite.getInterpolatedV(4))
                        }),

                new TexturedQuad(
                        new PositionTextureVertex[]{
                                new PositionTextureVertex(1, 2, 1, chickenSprite.getInterpolatedU(4), chickenSprite.getInterpolatedV(4)),
                                new PositionTextureVertex(3, 2, 1, chickenSprite.getInterpolatedU(6), chickenSprite.getInterpolatedV(4)),
                                new PositionTextureVertex(3, 4, 1, chickenSprite.getInterpolatedU(6), chickenSprite.getInterpolatedV(6)),
                                new PositionTextureVertex(1, 4, 1, chickenSprite.getInterpolatedU(4), chickenSprite.getInterpolatedV(6))
                        }),
                new TexturedQuad(
                        new PositionTextureVertex[]{
                                new PositionTextureVertex(1, 2, 1, chickenSprite.getInterpolatedU(2), chickenSprite.getInterpolatedV(6)),
                                new PositionTextureVertex(1, 2, 3, chickenSprite.getInterpolatedU(4), chickenSprite.getInterpolatedV(6)),
                                new PositionTextureVertex(1, 4, 3, chickenSprite.getInterpolatedU(4), chickenSprite.getInterpolatedV(8)),
                                new PositionTextureVertex(1, 4, 1, chickenSprite.getInterpolatedU(2), chickenSprite.getInterpolatedV(8))
                        }),
                new TexturedQuad(
                        new PositionTextureVertex[]{
                                new PositionTextureVertex(3, 2, 1, chickenSprite.getInterpolatedU(6), chickenSprite.getInterpolatedV(6)),
                                new PositionTextureVertex(3, 2, 3, chickenSprite.getInterpolatedU(8), chickenSprite.getInterpolatedV(6)),
                                new PositionTextureVertex(3, 4, 3, chickenSprite.getInterpolatedU(8), chickenSprite.getInterpolatedV(8)),
                                new PositionTextureVertex(3, 4, 1, chickenSprite.getInterpolatedU(6), chickenSprite.getInterpolatedV(8))
                        }),
                new TexturedQuad(
                        new PositionTextureVertex[]{
                                new PositionTextureVertex(1, 4, 1, chickenSprite.getInterpolatedU(8), chickenSprite.getInterpolatedV(6)),
                                new PositionTextureVertex(1, 4, 3, chickenSprite.getInterpolatedU(10), chickenSprite.getInterpolatedV(6)),
                                new PositionTextureVertex(3, 4, 3, chickenSprite.getInterpolatedU(10), chickenSprite.getInterpolatedV(8)),
                                new PositionTextureVertex(3, 4, 1, chickenSprite.getInterpolatedU(8), chickenSprite.getInterpolatedV(8))
                        }),
                new TexturedQuad(
                        new PositionTextureVertex[]{
                                new PositionTextureVertex(1, 3, 3, chickenSprite.getInterpolatedU(6), chickenSprite.getInterpolatedV(4)),
                                new PositionTextureVertex(3, 3, 3, chickenSprite.getInterpolatedU(8), chickenSprite.getInterpolatedV(4)),
                                new PositionTextureVertex(3, 4, 3, chickenSprite.getInterpolatedU(8), chickenSprite.getInterpolatedV(6)),
                                new PositionTextureVertex(1, 4, 3, chickenSprite.getInterpolatedU(6), chickenSprite.getInterpolatedV(6))
                        })
                );
        model.head.cubeList.add(partA);

        final TextureAtlasSprite chickenWings = blockTextureMap.getAtlasSprite(Reference.MOD_ID + ":chicken-winglets");

        TexturedQuad[] texturedQuads = {new TexturedQuad(
                new PositionTextureVertex[]{
                        new PositionTextureVertex(0, 0, 0, chickenWings.getInterpolatedU(0), chickenWings.getInterpolatedV(0)),
                        new PositionTextureVertex(0, 12, 0, chickenWings.getInterpolatedU(12), chickenWings.getInterpolatedV(0)),
                        new PositionTextureVertex(0, 12, 6, chickenWings.getInterpolatedU(12), chickenWings.getInterpolatedV(6)),
                        new PositionTextureVertex(0, 0, 6, chickenWings.getInterpolatedU(0), chickenWings.getInterpolatedV(6))
                }
                ),
                new TexturedQuad(
                        new PositionTextureVertex[]{
                                new PositionTextureVertex(2, 0, 0, chickenWings.getInterpolatedU(0), chickenWings.getInterpolatedV(0)),
                                new PositionTextureVertex(2, 12, 0, chickenWings.getInterpolatedU(12), chickenWings.getInterpolatedV(0)),
                                new PositionTextureVertex(2, 12, 6, chickenWings.getInterpolatedU(12), chickenWings.getInterpolatedV(6)),
                                new PositionTextureVertex(2, 0, 6, chickenWings.getInterpolatedU(0), chickenWings.getInterpolatedV(6))
                        }
                ),
                new TexturedQuad(
                        new PositionTextureVertex[]{
                                new PositionTextureVertex(0, 0, 0, chickenWings.getInterpolatedU(0), chickenWings.getInterpolatedV(0)),
                                new PositionTextureVertex(2, 0, 0, chickenWings.getInterpolatedU(2), chickenWings.getInterpolatedV(0)),
                                new PositionTextureVertex(2, 0, 6, chickenWings.getInterpolatedU(2), chickenWings.getInterpolatedV(6)),
                                new PositionTextureVertex(0, 0, 6, chickenWings.getInterpolatedU(0), chickenWings.getInterpolatedV(6))
                        }
                ),
                new TexturedQuad(
                        new PositionTextureVertex[]{
                                new PositionTextureVertex(0, 12, 0, chickenWings.getInterpolatedU(0), chickenWings.getInterpolatedV(0)),
                                new PositionTextureVertex(2, 12, 0, chickenWings.getInterpolatedU(2), chickenWings.getInterpolatedV(0)),
                                new PositionTextureVertex(2, 12, 6, chickenWings.getInterpolatedU(2), chickenWings.getInterpolatedV(6)),
                                new PositionTextureVertex(0, 12, 6, chickenWings.getInterpolatedU(0), chickenWings.getInterpolatedV(6))
                        }
                ),
                new TexturedQuad(
                        new PositionTextureVertex[]{
                                new PositionTextureVertex(0, 0, 0, chickenWings.getInterpolatedU(0), chickenWings.getInterpolatedV(0)),
                                new PositionTextureVertex(0, 12, 0, chickenWings.getInterpolatedU(12), chickenWings.getInterpolatedV(0)),
                                new PositionTextureVertex(2, 12, 0, chickenWings.getInterpolatedU(12), chickenWings.getInterpolatedV(2)),
                                new PositionTextureVertex(2, 0, 0, chickenWings.getInterpolatedU(0), chickenWings.getInterpolatedV(2))
                        }
                ),
                new TexturedQuad(
                        new PositionTextureVertex[]{
                                new PositionTextureVertex(0, 0, 6, chickenWings.getInterpolatedU(0), chickenWings.getInterpolatedV(0)),
                                new PositionTextureVertex(2, 0, 6, chickenWings.getInterpolatedU(0), chickenWings.getInterpolatedV(2)),
                                new PositionTextureVertex(2, 12, 6, chickenWings.getInterpolatedU(12), chickenWings.getInterpolatedV(2)),
                                new PositionTextureVertex(0, 12, 6, chickenWings.getInterpolatedU(12), chickenWings.getInterpolatedV(0))


                        }
                )};

        final Matrix4f rightWingTransform = new Matrix4f().translate(new Vector3f(-7.6f, -8, -6));
        EntityMesh rightWing = new EntityMesh(model.body);
        rightWing.addTexturedQuads(rightWingTransform, new Matrix3f(), texturedQuads);
        model.body.cubeList.add(rightWing);

        final Matrix4f leftWingTransform = new Matrix4f().translate(new Vector3f(5.6f, -8, -6));
        EntityMesh leftWing = new EntityMesh(model.body);
        leftWing.addTexturedQuads(leftWingTransform, new Matrix3f(), texturedQuads);
        model.body.cubeList.add(leftWing);

        texturedQuads = new TexturedQuad[]{
                new TexturedQuad(
                        new PositionTextureVertex[]{
                                new PositionTextureVertex(0, 0, 0, chickenSprite.getInterpolatedU(15), chickenSprite.getInterpolatedV(10)),
                                new PositionTextureVertex(4, 0, 0, chickenSprite.getInterpolatedU(16), chickenSprite.getInterpolatedV(10)),
                                new PositionTextureVertex(4, 8, 0, chickenSprite.getInterpolatedU(16), chickenSprite.getInterpolatedV(16)),
                                new PositionTextureVertex(0, 8, 0, chickenSprite.getInterpolatedU(15), chickenSprite.getInterpolatedV(16))
                        }
                ),
                new TexturedQuad(
                        new PositionTextureVertex[]{
                                new PositionTextureVertex(0, 0, 4, chickenSprite.getInterpolatedU(15), chickenSprite.getInterpolatedV(10)),
                                new PositionTextureVertex(4, 0, 4, chickenSprite.getInterpolatedU(16), chickenSprite.getInterpolatedV(10)),
                                new PositionTextureVertex(4, 8, 4, chickenSprite.getInterpolatedU(16), chickenSprite.getInterpolatedV(16)),
                                new PositionTextureVertex(0, 8, 4, chickenSprite.getInterpolatedU(15), chickenSprite.getInterpolatedV(16))
                        }
                ),
                new TexturedQuad(
                        new PositionTextureVertex[]{
                                new PositionTextureVertex(0, 0, 0, chickenSprite.getInterpolatedU(15), chickenSprite.getInterpolatedV(10)),
                                new PositionTextureVertex(0, 0, 4, chickenSprite.getInterpolatedU(16), chickenSprite.getInterpolatedV(10)),
                                new PositionTextureVertex(0, 8, 4, chickenSprite.getInterpolatedU(16), chickenSprite.getInterpolatedV(16)),
                                new PositionTextureVertex(0, 8, 0, chickenSprite.getInterpolatedU(15), chickenSprite.getInterpolatedV(16))
                        }
                ),
                new TexturedQuad(
                        new PositionTextureVertex[]{
                                new PositionTextureVertex(4, 0, 0, chickenSprite.getInterpolatedU(15), chickenSprite.getInterpolatedV(10)),
                                new PositionTextureVertex(4, 0, 4, chickenSprite.getInterpolatedU(16), chickenSprite.getInterpolatedV(10)),
                                new PositionTextureVertex(4, 8, 4, chickenSprite.getInterpolatedU(16), chickenSprite.getInterpolatedV(16)),
                                new PositionTextureVertex(4, 8, 0, chickenSprite.getInterpolatedU(15), chickenSprite.getInterpolatedV(16))
                        }
                ),
                new TexturedQuad(
                        new PositionTextureVertex[]{
                                new PositionTextureVertex(0, 7.8f, 1, chickenSprite.getInterpolatedU(11), chickenSprite.getInterpolatedV(8)),
                                new PositionTextureVertex(4, 7.8f, 1, chickenSprite.getInterpolatedU(14), chickenSprite.getInterpolatedV(8)),
                                new PositionTextureVertex(4, 7.8f, -2, chickenSprite.getInterpolatedU(14), chickenSprite.getInterpolatedV(11)),
                                new PositionTextureVertex(0, 7.8f, -2, chickenSprite.getInterpolatedU(11), chickenSprite.getInterpolatedV(11))
                        }
                )
        };

        final Matrix4f legTransform = new Matrix4f().scale(new Vector3f(1.01f, 1.01f, 1.01f)).translate(new Vector3f(-2, 4, -2));
        EntityMesh rightForeLeg = new EntityMesh(model.leg3);
        rightForeLeg.addTexturedQuads(legTransform, new Matrix3f(), texturedQuads);
        model.leg3.cubeList.add(rightForeLeg);

        EntityMesh leftForeLeg = new EntityMesh(model.leg4);
        leftForeLeg.addTexturedQuads(legTransform, new Matrix3f(), texturedQuads);
        model.leg4.cubeList.add(leftForeLeg);

        EntityMesh rightRearLeg = new EntityMesh(model.leg1);
        rightRearLeg.addTexturedQuads(legTransform, new Matrix3f(), texturedQuads);
        model.leg1.cubeList.add(rightRearLeg);

        EntityMesh leftRearLeg = new EntityMesh(model.leg2);
        leftRearLeg.addTexturedQuads(legTransform, new Matrix3f(), texturedQuads);
        model.leg2.cubeList.add(leftRearLeg);

        return model;
    }
}
