package com.github.atomicblom.shearmadness.variations.immersiveengineering;

import com.github.atomicblom.shearmadness.api.IBehaviourRegistry;
import com.github.atomicblom.shearmadness.api.ItemStackHelper;
import com.github.atomicblom.shearmadness.api.behaviour.FlightBehaviour;
import com.github.atomicblom.shearmadness.api.events.IRegisterShearMadnessBehaviours;
import com.github.atomicblom.shearmadness.api.CommonReference;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;

@SuppressWarnings({"MethodMayBeStatic", "UnnecessarilyQualifiedInnerClassAccess"})
@Mod.EventBusSubscriber(modid = CommonReference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class IEBehaviours
{
    @SubscribeEvent
    public static void onProcessIMC(InterModEnqueueEvent event) {
        InterModComms.sendTo(CommonReference.MOD_ID, CommonReference.IMCMethods.REGISTER_BEHAVIOURS, () -> (IRegisterShearMadnessBehaviours) IEBehaviours::registerBehaviours);
    }

    //FIXME: May as well send these as individual IMC messages rather than a big batch.
    private static void registerBehaviours(IBehaviourRegistry registry) {
        registry.registerBehaviour(
                itemStack -> ItemStackHelper.isStackForBlock(itemStack, IELibrary.balloon),
                sheep -> new FlightBehaviour(sheep, 1, false)
        );
    }
}
