package com.github.atomicblom.shearmadness.variation;

import com.github.atomicblom.shearmadness.api.IVariationRegistry;
import com.github.atomicblom.shearmadness.api.ItemStackHelper;
import com.github.atomicblom.shearmadness.api.RegisterShearMadnessVariationEvent;
import com.github.atomicblom.shearmadness.variation.silly.InfiltratorModelMaker;
import com.github.atomicblom.shearmadness.utility.ChiselLibrary;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by codew on 31/08/2016.
 */
@SuppressWarnings({"MethodMayBeStatic", "AnonymousInnerClass"})
public enum SillyVarations
{
    INSTANCE;

    @SubscribeEvent(priority = EventPriority.LOW)
    @Optional.Method(modid = "shearmadness")
    public void onShearMadnessRegisterVariations(RegisterShearMadnessVariationEvent event)
    {

        final IVariationRegistry registry = event.getRegistry();
        registry.registerVariation(
                itemStack -> ItemStackHelper.isStackForBlock(itemStack, ChiselLibrary.marbleextra) && itemStack.getItemDamage() == 7,
                new InfiltratorModelMaker()
                );
    }
}
