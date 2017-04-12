package com.github.atomicblom.shearmadness.events;

import com.github.atomicblom.shearmadness.api.particles.ICustomParticleFactory;
import com.github.atomicblom.shearmadness.variations.CommonReference;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent.NewRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.RegistryBuilder;

@Mod.EventBusSubscriber
public final class RegistryEventHandler {

    @SubscribeEvent
    public static void onCreateRegistry(NewRegistry event) {
        new RegistryBuilder<ICustomParticleFactory>()
                .setType(ICustomParticleFactory.class)
                .setName(new ResourceLocation(CommonReference.MOD_ID, "custom_particles"))
                .create();
    }
}
