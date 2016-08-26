package com.github.atomicblom.shearmadness.proxy;

import com.github.atomicblom.shearmadness.utility.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@SuppressWarnings({"MethodMayBeStatic", "unused"})
public class CommonAudioProxy
{
    private SoundEvent sheepChiseledSound;

    public void registerSounds()
    {
        sheepChiseledSound = registerSound("sheepchiseled");
    }

    private static SoundEvent registerSound(String soundName) {
        final ResourceLocation soundID = new ResourceLocation(Reference.MOD_ID, soundName);
        return GameRegistry.register(new SoundEvent(soundID).setRegistryName(soundID));
    }

    public SoundEvent getSheepChiseledSound()
    {
        return sheepChiseledSound;
    }
}
