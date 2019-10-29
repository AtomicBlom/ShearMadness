package com.github.atomicblom.shearmadness.events;

import com.github.atomicblom.shearmadness.utility.Reference;
import com.github.atomicblom.shearmadness.api.CommonReference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = CommonReference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SoundEventRegistrationEventHandler {
    @SubscribeEvent
    public static void onRegisterSoundEvents(RegistryEvent.Register<SoundEvent> event) {
        final IForgeRegistry<SoundEvent> registry = event.getRegistry();
        registry.register(createSoundEvent(Reference.Sounds.SheepChiseled));
    }

    private static SoundEvent createSoundEvent(ResourceLocation soundName) {
        return new SoundEvent(soundName).setRegistryName(soundName);
    }
}
