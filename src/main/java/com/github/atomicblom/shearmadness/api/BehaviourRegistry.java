package com.github.atomicblom.shearmadness.api;

import com.github.atomicblom.shearmadness.api.ai.ShearMadnessGoal;
import com.github.atomicblom.shearmadness.api.behaviour.BehaviourBase;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.ItemStack;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class BehaviourRegistry implements IBehaviourRegistry {

    public static final BehaviourRegistry INSTANCE = new BehaviourRegistry();

    private final List<ShearMadnessBehaviour> behaviours = new LinkedList<>();
    private final List<ShearMadnessGoalDefinition> goals = new LinkedList<>();

    @Override
    public void registerBehaviour(Function<ItemStack, Boolean> handlesVariant, Function<SheepEntity, BehaviourBase> behaviourFactory) {
        behaviours.add(new ShearMadnessBehaviour(handlesVariant, behaviourFactory));
    }

    @Override
    public void registerGoal(Function<ItemStack, Boolean> handlesVariant, Function<SheepEntity, ShearMadnessGoal> goalFactory) {
        goals.add(new ShearMadnessGoalDefinition(handlesVariant, goalFactory));
    }

    public Iterable<BehaviourBase> getApplicableBehaviours(ItemStack itemStack, SheepEntity entity) {
        final Iterator<ShearMadnessBehaviour> baseIterator = behaviours.iterator();

        return () -> new BehaviourIterator(itemStack, baseIterator, entity);
    }

    private static class BehaviourIterator implements Iterator<BehaviourBase> {
        private final Iterator<ShearMadnessBehaviour> baseIterator;
        private final SheepEntity entity;
        private ShearMadnessBehaviour nextBehaviour = null;

        private final ItemStack itemStack;

        private BehaviourIterator(ItemStack itemStack, Iterator<ShearMadnessBehaviour> baseIterator, SheepEntity entity) {
            this.baseIterator = baseIterator;
            this.entity = entity;
            this.itemStack = itemStack;
        }

        @Override
        public boolean hasNext() {
            while (true) {
                if (!baseIterator.hasNext()) {
                    return false;
                }
                nextBehaviour = baseIterator.next();
                if (nextBehaviour.canHandleItemStack(itemStack)) {
                    return true;
                }
            }
        }

        @Override
        public BehaviourBase next() {
            if (nextBehaviour == null) {
                throw new NoSuchElementException("Invalid iteration over behaviours");
            }
            return nextBehaviour.createBehaviourBase(entity);
        }
    }

    public Iterable<ShearMadnessGoal> getApplicableGoals(ItemStack itemStack, SheepEntity entity) {
        final Iterator<ShearMadnessGoalDefinition> baseIterator = goals.iterator();

        return () -> new GoalIterator(itemStack, baseIterator, entity);
    }

    private static class GoalIterator implements Iterator<ShearMadnessGoal> {
        private final Iterator<ShearMadnessGoalDefinition> baseIterator;
        private final SheepEntity entity;
        private ShearMadnessGoalDefinition nextBehaviour = null;

        private final ItemStack itemStack;

        private GoalIterator(ItemStack itemStack, Iterator<ShearMadnessGoalDefinition> baseIterator, SheepEntity entity) {
            this.baseIterator = baseIterator;
            this.entity = entity;
            this.itemStack = itemStack;
        }

        @Override
        public boolean hasNext() {
            while (true) {
                if (!baseIterator.hasNext()) {
                    return false;
                }
                nextBehaviour = baseIterator.next();
                if (nextBehaviour.canHandleItemStack(itemStack)) {
                    return true;
                }
            }
        }

        @Override
        public ShearMadnessGoal next() {
            if (nextBehaviour == null) {
                throw new NoSuchElementException("Invalid iteration over behaviours");
            }
            return nextBehaviour.createGoal(entity);
        }
    }
}
