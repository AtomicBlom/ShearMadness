package com.github.atomicblom.shearmadness.events;

import com.github.atomicblom.shearmadness.rendering.RenderChiselSheep;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RenderingInitializationEventHandler {
    @SubscribeEvent
    public static void onModelRegistryReady(ModelRegistryEvent event) {
        EntityRendererManager renderManager = Minecraft.getInstance().getRenderManager();
        renderManager.renderers.remove(EntityType.SHEEP);
        renderManager.renderers.put(EntityType.SHEEP, new RenderChiselSheep(renderManager));
    }
}
