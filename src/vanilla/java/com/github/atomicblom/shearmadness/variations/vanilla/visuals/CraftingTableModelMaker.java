package com.github.atomicblom.shearmadness.variations.vanilla.visuals;

import com.github.atomicblom.shearmadness.api.Capability;
import com.github.atomicblom.shearmadness.api.modelmaker.DefaultModelMaker;
import com.github.atomicblom.shearmadness.api.rendering.PartDefinition;
import com.github.atomicblom.shearmadness.api.rendering.QuadrupedTransformDefinition;
import com.github.atomicblom.shearmadness.api.rendering.vector.Matrix3f;
import com.github.atomicblom.shearmadness.api.rendering.vector.Vector3f;
import com.mojang.blaze3d.platform.GlStateManager;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.entity.model.QuadrupedModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.entity.model.SheepModel;
import net.minecraft.client.renderer.entity.model.SheepWoolModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.WorkbenchContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IWorldPosCallable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CraftingTableModelMaker extends DefaultModelMaker {
    @Override
    public QuadrupedModel<SheepEntity> createModel(ItemStack itemStack, LivingEntity entity) {
        QuadrupedModel<SheepEntity> baseModel = super.createModel(itemStack, entity);

        final ItemStack craftedItem = getCraftingItemStack(entity);

        if (craftedItem.isEmpty()) {
            return baseModel;
        }

        final CraftingItemIdentifier newModel = new CraftingItemIdentifier();
        newModel.body = baseModel.body;
        newModel.headModel = baseModel.headModel;
        newModel.legBackLeft = baseModel.legBackLeft;
        newModel.legBackRight = baseModel.legBackRight;
        newModel.legFrontLeft = baseModel.legFrontLeft;
        newModel.legFrontRight = baseModel.legFrontRight;

        final IBakedModel bakedModelForItem = getBakedModelForItem(craftedItem, entity);

        PartDefinition definition = new PartDefinition(
                new Vector3f(0.0f, 6.0f, -8.0f),
                QuadrupedTransformDefinition.createPartMatrix(
                        new Vector3f(4, 4, 4),
                        new Vector3f(0, -8, 0)),
                new Matrix3f()
        );

        newModel.itemIndicator = getModelRendererForBlockState(definition, null, bakedModelForItem, "Item Indicator");

        return newModel;
    }

    public ItemStack getCraftingItemStack(LivingEntity entity) {
        if (!(entity instanceof SheepEntity)) return ItemStack.EMPTY;
        return entity.getCapability(Capability.CHISELED_SHEEP).map(capability -> {
            final CompoundNBT extraData = capability.getExtraData();
            if (!extraData.contains("AUTO_CRAFT")) { return ItemStack.EMPTY; }

            final CompoundNBT craftMatrixNBT = extraData.getCompound("AUTO_CRAFT");

            final ItemStack[] originalCraftingGrid = new ItemStack[9];
            for (int i = 0; i < 9; ++i)
            {
                originalCraftingGrid[i] = ItemStack.EMPTY;

                final String key = ((Integer) i).toString();

                final boolean nbtHasItemInIndex = craftMatrixNBT.contains(key);

                if (nbtHasItemInIndex) {
                    final ItemStack itemstack = ItemStack.read(craftMatrixNBT.getCompound(key));
                    originalCraftingGrid[i] = itemstack;
                }
            }
            PlayerInventory playerInventory = new PlayerInventory(null);

            //FIXME: get a window ID somehow?
            final WorkbenchContainer container = new WorkbenchContainer(200, playerInventory, IWorldPosCallable.of(entity.getEntityWorld(), entity.getPosition()));
            for (int i = 0; i < 9; ++i) {
                container.field_75162_e.setInventorySlotContents(i, originalCraftingGrid[i]);
            }
            Optional<ICraftingRecipe> recipe = entity.getEntityWorld().getServer().getRecipeManager().getRecipe(IRecipeType.CRAFTING, container.field_75162_e, entity.getEntityWorld());
            return recipe.map(IRecipe::getRecipeOutput).orElse(ItemStack.EMPTY);
        }).orElse(ItemStack.EMPTY);
    }

    //FIXME: could be SheepWoolModel
    private static class CraftingItemIdentifier extends SheepWoolModel<SheepEntity> {
        public RendererModel itemIndicator;

        @Override
        public void setRotationAngles(SheepEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
            super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
            this.itemIndicator.rotateAngleY = ageInTicks * 0.1f;
        }

        @Override
        public void render(SheepEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

            if (this.isChild)
            {
                float f = 2.0F;
                GlStateManager.pushMatrix();
                GlStateManager.translatef(0.0F, this.childYOffset * scale, this.childZOffset * scale);
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
