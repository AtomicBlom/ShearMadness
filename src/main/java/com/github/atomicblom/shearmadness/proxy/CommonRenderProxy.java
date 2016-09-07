package com.github.atomicblom.shearmadness.proxy;

import com.github.atomicblom.shearmadness.variation.ImmersiveEngineeringVariations;
import com.github.atomicblom.shearmadness.variation.ShearMadnessVariations;
import com.github.atomicblom.shearmadness.variation.SillyVarations;
import net.minecraftforge.common.MinecraftForge;

@SuppressWarnings({"unused", "WeakerAccess"})
public class CommonRenderProxy
{
    public void registerRenderers()
    {

    }

    public void registerVariants()
    {
        MinecraftForge.EVENT_BUS.register(ShearMadnessVariations.INSTANCE);
        MinecraftForge.EVENT_BUS.register(SillyVarations.INSTANCE);
        MinecraftForge.EVENT_BUS.register(ImmersiveEngineeringVariations.INSTANCE);
    }
}
