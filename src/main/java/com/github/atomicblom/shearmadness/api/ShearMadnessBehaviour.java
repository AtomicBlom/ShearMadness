package com.github.atomicblom.shearmadness.api;

import com.github.atomicblom.shearmadness.api.behaviour.BehaviourBase;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.ItemStack;

import java.util.function.Function;

class ShearMadnessBehaviour {
    private final Function<ItemStack, Boolean> handlesVariant;
    private final Function<SheepEntity, BehaviourBase> behaviourFactory;

    ShearMadnessBehaviour(Function<ItemStack, Boolean> handlesVariant, Function<SheepEntity, BehaviourBase> behaviourFactory)
    {

        this.handlesVariant = handlesVariant;
        this.behaviourFactory = behaviourFactory;
    }

    boolean canHandleItemStack(ItemStack itemStack)
    {
        return handlesVariant.apply(itemStack);
    }

    BehaviourBase createBehaviourBase(SheepEntity entity)
    {
        return behaviourFactory.apply(entity);
    }
}
