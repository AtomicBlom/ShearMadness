package com.github.atomicblom.shearmadness.variations.chancecubes;

import com.github.atomicblom.shearmadness.api.IBehaviourRegistry;
import com.github.atomicblom.shearmadness.api.ItemStackHelper;
import com.github.atomicblom.shearmadness.api.events.IRegisterShearMadnessBehaviours;
import com.github.atomicblom.shearmadness.variations.CommonReference;
import com.github.atomicblom.shearmadness.variations.chancecubes.behaviour.ChanceCubeBehaviour;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;

@SuppressWarnings({"MethodMayBeStatic", "UnnecessarilyQualifiedInnerClassAccess"})
@Mod.EventBusSubscriber(modid = CommonReference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChanceCubesBehaviours
{
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onProcessIMC(InterModEnqueueEvent event) {
        InterModComms.sendTo(CommonReference.MOD_ID, CommonReference.IMCMethods.REGISTER_BEHAVIOURS, () -> (IRegisterShearMadnessBehaviours) ChanceCubesBehaviours::registerBehaviours);
    }

    //FIXME: May as well send these as individual IMC messages rather than a big batch.
    private static void registerBehaviours(IBehaviourRegistry registry) {

        registry.registerBehaviour(
                itemStack -> ItemStackHelper.isStackForBlock(itemStack, ChanceCubesLibrary.chance_cube),
                (sheep) -> new ChanceCubeBehaviour(sheep, ChanceCubeType.NORMAL)
        );

        registry.registerBehaviour(
                itemStack -> ItemStackHelper.isStackForBlock(itemStack, ChanceCubesLibrary.chance_icosadedron),
                (sheep) -> new ChanceCubeBehaviour(sheep, ChanceCubeType.ICOSAHEDRON)
        );

        registry.registerBehaviour(
                itemStack -> ItemStackHelper.isStackForBlock(itemStack, ChanceCubesLibrary.chance_cube_giant_compact, ChanceCubesLibrary.chance_cube_giant),
                (sheep) -> new ChanceCubeBehaviour(sheep, ChanceCubeType.GIANT)
        );
    }
}
