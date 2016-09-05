package com.github.atomicblom.shearmadness.api;

import com.github.atomicblom.shearmadness.api.behaviour.BehaviourBase;
import com.github.atomicblom.shearmadness.api.rendering.QuadrupedTransformDefinition;
import com.github.atomicblom.shearmadness.capability.ChiseledSheepCapability;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import java.util.function.Function;

/**
 * A registry for registering different mechanisms for rendering different blocks on sheep.
 */
@SideOnly(Side.CLIENT)
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

    /**
     * Registers a Model Maker that will be used when the function predicate matches
     * @param handlesVariant a function that should return true if your IModelMaker applies to the itemStack
     * @param variationModelMaker the model maker that will make the model for the quadruped.
     * @param behaviourFactory the factory to create a behaviour for the sheep.
     */
    void registerVariationWithBehaviour(Function<ItemStack, Boolean> handlesVariant, IModelMaker variationModelMaker, Function<EntitySheep, BehaviourBase> behaviourFactory);

    /**
     * Registers a Model Maker that will be used when the function predicate matches using the default model maker
     * with custom transformations.
     * @param handlesVariant a function that should return true if your IModelMaker applies to the itemStack
     * @param transformDefinition the transforms for the model
     * @param behaviourFactory the factory to create a behaviour for the sheep.
     */
    void registerVariationWithBehaviour(Function<ItemStack, Boolean> handlesVariant, QuadrupedTransformDefinition transformDefinition, Function<EntitySheep, BehaviourBase> behaviourFactory);

    /**
     * Registers a Model Maker that will be used when the function predicate matches
     * @param handlesVariant a function that should return true if your IModelMaker applies to the itemStack
     * @param behaviourFactory the factory to create a behaviour for the sheep.
     */
    void registerBehaviour(Function<ItemStack, Boolean> handlesVariant, Function<EntitySheep, BehaviourBase> behaviourFactory);
}
