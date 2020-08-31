package com.github.atomicblom.shearmadness.variations.vanilla.visuals;

import com.github.atomicblom.shearmadness.api.Capability;
import com.github.atomicblom.shearmadness.api.capability.IChiseledSheepCapability;
import com.github.atomicblom.shearmadness.api.modelmaker.DefaultModelMaker;
import com.github.atomicblom.shearmadness.api.rendering.PartDefinition;
import com.github.atomicblom.shearmadness.api.rendering.QuadrupedTransformDefinition;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.model.ModelQuadruped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelSheep1;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.CompoundNBT;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Vector3f;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CraftingTableModelMaker extends DefaultModelMaker {
    @Override
    public ModelQuadruped createModel(ItemStack itemStack, EntityLivingBase entity) {
        ModelQuadruped baseModel = super.createModel(itemStack, entity);

        final ItemStack craftedItem = getCraftingItemStack(entity);

        if (craftedItem.isEmpty()) {
            return baseModel;
        }

        final CraftingItemIdentifier newModel = new CraftingItemIdentifier();
        newModel.body = baseModel.body;
        newModel.head = baseModel.head;
        newModel.leg1 = baseModel.leg1;
        newModel.leg2 = baseModel.leg2;
        newModel.leg3 = baseModel.leg3;
        newModel.leg4 = baseModel.leg4;

        final IBakedModel bakedModelForItem = getBakedModelForItem(craftedItem, entity);

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

    public ItemStack getCraftingItemStack(EntityLivingBase entity) {

        if (!entity.hasCapability(Capability.CHISELED_SHEEP, null) && !(entity instanceof SheepEntity)) {
            return ItemStack.EMPTY;
        }

        final IChiseledSheepCapability capability = entity.getCapability(Capability.CHISELED_SHEEP, null);
        assert capability != null;

        final CompoundNBT extraData = capability.getExtraData();
        if (!extraData.hasKey("AUTO_CRAFT")) { return ItemStack.EMPTY; }

        final CompoundNBT craftMatrixNBT = extraData.getCompoundTag("AUTO_CRAFT");

        final ItemStack[] originalCraftingGrid = new ItemStack[9];
        for (int i = 0; i < 9; ++i)
        {
            originalCraftingGrid[i] = ItemStack.EMPTY;

            final String key = ((Integer) i).toString();

            final boolean nbtHasItemInIndex = craftMatrixNBT.hasKey(key);

            if (nbtHasItemInIndex) {
                final ItemStack itemstack = new ItemStack(craftMatrixNBT.getCompoundTag(key));
                originalCraftingGrid[i] = itemstack;
            }
        }
        final ContainerWorkbench container = new ContainerWorkbench(new InventoryPlayer(null), entity.getEntityWorld(), entity.getPosition());
        for (int i = 0; i < 9; ++i) {
            container.craftMatrix.setInventorySlotContents(i, originalCraftingGrid[i]);
        }
        return CraftingManager.findMatchingResult(container.craftMatrix, entity.getEntityWorld());
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
