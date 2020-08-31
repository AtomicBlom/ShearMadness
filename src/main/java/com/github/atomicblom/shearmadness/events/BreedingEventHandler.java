package com.github.atomicblom.shearmadness.events;

import com.github.atomicblom.shearmadness.api.Capability;
import com.github.atomicblom.shearmadness.configuration.BreedingBehaviour;
import com.github.atomicblom.shearmadness.configuration.Settings;
import com.github.atomicblom.shearmadness.api.CommonReference;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CommonReference.MOD_ID)
public class BreedingEventHandler {
    @SubscribeEvent
    public static void onBreedingEvent(BabyEntitySpawnEvent event) {
        if (Settings.Behaviours.getBreedingBehaviour() != BreedingBehaviour.SimpleBreeding) {
            return;
        }
        final LivingEntity parentA = event.getParentA();
        final LivingEntity parentB = event.getParentB();
        final AgeableEntity child = event.getChild();
        if (child == null) return;

        parentA.getCapability(Capability.CHISELED_SHEEP).ifPresent(parentACapability ->
                parentB.getCapability(Capability.CHISELED_SHEEP).ifPresent(parentBCapability ->
                        child.getCapability(Capability.CHISELED_SHEEP).ifPresent(childCapability -> {
                                final ItemStack parentAItem = parentACapability.getChiselItemStack();
                                final ItemStack parentBItem = parentBCapability.getChiselItemStack();

                                if (event.getParentA().world.rand.nextDouble() >= 0.5) {
                                    if (!parentAItem.isEmpty()) {
                                        childCapability.chisel(parentAItem);
                                    }
                                } else {
                                    if (!parentBItem.isEmpty()) {
                                        childCapability.chisel(parentBItem);
                                    }
                                }
                        })
                )
        );
    }
}
