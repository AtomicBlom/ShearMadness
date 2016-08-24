package com.github.atomicblom.shearmadness.proxy;

import net.minecraft.util.SoundEvent;

public interface IRenderProxy
{
    void registerRenderers();

    void registerSounds();

    void fireRegistryEvent();

    SoundEvent getSheepChiseledSound();
}
