package com.github.atomicblom.shearmadness.events;

import com.github.atomicblom.shearmadness.Chiseling;
import com.github.atomicblom.shearmadness.Shearing;
import com.github.atomicblom.shearmadness.ai.SheepBehaviourAI;
import com.github.atomicblom.shearmadness.api.Capability;
import com.github.atomicblom.shearmadness.api.ItemStackHelper;
import com.github.atomicblom.shearmadness.api.capability.IChiseledSheepCapability;
import com.github.atomicblom.shearmadness.api.events.ShearMadnessSheepKilledEvent;
import com.github.atomicblom.shearmadness.api.events.ShearMadnessSpecialInteractionEvent;
import com.github.atomicblom.shearmadness.utility.ChiselLibrary;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.List;

@Mod.EventBusSubscriber
public class EntityEventHandler
{
	@SuppressWarnings({"ConstantConditions", "MethodWithMoreThanThreeNegations"})
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onPlayerInteractionWithEntity(PlayerInteractEvent.EntityInteract event)
	{
		//Process for shearing a sheep
		if (event.getWorld().isRemote) return;
		if (!(event.getTarget() instanceof EntitySheep)) return;
		final ItemStack itemStack = event.getItemStack();
		if (itemStack.isEmpty()) {
			checkSpecialSheepInteraction(event);
			return;
		}
		if (!(itemStack.getItem() instanceof ItemShears)) return;

		final EntitySheep sheep = (EntitySheep) event.getTarget();
		if (!sheep.isShearable(itemStack, event.getWorld(), event.getPos())) return;

		final IChiseledSheepCapability capability = sheep.getCapability(Capability.CHISELED_SHEEP, null);
		if (capability == null) return;
		if (!capability.isChiseled()) return;

		//Ok, we have a chiseled sheep, cancel vanilla.
		event.setCanceled(true);

		Shearing.shearSheep(itemStack, sheep, capability);
	}

	private static void checkSpecialSheepInteraction(PlayerInteractEvent.EntityInteract event)
	{
		final EntitySheep sheep = (EntitySheep) event.getTarget();
		final IChiseledSheepCapability capability = sheep.getCapability(Capability.CHISELED_SHEEP, null);
		if (capability == null) return;
		if (!capability.isChiseled()) return;
		if (event.getHand() != EnumHand.MAIN_HAND) return;

		final EntityPlayer entityPlayer = event.getEntityPlayer();

		MinecraftForge.EVENT_BUS.post(new ShearMadnessSpecialInteractionEvent(event.getWorld(), entityPlayer, sheep, capability));
	}

	@SuppressWarnings("BooleanVariableAlwaysNegated")
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onAttack(AttackEntityEvent event)
	{
		//Process for chiseling a sheep
		final Entity sheep = event.getTarget();
		if (sheep == null) return;

		final EntityPlayer entityPlayer = event.getEntityPlayer();
		ItemStack activeStack = entityPlayer.inventory.getCurrentItem();
		boolean attackedWithChisel = false;
		if (ChiselLibrary.isChisel(activeStack.getItem()))
		{
			attackedWithChisel = true;
		} else
		{
			activeStack = entityPlayer.inventory.offHandInventory.get(0);
			if (ChiselLibrary.isChisel(activeStack.getItem()))
			{
				attackedWithChisel = true;
			}
		}
		if (!attackedWithChisel)
		{
			return;
		}

		if (!sheep.hasCapability(Capability.CHISELED_SHEEP, null)) {
			return;
		}

		event.setCanceled(true);

		Chiseling.chiselSheep(sheep, entityPlayer, activeStack);
	}

	@SubscribeEvent
	public static void onLivingDrop(LivingDropsEvent event) {

		final Entity entity = event.getEntity();
		if (entity.hasCapability(Capability.CHISELED_SHEEP, null)) {
			final IChiseledSheepCapability capability = entity.getCapability(Capability.CHISELED_SHEEP, null);
			assert capability != null;
			if (capability.isChiseled())
			{
				final List<EntityItem> drops = event.getDrops();
				final ItemStack chiselItemStack = capability.getChiselItemStack();
				final Item chiselItem = chiselItemStack.getItem();

				drops.removeIf(entityItem -> ItemStackHelper.isStackForBlock(entityItem.getItem(), Blocks.WOOL));

				drops.add(new EntityItem(entity.world, entity.posX, entity.posY, entity.posZ, chiselItemStack.copy()));

				MinecraftForge.EVENT_BUS.post(new ShearMadnessSheepKilledEvent(
						drops,
						event.getSource(),
						event.getLootingLevel(),
						chiselItemStack,
						entity
				));
			}
		}
	}

	@SubscribeEvent
	public static void onCommonEntityJoinWorldEvent(EntityJoinWorldEvent event)
	{
		final Entity entity = event.getEntity();
		if (entity instanceof EntityLiving && entity.hasCapability(Capability.CHISELED_SHEEP, null))
		{
			final EntityLiving livingEntity = (EntityLiving) event.getEntity();
			final EntityAITasks tasks = livingEntity.tasks;
			tasks.addTask(0, new SheepBehaviourAI(livingEntity));
		}
	}

	@SubscribeEvent
	public static void onEntityLivingDeathEvent(LivingDeathEvent event) {
		final Entity entity = event.getEntity();
		if (entity instanceof EntityLiving && entity.hasCapability(Capability.CHISELED_SHEEP, null)) {
			final EntityLiving livingEntity = (EntityLiving) entity;
			livingEntity.tasks.taskEntries
					.stream()
					.filter(taskEntry -> taskEntry.action instanceof SheepBehaviourAI)
					.forEach(taskEntry -> ((SheepBehaviourAI) taskEntry.action).onDeath());
		}
	}
}
