package com.github.atomicblom.shearmadness.variations.immersiveengineering;

import com.github.atomicblom.shearmadness.api.IVariationRegistry;
import com.github.atomicblom.shearmadness.api.ItemStackHelper;
import com.github.atomicblom.shearmadness.api.events.RegisterShearMadnessVariationEvent;
import com.github.atomicblom.shearmadness.api.transformation.ConveyorTransformations;
import com.github.atomicblom.shearmadness.variations.immersiveengineering.visuals.ImmersiveEngineeringPostModelMaker;
import com.github.atomicblom.shearmadness.variations.immersiveengineering.visuals.ImmersiveEngineeringWallMountModelMaker;
import com.github.atomicblom.shearmadness.variations.immersiveengineering.visuals.ClothDeviceTransformations;
import com.github.atomicblom.shearmadness.variations.immersiveengineering.visuals.TeslaCoilTransformations;
import com.github.atomicblom.shearmadness.utility.Reference;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class IEVariations
{
    @SubscribeEvent(priority = EventPriority.HIGH)
    @Optional.Method(modid = Reference.ModID.IMMERSIVE_ENGINEERING)
    public void onShearMadnessRegisterVariations(RegisterShearMadnessVariationEvent event) {
        final IVariationRegistry registry = event.getRegistry();
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
