package com.github.atomicblom.shearmadness.variations.immersiveengineering;

import com.github.atomicblom.shearmadness.api.IVariationRegistry;
import com.github.atomicblom.shearmadness.api.ItemStackHelper;
import com.github.atomicblom.shearmadness.api.events.IRegisterShearMadnessVariations;
import com.github.atomicblom.shearmadness.api.transformation.ConveyorTransformations;
import com.github.atomicblom.shearmadness.api.CommonReference;
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
                itemStack ->
                        ItemStackHelper.isStackForBlock(itemStack, IELibrary.alu_post) ||
                        ItemStackHelper.isStackForBlock(itemStack, IELibrary.steel_post) ||
                        ItemStackHelper.isStackForBlock(itemStack, IELibrary.treated_post),
                new ImmersiveEngineeringPostModelMaker()
        );

        registry.registerVariation(
                itemStack ->
                        ItemStackHelper.isStackForBlock(itemStack, IELibrary.alu_wallmount) ||
                        ItemStackHelper.isStackForBlock(itemStack, IELibrary.steel_wallmount) ||
                        ItemStackHelper.isStackForBlock(itemStack, IELibrary.treated_wallmount),
                new ImmersiveEngineeringWallMountModelMaker()
        );

        registry.registerVariation(
                itemStack ->
                        ItemStackHelper.isStackForBlock(itemStack, IELibrary.conveyor_basic) ||
                        ItemStackHelper.isStackForBlock(itemStack, IELibrary.conveyor_redstone) ||
                        ItemStackHelper.isStackForBlock(itemStack, IELibrary.conveyor_dropper) ||
                        ItemStackHelper.isStackForBlock(itemStack, IELibrary.conveyor_splitter) ||
                        ItemStackHelper.isStackForBlock(itemStack, IELibrary.conveyor_extract) ||
                        ItemStackHelper.isStackForBlock(itemStack, IELibrary.conveyor_covered),
                new ConveyorTransformations()
        );

        registry.registerVariation(
                itemStack -> ItemStackHelper.isStackForBlock(itemStack, IELibrary.balloon),
                new ClothDeviceTransformations()
        );

        registry.registerVariation(
                itemStack -> ItemStackHelper.isStackForBlock(itemStack, IELibrary.tesla_coil),
                new TeslaCoilTransformations(() -> 0)
        );

        registry.registerVariation(
                itemStack ->
                        ItemStackHelper.isStackForBlock(itemStack, IELibrary.connector_lv) ||
                        ItemStackHelper.isStackForBlock(itemStack, IELibrary.connector_mv) ||
                        ItemStackHelper.isStackForBlock(itemStack, IELibrary.connector_hv) ||
                        ItemStackHelper.isStackForBlock(itemStack, IELibrary.connector_structural) ||
                        ItemStackHelper.isStackForBlock(itemStack, IELibrary.connector_redstone) ||
                        ItemStackHelper.isStackForBlock(itemStack, IELibrary.connector_probe) ||
                        ItemStackHelper.isStackForBlock(itemStack, IELibrary.connector_bundled),
                new TeslaCoilTransformations(() -> -8)
        );
    }
}
