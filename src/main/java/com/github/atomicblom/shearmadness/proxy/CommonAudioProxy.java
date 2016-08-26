package com.github.atomicblom.shearmadness.proxy;

import com.github.atomicblom.shearmadness.utility.Reference;
import com.github.atomicblom.shearmadness.utility.SoundLibrary;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@SuppressWarnings("MethodMayBeStatic")
public class CommonAudioProxy
{
    public void registerSounds()
    {
        //FIXME: Remove this once SoundEvent is added to ObjectHolderRef
        SoundLibrary.sheepChiseled = registerSound("sheepChiseled");
    }

    private static SoundEvent registerSound(String soundName) {
        final ResourceLocation soundID = new ResourceLocation(Reference.MOD_ID, soundName);
        return GameRegistry.register(new SoundEvent(soundID).setRegistryName(soundID));
    }
}
