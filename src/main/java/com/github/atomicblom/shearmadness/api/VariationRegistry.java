package com.github.atomicblom.shearmadness.api;

import com.github.atomicblom.shearmadness.api.behaviour.BehaviourBase;
import com.github.atomicblom.shearmadness.api.rendering.QuadrupedTransformDefinition;
import com.github.atomicblom.shearmadness.modelmaker.DefaultModelMaker;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.ItemStack;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class VariationRegistry implements IVariationRegistry
{
    public static final VariationRegistry INSTANCE = new VariationRegistry();

    private VariationRegistry() {}

    private final List<ShearMadnessVariation> variations = new LinkedList<>();
    private final List<ShearMadnessBehaviour> behaviours = new LinkedList<>();
    private final IModelMaker defaultHandler = new DefaultModelMaker();

    @Override
    public void registerVariation(Function<ItemStack, Boolean> handlesVariant, IModelMaker variationModelMaker) {
        variations.add(new ShearMadnessVariation(handlesVariant, variationModelMaker));
    }

    @Override
    public void registerVariation(Function<ItemStack, Boolean> handlesVariant, QuadrupedTransformDefinition transformDefinition)
    {
        variations.add(new ShearMadnessVariation(handlesVariant, new DefaultModelMaker(transformDefinition)));
    }

    @Override
    public void registerVariationWithBehaviour(Function<ItemStack, Boolean> handlesVariant, IModelMaker variationModelMaker, Function<EntitySheep, BehaviourBase> behaviourFactory) {
        registerVariation(handlesVariant, variationModelMaker);
        registerBehaviour(handlesVariant, behaviourFactory);
    }

    @Override
    public void registerVariationWithBehaviour(Function<ItemStack, Boolean> handlesVariant, QuadrupedTransformDefinition transformDefinition, Function<EntitySheep, BehaviourBase> behaviourFactory) {
        registerVariation(handlesVariant, transformDefinition);
        registerBehaviour(handlesVariant, behaviourFactory);
    }

    @Override
    public void registerBehaviour(Function<ItemStack, Boolean> handlesVariant, Function<EntitySheep, BehaviourBase> behaviourFactory) {
        behaviours.add(new ShearMadnessBehaviour(handlesVariant, behaviourFactory));
    }

    public IModelMaker getVariationModelMaker(ItemStack itemStack) {
        IModelMaker handler = null;
        for (final ShearMadnessVariation variation : variations)
        {
            if (variation.canHandleItemStack(itemStack)) {
                handler = variation.getVariationModelMaker();
                break;
            }
        }
        if (handler == null) {
            handler = defaultHandler;
        }

        return handler;
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

