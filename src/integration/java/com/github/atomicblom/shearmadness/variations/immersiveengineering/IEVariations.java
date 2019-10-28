package com.github.atomicblom.shearmadness.variations.immersiveengineering;

import com.github.atomicblom.shearmadness.api.IVariationRegistry;
import com.github.atomicblom.shearmadness.api.ItemStackHelper;
import com.github.atomicblom.shearmadness.api.events.IRegisterShearMadnessVariations;
import com.github.atomicblom.shearmadness.api.transformation.ConveyorTransformations;
import com.github.atomicblom.shearmadness.variations.CommonReference;
import com.github.atomicblom.shearmadness.variations.immersiveengineering.visuals.ImmersiveEngineeringPostModelMaker;
import com.github.atomicblom.shearmadness.variations.immersiveengineering.visuals.ImmersiveEngineeringWallMountModelMaker;
import com.github.atomicblom.shearmadness.variations.immersiveengineering.visuals.ClothDeviceTransformations;
import com.github.atomicblom.shearmadness.variations.immersiveengineering.visuals.TeslaCoilTransformations;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;

@SuppressWarnings({"MethodMayBeStatic", "AnonymousInnerClass"})
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = CommonReference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class IEVariations
{
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onProcessIMC(InterModEnqueueEvent event) {
        InterModComms.sendTo(CommonReference.MOD_ID, CommonReference.IMCMethods.REGISTER_VARIATIONS, () -> (IRegisterShearMadnessVariations) IEVariations::registerVariations);
    }

    private static void registerVariations(IVariationRegistry registry) {
        registry.registerVariation(
                itemStack -> ItemStackHelper.isStackForBlock(itemStack, IELibrary.metalDecoration2, 0) ||
                        ItemStackHelper.isStackForBlock(itemStack, IELibrary.metalDecoration2, 2) ||
                        ItemStackHelper.isStackForBlock(itemStack, IELibrary.woodenDevice1, 3),
                new ImmersiveEngineeringPostModelMaker()
        );

        registry.registerVariation(
                itemStack ->
                        ItemStackHelper.isStackForBlock(itemStack, IELibrary.metalDecoration2, 1) ||
                        ItemStackHelper.isStackForBlock(itemStack, IELibrary.metalDecoration2, 3) ||
                        ItemStackHelper.isStackForBlock(itemStack, IELibrary.woodenDevice1, 4),
                new ImmersiveEngineeringWallMountModelMaker()
        );

        registry.registerVariation(
                itemStack -> ItemStackHelper.isStackForBlock(itemStack, IELibrary.conveyor),
                new ConveyorTransformations()
        );

        registry.registerVariation(
                itemStack -> ItemStackHelper.isStackForBlock(itemStack, IELibrary.clothDevice),
                new ClothDeviceTransformations()
        );

        registry.registerVariation(
                itemStack -> ItemStackHelper.isStackForBlock(itemStack, IELibrary.metalDevice1, 8),
                new TeslaCoilTransformations(() -> -28)
        );

        registry.registerVariation(
                itemStack -> ItemStackHelper.isStackForBlock(itemStack, IELibrary.connector, 11),
                new TeslaCoilTransformations(() -> -15)
        );
    }
}
