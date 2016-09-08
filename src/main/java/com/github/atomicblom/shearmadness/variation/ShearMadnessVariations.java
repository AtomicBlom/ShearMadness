package com.github.atomicblom.shearmadness.variation;

import com.github.atomicblom.shearmadness.api.IVariationRegistry;
import com.github.atomicblom.shearmadness.api.ItemStackHelper;
import com.github.atomicblom.shearmadness.api.events.RegisterShearMadnessVariationEvent;
import com.github.atomicblom.shearmadness.api.transformation.RailTransformations;
import com.github.atomicblom.shearmadness.api.transformation.StairTransformations;
import com.github.atomicblom.shearmadness.modelmaker.DefaultChiselModelMaker;
import net.minecraft.block.BlockStairs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.chisel.api.carving.CarvingUtils;

import java.util.function.Function;

@SuppressWarnings({"MethodMayBeStatic", "AnonymousInnerClass"})
public enum ShearMadnessVariations
{
    INSTANCE;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    @Optional.Method(modid = "shearmadness")
    @SideOnly(Side.CLIENT)
    public void onShearMadnessRegisterVariations(RegisterShearMadnessVariationEvent event) {
        final IVariationRegistry registry = event.getRegistry();

        //Java 8 Style Registration
        registry.registerVariation(
                itemStack -> CarvingUtils.getChiselRegistry().getVariation(itemStack) != null,
                new DefaultChiselModelMaker()
        );

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
