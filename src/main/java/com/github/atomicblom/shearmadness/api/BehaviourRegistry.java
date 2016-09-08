package com.github.atomicblom.shearmadness.api;

import com.github.atomicblom.shearmadness.api.behaviour.BehaviourBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.ItemStack;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class BehaviourRegistry implements IBehaviourRegistry {

    public static final BehaviourRegistry INSTANCE = new BehaviourRegistry();

    private final List<ShearMadnessBehaviour> behaviours = new LinkedList<>();

    @Override
    public void registerBehaviour(Function<ItemStack, Boolean> handlesVariant, Function<EntitySheep, BehaviourBase> behaviourFactory) {
        behaviours.add(new ShearMadnessBehaviour(handlesVariant, behaviourFactory));
    }

    public Iterable<BehaviourBase> getApplicableBehaviours(ItemStack itemStack, EntitySheep entity) {
        final Iterator<ShearMadnessBehaviour> baseIterator = behaviours.iterator();

        return () -> new BehaviourIterator(itemStack, baseIterator, entity);
    }

    private static class BehaviourIterator implements Iterator<BehaviourBase> {
        private final Iterator<ShearMadnessBehaviour> baseIterator;
        private final EntitySheep entity;
        private ShearMadnessBehaviour nextBehaviour = null;

        private final ItemStack itemStack;

        private BehaviourIterator(ItemStack itemStack, Iterator<ShearMadnessBehaviour> baseIterator, EntitySheep entity) {
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
}
