package com.github.atomicblom.shearmadness.variations.silly;

import com.github.atomicblom.shearmadness.api.IVariationRegistry;
import com.github.atomicblom.shearmadness.api.ItemStackHelper;
import com.github.atomicblom.shearmadness.api.events.IRegisterShearMadnessVariations;
import com.github.atomicblom.shearmadness.api.CommonReference;
import com.github.atomicblom.shearmadness.variations.silly.transformations.WeaponTransformations;
import com.github.atomicblom.shearmadness.variations.silly.visuals.InfiltratorModelMaker;
import com.github.atomicblom.shearmadness.variations.silly.visuals.TestModelMaker;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;

@SuppressWarnings({"MethodMayBeStatic", "AnonymousInnerClass"})
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = CommonReference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SillyVariations
{
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onProcessIMC(InterModEnqueueEvent event) {
        InterModComms.sendTo(CommonReference.MOD_ID, CommonReference.IMCMethods.REGISTER_VARIATIONS, () -> (IRegisterShearMadnessVariations) SillyVariations::registerVariations);
    }

    private static void registerVariations(IVariationRegistry registry) {
        registry.registerVariation(
                itemStack -> ItemStackHelper.isStackForBlock(itemStack, ChiselLibrary.marble2) && itemStack.getDamage() == 7,
                new InfiltratorModelMaker()
        );

        registry.registerVariation(
                itemStack -> itemStack.getItem() == Items.FEATHER,
                new InfiltratorModelMaker()
        );

        registry.registerVariation(
                itemStack -> itemStack.getItem() == Items.BOOK,
                new TestModelMaker()
        );

        registry.registerVariation(
                itemStack -> itemStack.getItem() instanceof SwordItem,
                new WeaponTransformations()
        );
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onTextureStitchEvent(TextureStitchEvent.Pre event) {
        if (!PlayerContainer.LOCATION_BLOCKS_TEXTURE.equals(event.getMap().getTextureLocation())) return;
        event.addSprite(Reference.Textures.CHICKEN_NUGGETS);
        event.addSprite(Reference.Textures.CHICKEN_WINGLETS);
    }
}
