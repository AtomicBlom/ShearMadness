package com.github.atomicblom.shearmadness.variations.vanilla.visuals;

import com.github.atomicblom.shearmadness.api.modelmaker.DefaultModelMaker;
import com.github.atomicblom.shearmadness.api.rendering.PartDefinition;
import com.github.atomicblom.shearmadness.api.rendering.QuadrupedTransformDefinition;
import net.minecraft.client.model.ModelQuadruped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelSheep1;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by codew on 4/11/2016.
 */
public class CraftingTableModelMaker extends DefaultModelMaker {



    @Override
    public ModelQuadruped createModel(ItemStack itemStack, EntityLivingBase entity) {
        ModelQuadruped baseModel = super.createModel(itemStack, entity);

        CraftingItemIdentifier newModel = new CraftingItemIdentifier();
        newModel.body = baseModel.body;
        newModel.head = baseModel.head;
        newModel.leg1 = baseModel.leg1;
        newModel.leg2 = baseModel.leg2;
        newModel.leg3 = baseModel.leg3;
        newModel.leg4 = baseModel.leg4;

        final IBakedModel bakedModelForItem = getBakedModelForItem(itemStack, entity);

        PartDefinition definition = new PartDefinition(
                new Vector3f(0.0f, 6.0f, -8.0f),
                QuadrupedTransformDefinition.createPartMatrix(
                        new Vector3f(4, 4, 4),
                        new Vector3f(0, -8, 0)),
                new Matrix3f()
        );

        newModel.itemIndicator = getModelRendererForBlockState(definition, null, bakedModelForItem);

        return newModel;
    }

    private static class CraftingItemIdentifier extends ModelSheep1 {
        public ModelRenderer itemIndicator;

        @Override
        public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
            super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
            this.itemIndicator.rotateAngleY = ageInTicks * 0.1f;
        }

        @Override
        public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

            if (this.isChild)
            {
                float f = 2.0F;
                GlStateManager.pushMatrix();
                GlStateManager.translate(0.0F, this.childYOffset * scale, this.childZOffset * scale);
                itemIndicator.render(scale);
                GlStateManager.popMatrix();
            }
            else
            {
                itemIndicator.render(scale);
            }
        }
    }
}
