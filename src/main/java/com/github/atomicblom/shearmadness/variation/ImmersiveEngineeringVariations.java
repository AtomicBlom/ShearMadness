package com.github.atomicblom.shearmadness.variation;

import com.github.atomicblom.shearmadness.api.IVariationRegistry;
import com.github.atomicblom.shearmadness.api.ItemStackHelper;
import com.github.atomicblom.shearmadness.api.RegisterShearMadnessVariationEvent;
import com.github.atomicblom.shearmadness.library.ImmersiveEngineeringLibrary;
import com.github.atomicblom.shearmadness.api.transformation.ConveyorTransformations;
import com.github.atomicblom.shearmadness.utility.Reference;
import com.github.atomicblom.shearmadness.variation.immersiveengineering.ImmersiveEngineeringPostModelMaker;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public enum ImmersiveEngineeringVariations
{
    INSTANCE;
    @SubscribeEvent(priority = EventPriority.HIGH)
    @Optional.Method(modid = Reference.ModID.IMMERSIVE_ENGINEERING)
    public void onShearMadnessRegisterVariations(RegisterShearMadnessVariationEvent event) {
        final IVariationRegistry registry = event.getRegistry();
        registry.registerVariation(
                itemStack -> ItemStackHelper.isStackForBlock(itemStack, ImmersiveEngineeringLibrary.metalDecoration2, 0) ||
                        ItemStackHelper.isStackForBlock(itemStack, ImmersiveEngineeringLibrary.metalDecoration2, 2) ||
                        ItemStackHelper.isStackForBlock(itemStack, ImmersiveEngineeringLibrary.woodenDevice1, 3),
                new ImmersiveEngineeringPostModelMaker()
        );

        registry.registerVariation(
                itemStack -> ItemStackHelper.isStackForBlock(itemStack, ImmersiveEngineeringLibrary.conveyor),
                new ConveyorTransformations()
        );
    }
}
