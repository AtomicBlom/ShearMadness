package com.github.atomicblom.shearmadness.api;

import com.github.atomicblom.shearmadness.api.behaviour.BehaviourBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.ItemStack;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class BehaviourRegistry implements IBehaviourRegistry {

    public static final BehaviourRegistry INSTANCE = new BehaviourRegistry();

    private final List<ShearMadnessBehaviour> behaviours = new LinkedList<>();

    @Override
    public void registerBehaviour(Function<ItemStack, Boolean> handlesVariant, Function<EntitySheep, BehaviourBase> behaviourFactory) {
        behaviours.add(new ShearMadnessBehaviour(handlesVariant, behaviourFactory));
    }

    public Iterable<BehaviourBase> getApplicableBehaviours(ItemStack itemStack, EntitySheep entity) {
        Iterator<ShearMadnessBehaviour> baseIterator = behaviours.iterator();

        return () -> new Iterator<BehaviourBase>() {
            ShearMadnessBehaviour nextBehaviour = null;

            ItemStack localItemStack = itemStack;

            @Override
            public boolean hasNext() {
                while (true) {
                    if (!baseIterator.hasNext()) {
                        return false;
                    }
                    nextBehaviour = baseIterator.next();
                    if (nextBehaviour.canHandleItemStack(localItemStack)) {
                        return true;
                    }
                }
            }

            @Override
            public BehaviourBase next() {
                return nextBehaviour.createBehaviourBase(entity);
            }
        };
    }
}
