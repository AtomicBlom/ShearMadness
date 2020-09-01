package com.github.atomicblom.shearmadness.events;

import com.github.atomicblom.shearmadness.Chiseling;
import com.github.atomicblom.shearmadness.ShearMadnessMod;
import com.github.atomicblom.shearmadness.Shearing;
import com.github.atomicblom.shearmadness.ai.SheepBehaviourAI;
import com.github.atomicblom.shearmadness.api.Capability;
import com.github.atomicblom.shearmadness.api.CommonReference;
import com.github.atomicblom.shearmadness.api.capability.IChiseledSheepCapability;
import com.github.atomicblom.shearmadness.api.events.ShearMadnessSheepKilledEvent;
import com.github.atomicblom.shearmadness.api.events.ShearMadnessSpecialInteractionEvent;
import com.github.atomicblom.shearmadness.utility.ChiselLibrary;
import com.github.atomicblom.shearmadness.utility.ItemLibrary;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Hand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collection;

import static net.minecraft.world.biome.Biome.LOGGER;

@Mod.EventBusSubscriber(modid = CommonReference.MOD_ID)
public class EntityEventHandler
{
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onPlayerInteractionWithEntity(PlayerInteractEvent.EntityInteract event)
	{
		//Process for shearing a sheep
		if (event.getWorld().isRemote) return;
		if (!(event.getTarget() instanceof SheepEntity)) return;
		final ItemStack itemStack = event.getItemStack();
		if (itemStack.isEmpty()) {
			checkSpecialSheepInteraction(event);
			return;
		}
		if (!(itemStack.getItem() instanceof ShearsItem)) return;

		final SheepEntity sheep = (SheepEntity) event.getTarget();
		if (!sheep.isShearable(itemStack, event.getWorld(), event.getPos())) return;

		final LazyOptional<IChiseledSheepCapability> possibleCapability = sheep.getCapability(Capability.CHISELED_SHEEP);
		possibleCapability.ifPresent(capability -> {
			if (!capability.isChiseled()) return;

			//Ok, we have a chiseled sheep, cancel vanilla.
			event.setCanceled(true);

			Shearing.shearSheep(itemStack, sheep, capability, event.getHand());
		});
	}

	private static void checkSpecialSheepInteraction(PlayerInteractEvent.EntityInteract event)
	{
		final SheepEntity sheep = (SheepEntity) event.getTarget();
		final LazyOptional<IChiseledSheepCapability> possibleCapability = sheep.getCapability(Capability.CHISELED_SHEEP, null);
		possibleCapability.ifPresent(capability -> {
			if (!capability.isChiseled()) return;
			if (event.getHand() != Hand.MAIN_HAND) return;

			if (!(event.getPlayer() instanceof ServerPlayerEntity)) {
				LOGGER.error("Expected player to be an instance of ServerPlayerEntity");
				return;
			}
			final ServerPlayerEntity entityPlayer = (ServerPlayerEntity)event.getPlayer();

			MinecraftForge.EVENT_BUS.post(new ShearMadnessSpecialInteractionEvent(event.getWorld(), entityPlayer, sheep, capability));
		});
	}

	@SuppressWarnings("BooleanVariableAlwaysNegated")
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onAttack(AttackEntityEvent event)
	{
		//Process for chiseling a sheep
		final Entity sheep = event.getTarget();
		if (sheep == null) return;

		final PlayerEntity entityPlayer = event.getPlayer();
		ItemStack activeStack = entityPlayer.inventory.getCurrentItem();
		Hand hand = Hand.MAIN_HAND;
		boolean attackedWithChisel = false;
		if (isChisel(activeStack.getItem()))
		{
			attackedWithChisel = true;
		} else
		{
			activeStack = entityPlayer.inventory.offHandInventory.get(0);
			if (isChisel(activeStack.getItem()))
			{
				attackedWithChisel = true;
				hand = Hand.OFF_HAND;
			}
		}
		if (!attackedWithChisel)
		{
			return;
		}

		final LazyOptional<IChiseledSheepCapability> possibleCapability = sheep.getCapability(Capability.CHISELED_SHEEP);
		ItemStack finalActiveStack = activeStack;
		Hand finalHand = hand;
		possibleCapability.ifPresent(capability -> {
			event.setCanceled(true);

			Chiseling.chiselSheep(sheep, entityPlayer, finalActiveStack, finalHand);
		});
	}

	public static boolean isChisel(Item item)
	{
		return item == ChiselLibrary.chisel_iron ||
				item == ChiselLibrary.chisel_diamond ||
				item == ChiselLibrary.chisel_hitech ||
				item == ItemLibrary.not_a_chisel;
	}

	@SubscribeEvent
	public static void onLivingDrop(LivingDropsEvent event) {

		final Entity entity = event.getEntity();
		final LazyOptional<IChiseledSheepCapability> possibleCapability = entity.getCapability(Capability.CHISELED_SHEEP);
		possibleCapability.ifPresent(capability -> {
			if (capability.isChiseled())
			{
				final Collection<ItemEntity> drops = event.getDrops();
				final ItemStack chiselItemStack = capability.getChiselItemStack();

				drops.removeIf(entityItem -> ItemTags.WOOL.contains(entityItem.getItem().getItem()));

				drops.add(new ItemEntity(entity.world, entity.getPosX(), entity.getPosY(), entity.getPosZ(), chiselItemStack.copy()));

				MinecraftForge.EVENT_BUS.post(new ShearMadnessSheepKilledEvent(
						drops,
						event.getSource(),
						event.getLootingLevel(),
						chiselItemStack,
						entity
				));
			}
		});
	}

	@SubscribeEvent
	public static void onCommonEntityJoinWorldEvent(EntityJoinWorldEvent event)
	{
		final Entity entity = event.getEntity();
		if (entity instanceof MobEntity) {
			final LazyOptional<IChiseledSheepCapability> possibleCapability = entity.getCapability(Capability.CHISELED_SHEEP);
			possibleCapability.ifPresent(capability -> {
				final MobEntity livingEntity = (MobEntity) entity;

				final GoalSelector brain = livingEntity.goalSelector;
				brain.addGoal(2, new SheepBehaviourAI(livingEntity));
			});
		}
	}

	@SubscribeEvent
	public static void onEntityLivingDeathEvent(LivingDeathEvent event) {
		final Entity entity = event.getEntity();
		if (entity instanceof MobEntity) {
			final LazyOptional<IChiseledSheepCapability> possibleCapability = entity.getCapability(Capability.CHISELED_SHEEP);
			possibleCapability.ifPresent(capability -> {
				final MobEntity livingEntity = (MobEntity) entity;

				livingEntity.goalSelector.getRunningGoals()
						.filter(taskEntry -> taskEntry.getGoal() instanceof SheepBehaviourAI)
						.forEach(taskEntry -> ((SheepBehaviourAI) taskEntry.getGoal()).onDeath());

//				livingEntity.goalSelector.goals
//					.stream()
//					.filter(taskEntry -> taskEntry.action instanceof SheepBehaviourAI)
//					.forEach(taskEntry -> ((SheepBehaviourAI) taskEntry.action).onDeath());
			});
		}
	}
}
