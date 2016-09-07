package com.github.atomicblom.shearmadness.api;

import com.github.atomicblom.shearmadness.api.modelmaker.DefaultModelMaker;
import com.github.atomicblom.shearmadness.api.modelmaker.IModelMaker;
import com.github.atomicblom.shearmadness.api.rendering.QuadrupedTransformDefinition;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

@SideOnly(Side.CLIENT)
public class VariationRegistry implements IVariationRegistry
{
    public static final VariationRegistry INSTANCE = new VariationRegistry();

    private VariationRegistry() {}

    private final List<ShearMadnessVariation> variations = new LinkedList<>();

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
}

