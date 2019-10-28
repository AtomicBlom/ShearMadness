package com.github.atomicblom.shearmadness.api;

import com.github.atomicblom.shearmadness.api.behaviour.BehaviourBase;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.ItemStack;

import java.util.function.Function;

public interface IBehaviourRegistry {

    /**
     * Registers a Model Maker that will be used when the function predicate matches
     * @param handlesVariant a function that should return true if your IModelMaker applies to the itemStack
     * @param behaviourFactory the factory to create a behaviour for the sheep.
     */
    void registerBehaviour(Function<ItemStack, Boolean> handlesVariant, Function<SheepEntity, BehaviourBase> behaviourFactory);
}
