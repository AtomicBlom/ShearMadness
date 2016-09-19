package com.github.atomicblom.shearmadness.variations.immersiveengineering;

import com.github.atomicblom.shearmadness.api.IBehaviourRegistry;
import com.github.atomicblom.shearmadness.api.ItemStackHelper;
import com.github.atomicblom.shearmadness.api.behaviour.FlightBehaviour;
import com.github.atomicblom.shearmadness.api.events.RegisterShearMadnessBehaviourEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@SuppressWarnings({"MethodMayBeStatic", "UnnecessarilyQualifiedInnerClassAccess"})
@Mod.EventBusSubscriber
public class IEBehaviours
{
    @SubscribeEvent(priority = EventPriority.LOWEST)
    @Optional.Method(modid = "shearmadness")
    public static void onShearMadnessRegisterBehaviours(RegisterShearMadnessBehaviourEvent event) {
        final IBehaviourRegistry registry = event.getRegistry();

        registry.registerBehaviour(
                itemStack -> ItemStackHelper.isStackForBlock(itemStack, IELibrary.clothDevice),
                sheep -> new FlightBehaviour(sheep, 1, false)
        );
    }
}
