package com.github.atomicblom.shearmadness.events;

import com.github.atomicblom.shearmadness.ai.SheepBehaviourAI;
import com.github.atomicblom.shearmadness.api.Capability;
import com.github.atomicblom.shearmadness.api.behaviour.DamageBehaviour;
import com.github.atomicblom.shearmadness.api.capability.IChiseledSheepCapability;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public final class SheepAttackedEventHandler {

    @SubscribeEvent
    public static void onSheepAttacked(LivingAttackEvent event) {
        final EntityLivingBase entityLiving = event.getEntityLiving();
        final DamageSource source = event.getSource();
        if (!entityLiving.hasCapability(Capability.CHISELED_SHEEP, null)) { return; }
        if (!(entityLiving instanceof EntityLiving)) { return; }
        final IChiseledSheepCapability capability = entityLiving.getCapability(Capability.CHISELED_SHEEP, null);
        assert capability != null;

        for (EntityAITasks.EntityAITaskEntry taskEntry : ((EntityLiving) entityLiving).tasks.taskEntries) {
            final EntityAIBase behaviourAI = taskEntry.action;
            if (behaviourAI instanceof SheepBehaviourAI) {
                final DamageBehaviour behaviour = (DamageBehaviour)((SheepBehaviourAI) behaviourAI).getBehaviour(DamageBehaviour.class);
                if (behaviour != null) {
                    if (behaviour.getDamageSource() == source) {
                        event.setCanceled(true);
                        return;
                    }
                }
            }
        }
    }
}
