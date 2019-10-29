package com.github.atomicblom.shearmadness.events;

import com.github.atomicblom.shearmadness.utility.Reference;
import com.github.atomicblom.shearmadness.capability.CapabilityProvider;
import com.github.atomicblom.shearmadness.api.CommonReference;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CommonReference.MOD_ID)
public class CapabilityEventHandler
{
	@SubscribeEvent
	public static void onCapabilityAttaching(AttachCapabilitiesEvent<Entity> event)
	{
		if (event.getObject().getClass().equals(SheepEntity.class))
		{
			event.addCapability(Reference.Capability.CHISELED_SHEEP, new CapabilityProvider());
		}
	}
}
