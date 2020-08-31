package com.github.atomicblom.shearmadness.api;

import com.github.atomicblom.shearmadness.api.ai.ShearMadnessGoal;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.ItemStack;

import java.util.function.Function;

public class ShearMadnessGoalDefinition {
    private final Function<ItemStack, Boolean> handlesVariant;
    private final Function<SheepEntity, ShearMadnessGoal> goalFactory;

    ShearMadnessGoalDefinition(Function<ItemStack, Boolean> handlesVariant, Function<SheepEntity, ShearMadnessGoal> goalFactory)
    {

        this.handlesVariant = handlesVariant;
        this.goalFactory = goalFactory;
    }

    boolean canHandleItemStack(ItemStack itemStack)
    {
        return handlesVariant.apply(itemStack);
    }

    ShearMadnessGoal createGoal(SheepEntity entity)
    {
        return goalFactory.apply(entity);
    }
}
