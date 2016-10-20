package com.github.atomicblom.shearmadness.variations.vanilla;

import com.github.atomicblom.shearmadness.api.IVariationRegistry;
import com.github.atomicblom.shearmadness.api.ItemStackHelper;
import com.github.atomicblom.shearmadness.api.events.RegisterShearMadnessVariationEvent;
import com.github.atomicblom.shearmadness.api.transformation.RailTransformations;
import com.github.atomicblom.shearmadness.api.transformation.StairTransformations;
import net.minecraft.block.BlockStairs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.function.Function;

@SuppressWarnings({"MethodMayBeStatic", "AnonymousInnerClass"})
@Mod.EventBusSubscriber(Side.CLIENT)
public class VanillaVariations
{
    @SubscribeEvent(priority = EventPriority.LOWEST)
    @Optional.Method(modid = "shearmadness")
    @SideOnly(Side.CLIENT)
    public static void onShearMadnessRegisterVariations(RegisterShearMadnessVariationEvent event) {
        final IVariationRegistry registry = event.getRegistry();

        //Java 7 Style Registration
        //noinspection Convert2Lambda
        registry.registerVariation(
                new Function<ItemStack, Boolean>() {
                    @Override
                    public Boolean apply(ItemStack itemStack)
                    {
                        return ItemStackHelper.isStackForBlock(
                                itemStack,
                                Blocks.RAIL,
                                Blocks.ACTIVATOR_RAIL,
                                Blocks.DETECTOR_RAIL,
                                Blocks.GOLDEN_RAIL
                        );
                    }
                },
                new RailTransformations()
        );

        registry.registerVariation(
                itemStack -> ItemStackHelper.isStackForBlockSubclassOf(
                        itemStack,
                        BlockStairs.class
                ),
                new StairTransformations()
        );
    }
}
