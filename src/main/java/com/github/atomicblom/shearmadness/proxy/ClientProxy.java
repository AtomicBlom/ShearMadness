package com.github.atomicblom.shearmadness.proxy;

import com.github.atomicblom.shearmadness.networking.CheckSheepChiseledRequestMessage;
import com.github.atomicblom.shearmadness.rendering.RenderChiselSheep;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelSheep2;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.github.atomicblom.shearmadness.ChiselSheepMod.CHANNEL;

@SuppressWarnings("MethodMayBeStatic")
public class ClientProxy extends CommonProxy
{
    @Override
    public void registerRenderers()
    {
        final RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        renderManager.entityRenderMap.remove(EntitySheep.class);
        renderManager.entityRenderMap.put(EntitySheep.class, new RenderChiselSheep(renderManager, new ModelSheep2(), 0.7f));
    }

    @SubscribeEvent
    public void onEntityJoinWorldEvent(EntityJoinWorldEvent event)
    {
        final Entity entity = event.getEntity();
        if (entity instanceof EntitySheep)
        {
            CHANNEL.sendToServer(new CheckSheepChiseledRequestMessage(entity));
        }
    }
}
