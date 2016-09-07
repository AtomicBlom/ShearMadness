package com.github.atomicblom.shearmadness.api;

import com.github.atomicblom.shearmadness.api.modelmaker.IModelMaker;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.function.Function;

@SideOnly(Side.CLIENT)
class ShearMadnessVariation {

    private final Function<ItemStack, Boolean> handlesVariant;
    private final IModelMaker variationModelMaker;

    ShearMadnessVariation(Function<ItemStack, Boolean> handlesVariant, IModelMaker variationModelMaker)
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
