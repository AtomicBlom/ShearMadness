package com.github.atomicblom.shearmadness.api;

import com.github.atomicblom.shearmadness.api.modelmaker.IModelMaker;
import com.github.atomicblom.shearmadness.api.rendering.QuadrupedTransformDefinition;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Function;

/**
 * A registry for registering different mechanisms for rendering different blocks on sheep.
 */
@OnlyIn(Dist.CLIENT)
public interface IVariationRegistry
{
    /**
     * Registers a Model Maker that will be used when the function predicate matches
     * @param handlesVariant a function that should return true if your IModelMaker applies to the itemStack
     * @param variationModelMaker the model maker that will make the model for the quadruped.
     */
    void registerVariation(Function<ItemStack, Boolean> handlesVariant, IModelMaker variationModelMaker);

    /**
     * Registers a Model Maker that will be used when the function predicate matches using the default model maker
     * with custom transformations.
     * @param handlesVariant a function that should return true if your IModelMaker applies to the itemStack
     * @param transformDefinition the transforms for the model
     */
    void registerVariation(Function<ItemStack, Boolean> handlesVariant, QuadrupedTransformDefinition transformDefinition);
}
