package com.github.atomicblom.shearmadness.events;

import com.github.atomicblom.shearmadness.capability.CapabilityProvider;
import com.github.atomicblom.shearmadness.variations.CommonReference;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class CapabilityEventHandler
{
	@SubscribeEvent
	public static void onCapabilityAttaching(AttachCapabilitiesEvent<Entity> event)
	{
		if (event.getObject().getClass().equals(SheepEntity.class))
		{
			event.addCapability(new ResourceLocation(CommonReference.MOD_ID, "chiseledSheep"), new CapabilityProvider());
		}
	}
}
