package com.github.atomicblom.shearmadness;

import com.github.atomicblom.shearmadness.variations.CommonReference;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
public class Events {
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onTextureStitchEvent(TextureStitchEvent.Pre event) {
        event.getMap().registerSprite(new ResourceLocation(CommonReference.MOD_ID, "bad-render"));
    }
}
