package com.github.atomicblom.shearmadness.variations.chisel;

import com.github.atomicblom.shearmadness.api.IBehaviourRegistry;
import com.github.atomicblom.shearmadness.api.ItemStackHelper;
import com.github.atomicblom.shearmadness.api.behaviour.FlightBehaviour;
import com.github.atomicblom.shearmadness.api.events.IRegisterShearMadnessBehaviours;
import com.github.atomicblom.shearmadness.variations.CommonReference;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;

@SuppressWarnings({"MethodMayBeStatic", "UnnecessarilyQualifiedInnerClassAccess"})
@Mod.EventBusSubscriber(modid = CommonReference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChiselBehaviours
{
    @SubscribeEvent
    public static void onProcessIMC(InterModEnqueueEvent event) {
        InterModComms.sendTo(CommonReference.MOD_ID, CommonReference.IMCMethods.REGISTER_BEHAVIOURS, () -> (IRegisterShearMadnessBehaviours) ChiselBehaviours::registerBehaviours);
    }

    //FIXME: May as well send these as individual IMC messages rather than a big batch.
    private static void registerBehaviours(IBehaviourRegistry registry) {
        registry.registerBehaviour(
                itemStack -> ItemStackHelper.isStackForBlock(itemStack, ChiselLibrary.technical, 4) ||
                        ItemStackHelper.isStackForBlock(itemStack, ChiselLibrary.technical1, 1),
                sheep -> new FlightBehaviour(sheep, 9f, true)
        );
    }
}