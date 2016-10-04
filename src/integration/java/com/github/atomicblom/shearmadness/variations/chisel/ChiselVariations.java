package com.github.atomicblom.shearmadness.variations.chisel;

import com.github.atomicblom.shearmadness.api.IVariationRegistry;
import com.github.atomicblom.shearmadness.api.events.RegisterShearMadnessVariationEvent;
import com.github.atomicblom.shearmadness.variations.chisel.visuals.DefaultChiselModelMaker;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.chisel.api.carving.CarvingUtils;

@SuppressWarnings({"MethodMayBeStatic", "AnonymousInnerClass"})
@Mod.EventBusSubscriber(Side.CLIENT)
public class ChiselVariations
{
    @SubscribeEvent(priority = EventPriority.LOWEST)
    @Optional.Method(modid = "shearmadness")
    @SideOnly(Side.CLIENT)
    public static void onShearMadnessRegisterVariations(RegisterShearMadnessVariationEvent event) {
        final IVariationRegistry registry = event.getRegistry();

        //Java 8 Style Registration
        registry.registerVariation(
                itemStack -> CarvingUtils.getChiselRegistry().getVariation(itemStack) != null,
                new DefaultChiselModelMaker()
        );
    }
}
