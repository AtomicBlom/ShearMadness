package com.github.atomicblom.shearmadness.variations.silly;

import com.github.atomicblom.shearmadness.api.IVariationRegistry;
import com.github.atomicblom.shearmadness.api.ItemStackHelper;
import com.github.atomicblom.shearmadness.api.events.RegisterShearMadnessVariationEvent;
import com.github.atomicblom.shearmadness.utility.Reference;
import com.github.atomicblom.shearmadness.variations.silly.visuals.InfiltratorModelMaker;
import com.github.atomicblom.shearmadness.utility.ChiselLibrary;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings({"MethodMayBeStatic", "AnonymousInnerClass"})
@Mod.EventBusSubscriber(Side.CLIENT)
public class SillyVarations
{
    @SubscribeEvent(priority = EventPriority.LOW)
    @Optional.Method(modid = "shearmadness")
    @SideOnly(Side.CLIENT)
    public void onShearMadnessRegisterVariations(RegisterShearMadnessVariationEvent event)
    {

        final IVariationRegistry registry = event.getRegistry();
        registry.registerVariation(
                itemStack -> ItemStackHelper.isStackForBlock(itemStack, ChiselLibrary.marbleextra) && itemStack.getItemDamage() == 7,
                new InfiltratorModelMaker()
                );
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onTextureStitchEvent(TextureStitchEvent.Pre event) {
        event.getMap().registerSprite(new ResourceLocation(Reference.MOD_ID, "chicken-nuggets"));
        event.getMap().registerSprite(new ResourceLocation(Reference.MOD_ID, "chicken-winglets"));
    }
}
