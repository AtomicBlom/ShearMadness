package com.github.atomicblom.shearmadness.api;

import com.github.atomicblom.shearmadness.api.modelmaker.IModelMaker;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
class ShearMadnessVariation {

    private final Function<ItemStack, Boolean> handlesVariant;
    private final IModelMaker variationModelMaker;

    ShearMadnessVariation(Function<ItemStack, Boolean> handlesVariant, IModelMaker variationModelMaker)
    {

        this.handlesVariant = handlesVariant;
        this.variationModelMaker = variationModelMaker;
    }

    boolean canHandleItemStack(ItemStack itemStack)
    {
        return handlesVariant.apply(itemStack);
    }

    IModelMaker getVariationModelMaker()
    {
        return variationModelMaker;
    }
}
