package com.github.atomicblom.shearmadness.api;

import com.github.atomicblom.shearmadness.api.behaviour.BehaviourBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.ItemStack;

import java.util.function.Function;

class ShearMadnessBehaviour {
    private final Function<ItemStack, Boolean> handlesVariant;
    private final Function<EntitySheep, BehaviourBase> behaviourFactory;

    ShearMadnessBehaviour(Function<ItemStack, Boolean> handlesVariant, Function<EntitySheep, BehaviourBase> behaviourFactory)
    {

        this.handlesVariant = handlesVariant;
        this.behaviourFactory = behaviourFactory;
    }

    boolean canHandleItemStack(ItemStack itemStack)
    {
        return handlesVariant.apply(itemStack);
    }

    BehaviourBase createBehaviourBase(EntitySheep entity)
    {
        return behaviourFactory.apply(entity);
    }
}
