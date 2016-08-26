package com.github.atomicblom.shearmadness.utility;

import com.github.atomicblom.shearmadness.api.IModelMaker;
import com.github.atomicblom.shearmadness.api.IVariationRegistry;
import com.github.atomicblom.shearmadness.api.RegisterShearMadnessVariationEvent;
import com.github.atomicblom.shearmadness.modelmaker.DefaultChiselModelMaker;
import com.github.atomicblom.shearmadness.transformation.RailQuadrupedTransformations;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.Optional.Method;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import team.chisel.api.carving.CarvingUtils;
import java.util.function.Function;

@SuppressWarnings({"MethodMayBeStatic", "AnonymousInnerClass"})
public enum ShearMadnessVariations
{
    INSTANCE;

    private static final IModelMaker DefaultChiselModelMaker = new DefaultChiselModelMaker();

    @SubscribeEvent
    @Method(modid = "shearmadness")
    public void onShearMadnessRegisterVariations(RegisterShearMadnessVariationEvent event) {
        final IVariationRegistry registry = event.getRegistry();

        //Java 8 Style Registration
        registry.registerVariation(
                itemStack -> CarvingUtils.getChiselRegistry().getVariation(itemStack) != null,
                DefaultChiselModelMaker
        );

        //Java 7 Style Registration
        //noinspection Convert2Lambda
        registry.registerVariation(
                new Function<ItemStack, Boolean>() {
                    @Override
                    public Boolean apply(ItemStack itemStack)
                    {
                        final Item item = itemStack.getItem();
                        if (item instanceof ItemBlock) {
                            if (((ItemBlock) item).block == Blocks.RAIL) {
                                return true;
                            }
                        }
                        return false;
                    }
                },
                new RailQuadrupedTransformations()
        );
    }
}
