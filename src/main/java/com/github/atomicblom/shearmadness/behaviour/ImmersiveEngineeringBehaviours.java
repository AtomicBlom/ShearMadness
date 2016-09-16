package com.github.atomicblom.shearmadness.behaviour;

import com.github.atomicblom.shearmadness.api.IBehaviourRegistry;
import com.github.atomicblom.shearmadness.api.ItemStackHelper;
import com.github.atomicblom.shearmadness.api.behaviour.DamageBehaviour;
import com.github.atomicblom.shearmadness.api.behaviour.ExplosiveBehaviour;
import com.github.atomicblom.shearmadness.api.behaviour.FlightBehaviour;
import com.github.atomicblom.shearmadness.api.behaviour.PlaceInvisibleBlockBehaviour;
import com.github.atomicblom.shearmadness.api.events.RegisterShearMadnessBehaviourEvent;
import com.github.atomicblom.shearmadness.configuration.Settings.Behaviours;
import com.github.atomicblom.shearmadness.library.ImmersiveEngineeringLibrary;
import com.github.atomicblom.shearmadness.utility.BlockLibrary;
import com.github.atomicblom.shearmadness.utility.ChiselLibrary;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@SuppressWarnings({"MethodMayBeStatic", "UnnecessarilyQualifiedInnerClassAccess"})
public enum ImmersiveEngineeringBehaviours
{
    INSTANCE;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    @Optional.Method(modid = "shearmadness")
    public void onShearMadnessRegisterBehaviours(RegisterShearMadnessBehaviourEvent event) {
        final IBehaviourRegistry registry = event.getRegistry();

        registry.registerBehaviour(
                itemStack -> ItemStackHelper.isStackForBlock(itemStack, ImmersiveEngineeringLibrary.clothDevice),
                sheep -> new FlightBehaviour(sheep, 1, false)
        );
    }
}
