package com.github.atomicblom.shearmadness.variations.vanilla;

import com.github.atomicblom.shearmadness.api.IVariationRegistry;
import com.github.atomicblom.shearmadness.api.ItemStackHelper;

import com.github.atomicblom.shearmadness.api.events.IRegisterShearMadnessVariations;
import com.github.atomicblom.shearmadness.api.transformation.RailTransformations;
import com.github.atomicblom.shearmadness.api.transformation.StairTransformations;
import com.github.atomicblom.shearmadness.api.CommonReference;
import com.github.atomicblom.shearmadness.variations.vanilla.visuals.CraftingTableModelMaker;
import net.minecraft.block.Blocks;
import net.minecraft.block.StairsBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;

@SuppressWarnings({"MethodMayBeStatic", "AnonymousInnerClass"})
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = CommonReference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class VanillaVariations
{
    @SubscribeEvent
    public static void onProcessIMC(InterModEnqueueEvent event) {
        InterModComms.sendTo(CommonReference.MOD_ID, CommonReference.IMCMethods.REGISTER_VARIATIONS, () -> (IRegisterShearMadnessVariations) VanillaVariations::registerVariations);
    }

    public static void registerVariations(IVariationRegistry registry) {
        registry.registerVariation(
                itemStack -> ItemStackHelper.isStackForBlock(
                        itemStack,
                        Blocks.RAIL,
                        Blocks.ACTIVATOR_RAIL,
                        Blocks.DETECTOR_RAIL,
                        Blocks.POWERED_RAIL
                ),
                new RailTransformations()
        );

        registry.registerVariation(
                itemStack -> ItemStackHelper.isStackForBlockSubclassOf(
                        itemStack,
                        StairsBlock.class
                ),
                new StairTransformations()
        );

        registry.registerVariation(
                itemStack -> ItemStackHelper.isStackForBlock(itemStack, Blocks.CRAFTING_TABLE),
                new CraftingTableModelMaker()
        );
    }
}
