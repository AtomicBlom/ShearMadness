package com.github.atomicblom.shearmadness.events;

import com.github.atomicblom.shearmadness.ShearMadnessMod;
import com.github.atomicblom.shearmadness.networking.CheckSheepChiseledRequestMessage;
import com.github.atomicblom.shearmadness.api.CommonReference;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.concurrent.ConcurrentLinkedDeque;

@Mod.EventBusSubscriber(modid = CommonReference.MOD_ID, value = Dist.CLIENT)
public class ClientEntityEventHandler {

    private static ConcurrentLinkedDeque<CheckSheepChiseledRequestMessage> pendingChecks = new ConcurrentLinkedDeque<>();

    @SubscribeEvent
    public static void onPlayerJoinedWorldEvent(ClientPlayerNetworkEvent.LoggedInEvent event) {
        while (!pendingChecks.isEmpty()) {
            ShearMadnessMod.CHANNEL.send(PacketDistributor.SERVER.noArg(), pendingChecks.removeFirst());
        }
    }

    @SubscribeEvent
    public static void onEntityJoinWorldEvent(EntityJoinWorldEvent event) {
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            final Entity entity = event.getEntity();
            if (entity instanceof SheepEntity) {
                CheckSheepChiseledRequestMessage message = new CheckSheepChiseledRequestMessage((SheepEntity) entity);
                if (Minecraft.getInstance().player == null) {
                    pendingChecks.add(message);
                } else {
                    ShearMadnessMod.CHANNEL.send(PacketDistributor.SERVER.noArg(), message);
                }
            }
        });
    }
}
