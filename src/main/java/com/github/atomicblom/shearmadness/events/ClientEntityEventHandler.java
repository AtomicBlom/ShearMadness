package com.github.atomicblom.shearmadness.events;

import com.github.atomicblom.shearmadness.networking.CheckSheepChiseledRequestMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import static com.github.atomicblom.shearmadness.ShearMadnessMod.CHANNEL;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientEntityEventHandler
{

	@SubscribeEvent
	public static void onEntityJoinWorldEvent(EntityJoinWorldEvent event)
	{
		final Entity entity = event.getEntity();
		if (entity instanceof SheepEntity)
		{
			CHANNEL.sendToServer(new CheckSheepChiseledRequestMessage(entity));
		}
	}
}
