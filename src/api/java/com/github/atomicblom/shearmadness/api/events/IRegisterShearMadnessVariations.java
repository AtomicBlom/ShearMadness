package com.github.atomicblom.shearmadness.api.events;

import com.github.atomicblom.shearmadness.api.IVariationRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface IRegisterShearMadnessVariations {
    void registerVariations(IVariationRegistry registry);
}
