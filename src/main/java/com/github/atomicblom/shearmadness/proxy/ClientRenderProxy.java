package com.github.atomicblom.shearmadness.proxy;

import com.github.atomicblom.shearmadness.rendering.RenderChiselSheep;
import com.github.atomicblom.shearmadness.utility.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelSheep2;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@SuppressWarnings("unused")
public class ClientRenderProxy extends CommonRenderProxy
{
    @Override
    public void registerRenderers()
    {
        final RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        renderManager.entityRenderMap.remove(EntitySheep.class);
        renderManager.entityRenderMap.put(EntitySheep.class, new RenderChiselSheep(renderManager, new ModelSheep2(), 0.7f));
    }
}
