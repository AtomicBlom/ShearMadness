package com.github.atomicblom.shearmadness.variations.chisel;

import com.github.atomicblom.shearmadness.api.IVariationRegistry;
import com.github.atomicblom.shearmadness.api.events.IRegisterShearMadnessVariations;
import com.github.atomicblom.shearmadness.api.CommonReference;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;

@SuppressWarnings({"MethodMayBeStatic", "AnonymousInnerClass"})
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = CommonReference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChiselVariations
{
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onProcessIMC(InterModEnqueueEvent event) {
        InterModComms.sendTo(CommonReference.MOD_ID, CommonReference.IMCMethods.REGISTER_VARIATIONS, () -> (IRegisterShearMadnessVariations) ChiselVariations::registerVariations);
    }

    private static void registerVariations(IVariationRegistry registry) {
        //FIXME: Requires CTM support.
//        registry.registerVariation(
//                itemStack -> CarvingUtils.getChiselRegistry().getVariation(itemStack) != null,
//                new DefaultChiselModelMaker()
//        );
    }
}
