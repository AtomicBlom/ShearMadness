package com.github.atomicblom.shearmadness.events;

import com.github.atomicblom.shearmadness.api.Capability;
import com.github.atomicblom.shearmadness.api.capability.IChiseledSheepCapability;
import com.github.atomicblom.shearmadness.configuration.BreedingBehaviour;
import com.github.atomicblom.shearmadness.configuration.Settings;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public final class BreedingEventHandler {

    @SubscribeEvent
    public static void onBreedingEvent(BabyEntitySpawnEvent event) {
        if (Settings.Behaviours.getBreedingBehaviour() != BreedingBehaviour.SimpleBreeding) {
            return;
        }
        final EntityLiving parentA = event.getParentA();
        final EntityLiving parentB = event.getParentB();
        final EntityAgeable child = event.getChild();
        if (child == null ||
                !parentA.hasCapability(Capability.CHISELED_SHEEP, null) ||
                !parentB.hasCapability(Capability.CHISELED_SHEEP, null) ||
                !child.hasCapability(Capability.CHISELED_SHEEP, null)) {
            return;
        }
        final IChiseledSheepCapability parentACapability = parentA.getCapability(Capability.CHISELED_SHEEP, null);
        final IChiseledSheepCapability parentBCapability = parentB.getCapability(Capability.CHISELED_SHEEP, null);
        final IChiseledSheepCapability childCapability = child.getCapability(Capability.CHISELED_SHEEP, null);

        assert parentACapability != null;
        assert parentBCapability != null;
        assert childCapability != null;

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
    }
}
