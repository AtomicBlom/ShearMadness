package com.github.atomicblom.shearmadness;

import com.github.atomicblom.shearmadness.utility.Reference;
import com.github.atomicblom.shearmadness.variations.CommonReference;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CommonReference.MOD_ID, value=Dist.CLIENT, bus=Mod.EventBusSubscriber.Bus.MOD)
public class Events {
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onAtlasStitch(TextureStitchEvent.Pre event) {
        event.addSprite(new ResourceLocation("shearmadness:bad_render"));
    }
}
