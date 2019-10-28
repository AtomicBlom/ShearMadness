package com.github.atomicblom.shearmadness.events;

import com.github.atomicblom.shearmadness.ai.SheepBehaviourAI;
import com.github.atomicblom.shearmadness.api.Capability;
import com.github.atomicblom.shearmadness.api.behaviour.DamageBehaviour;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

@Mod.EventBusSubscriber
public final class SheepAttackedEventHandler {

    @SubscribeEvent
    public static void onSheepAttacked(LivingAttackEvent event) {
        final LivingEntity entityLiving = event.getEntityLiving();
        final DamageSource source = event.getSource();
        if (entityLiving == null) { return; }
        entityLiving.getCapability(Capability.CHISELED_SHEEP).ifPresent(capability -> {
            ((MobEntity) entityLiving).goalSelector.getRunningGoals()
                    .filter(goal -> goal.getGoal() instanceof SheepBehaviourAI)
                    .map(goal -> (SheepBehaviourAI) goal.getGoal())
                    .map(behaviourAi -> behaviourAi.getBehaviour(DamageBehaviour.class))
                    .filter(Objects::nonNull)
                    .filter(damageBehaviour -> damageBehaviour.getDamageSource() == source)
                    .limit(1)
                    .forEach(behaviour -> event.setCanceled(true));
        });
    }
}
