package com.github.atomicblom.shearmadness.variations.vanilla.visuals;

import com.github.atomicblom.shearmadness.api.Capability;
import com.github.atomicblom.shearmadness.api.modelmaker.DefaultModelMaker;
import com.github.atomicblom.shearmadness.api.rendering.PartDefinition;
import com.github.atomicblom.shearmadness.api.rendering.QuadrupedTransformDefinition;
import com.github.atomicblom.shearmadness.api.rendering.vector.Matrix3f;
import com.github.atomicblom.shearmadness.api.rendering.vector.Vector3f;
import com.github.atomicblom.shearmadness.variations.vanilla.container.ContainerWorkbenchSheep;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.entity.model.QuadrupedModel;
import net.minecraft.client.renderer.entity.model.SheepWoolModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

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
            final ContainerWorkbenchSheep container = new ContainerWorkbenchSheep(200, playerInventory, (SheepEntity)entity);
            for (int i = 0; i < 9; ++i) {
                container.getCraftingMatrix().setInventorySlotContents(i, originalCraftingGrid[i]);
            }
            World entityWorld = entity.getEntityWorld();
            RecipeManager recipeManager = entityWorld.getRecipeManager();
            Optional<ICraftingRecipe> recipe = recipeManager.getRecipe(IRecipeType.CRAFTING, container.getCraftingMatrix(), entityWorld);
            return recipe.map(IRecipe::getRecipeOutput).orElse(ItemStack.EMPTY);
        }).orElse(ItemStack.EMPTY);
    }

    private static class CraftingItemIdentifier extends SheepWoolModel<SheepEntity> {
        public ModelRenderer itemIndicator;

        @Override
        public void setRotationAngles(SheepEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
            super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            this.itemIndicator.rotateAngleY = ageInTicks * 0.1f;
        }

        @Override
        public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
            super.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);

            if (this.isChild)
            {
                float f = 2.0F;
                matrixStackIn.push();
                matrixStackIn.translate(0.0F, this.childHeadOffsetY * this.childHeadScale, this.childHeadOffsetZ * childHeadScale);
                itemIndicator.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
                matrixStackIn.pop();
            }
            else
            {
                itemIndicator.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            }
        }
    }
}
