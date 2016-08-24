package com.github.atomicblom.shearmadness.api;

import net.minecraft.item.ItemStack;
import java.util.function.Function;

/**
 * Created by codew on 24/08/2016.
 */
class ShearMadnessVariation {

    private final Function<ItemStack, Boolean> handlesVariant;
    private final IModelMaker variationModelMaker;

    public ShearMadnessVariation(Function<ItemStack, Boolean> handlesVariant, IModelMaker variationModelMaker)
    {

        this.handlesVariant = handlesVariant;
        this.variationModelMaker = variationModelMaker;
    }

    public boolean canHandleItemStack(ItemStack itemStack)
    {
        return handlesVariant.apply(itemStack);
    }

    public IModelMaker getVariationModelMaker()
    {
        return variationModelMaker;
    }
}
