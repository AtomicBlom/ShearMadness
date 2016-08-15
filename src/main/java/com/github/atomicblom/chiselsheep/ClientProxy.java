package com.github.atomicblom.chiselsheep;

import com.github.atomicblom.chiselsheep.client.renderer.entity.RenderChiselSheep;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelSheep2;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntitySheep;

/**
 * Created by codew on 15/08/2016.
 */
public class ClientProxy extends CommonProxy
{
    @Override
    public void registerRenderers()
    {
        final RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        renderManager.entityRenderMap.remove(EntitySheep.class);
        renderManager.entityRenderMap.put(EntitySheep.class, new RenderChiselSheep(renderManager, new ModelSheep2(), 0.7f));
    }
}
