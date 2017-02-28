package com.github.atomicblom.shearmadness.variations.chancecubes;

import com.github.atomicblom.shearmadness.api.IBehaviourRegistry;
import com.github.atomicblom.shearmadness.api.ItemStackHelper;
import com.github.atomicblom.shearmadness.variations.chancecubes.behaviour.ChanceCubeBehaviour;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.github.atomicblom.shearmadness.api.events.RegisterShearMadnessBehaviourEvent;

@SuppressWarnings({"MethodMayBeStatic", "UnnecessarilyQualifiedInnerClassAccess"})
@Mod.EventBusSubscriber
public class ChanceCubesBehaviours {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    @Optional.Method(modid = ChanceCubesReference.CHANCE_CUBES)
    public static void onShearMadnessRegisterBehaviours(RegisterShearMadnessBehaviourEvent event) {
        IBehaviourRegistry registry = event.getRegistry();
        registry.registerBehaviour((itemStack) -> ItemStackHelper.isStackForBlock(itemStack, ChanceCubesLibrary.chance_cube),
                ChanceCubeBehaviour::new);
    }
}
