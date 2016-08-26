package com.github.atomicblom.shearmadness.proxy;

import com.github.atomicblom.shearmadness.api.RegisterShearMadnessVariationEvent;
import com.github.atomicblom.shearmadness.api.VariationRegistry;
import com.github.atomicblom.shearmadness.networking.CheckSheepChiseledRequestMessage;
import com.github.atomicblom.shearmadness.rendering.RenderChiselSheep;
import com.github.atomicblom.shearmadness.utility.ShearMadnessVariations;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelSheep2;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.github.atomicblom.shearmadness.ShearMadnessMod.CHANNEL;

@SuppressWarnings({"MethodMayBeStatic", "unused"})
public class ClientRenderProxy extends CommonRenderProxy
{
    @Override
    public void registerRenderers()
    {
        final RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        renderManager.entityRenderMap.remove(EntitySheep.class);
        renderManager.entityRenderMap.put(EntitySheep.class, new RenderChiselSheep(renderManager, new ModelSheep2(), 0.7f));
    }

    @Override
    public void registerVariants()
    {
        MinecraftForge.EVENT_BUS.register(ShearMadnessVariations.INSTANCE);
    }
}
