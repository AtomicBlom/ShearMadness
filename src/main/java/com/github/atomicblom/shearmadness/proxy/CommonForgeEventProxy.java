package com.github.atomicblom.shearmadness.proxy;

import com.github.atomicblom.shearmadness.Chiseling;
import com.github.atomicblom.shearmadness.Shearing;
import com.github.atomicblom.shearmadness.ai.*;
import com.github.atomicblom.shearmadness.capability.CapabilityProvider;
import com.github.atomicblom.shearmadness.capability.IChiseledSheepCapability;
import com.github.atomicblom.shearmadness.interactions.AnvilInteraction;
import com.github.atomicblom.shearmadness.interactions.WorkbenchInteraction;
import com.github.atomicblom.shearmadness.utility.BlockLibrary;
import com.github.atomicblom.shearmadness.utility.ChiselLibrary;
import com.github.atomicblom.shearmadness.utility.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.List;

@SuppressWarnings({"MethodMayBeStatic", "unused"})
public class CommonForgeEventProxy
{
    public void fireRegistryEvent() {}

    @SuppressWarnings({"ConstantConditions", "MethodWithMoreThanThreeNegations"})
    @SubscribeEvent
    public void onPlayerInteractionWithEntity(PlayerInteractEvent.EntityInteract event)
    {
        //Process for shearing a sheep
        if (event.getWorld().isRemote) return;
        if (!(event.getTarget() instanceof EntitySheep)) return;
        final ItemStack itemStack = event.getItemStack();
        if (itemStack == null) {
            checkSpecialSheepInteraction(event);
            return;
        }
        if (!(itemStack.getItem() instanceof ItemShears)) return;

        final EntitySheep sheep = (EntitySheep) event.getTarget();
        if (!sheep.isShearable(itemStack, event.getWorld(), event.getPos())) return;

        final IChiseledSheepCapability capability = sheep.getCapability(CapabilityProvider.CHISELED_SHEEP, null);
        if (capability == null) return;
        if (!capability.isChiseled()) return;

        //Ok, we have a chiseled sheep, cancel vanilla.
        event.setCanceled(true);

        Shearing.shearSheep(itemStack, sheep, capability);
    }

    private void checkSpecialSheepInteraction(PlayerInteractEvent.EntityInteract event)
    {
        final EntitySheep sheep = (EntitySheep) event.getTarget();

        final IChiseledSheepCapability capability = sheep.getCapability(CapabilityProvider.CHISELED_SHEEP, null);
        if (capability == null) return;
        if (!capability.isChiseled()) return;

        final Item item = capability.getChiselItemStack().getItem();
        if (item instanceof ItemBlock && ((ItemBlock) item).block == Blocks.ANVIL) {
            event.getEntityPlayer().displayGui(new AnvilInteraction(event.getWorld(), sheep));
        }
        if (item instanceof ItemBlock && ((ItemBlock) item).block == Blocks.CRAFTING_TABLE) {
            event.getEntityPlayer().displayGui(new WorkbenchInteraction(event.getWorld(), sheep));
        }
    }

    @SuppressWarnings("BooleanVariableAlwaysNegated")
    @SubscribeEvent
    public void onAttack(AttackEntityEvent event)
    {
        //Process for chiseling a sheep
        final Entity sheep = event.getTarget();
        if (sheep == null) return;

        final EntityPlayer entityPlayer = event.getEntityPlayer();
        ItemStack activeStack = entityPlayer.inventory.getCurrentItem();
        boolean attackedWithChisel = false;
        if (activeStack != null && ChiselLibrary.isChisel(activeStack.getItem()))
        {
            attackedWithChisel = true;
        } else
        {
            activeStack = entityPlayer.inventory.offHandInventory[0];
            if (activeStack != null && ChiselLibrary.isChisel(activeStack.getItem()))
            {
                attackedWithChisel = true;
            }
        }
        if (!attackedWithChisel)
        {
            return;
        }

        if (!sheep.hasCapability(CapabilityProvider.CHISELED_SHEEP, null)) {
            return;
        }

        event.setCanceled(true);

        Chiseling.chiselSheep(sheep, entityPlayer, activeStack);
    }

    @SubscribeEvent
    public void onLivingDrop(LivingDropsEvent event) {

        final Entity entity = event.getEntity();
        if (entity.hasCapability(CapabilityProvider.CHISELED_SHEEP, null)) {
            final IChiseledSheepCapability capability = entity.getCapability(CapabilityProvider.CHISELED_SHEEP, null);
            if (capability.isChiseled())
            {
                final List<EntityItem> drops = event.getDrops();
                final Item chiselItem = capability.getChiselItemStack().getItem();
                if (chiselItem instanceof ItemBlock)
                {
                    if (((ItemBlock) chiselItem).block == Blocks.TNT && event.getSource().isExplosion())
                    {
                        drops.clear();
                        return;
                    }
                }


                drops.removeIf(entityItem ->
                {
                    final Item item = entityItem.getEntityItem().getItem();
                    if (item instanceof ItemBlock)
                    {
                        if (((ItemBlock) item).block == Blocks.WOOL)
                        {
                            return true;
                        }
                    }
                    return false;
                });
                drops.add(new EntityItem(entity.worldObj, entity.posX, entity.posY, entity.posZ, capability.getChiselItemStack().copy()));
            }
        }
    }

    @SubscribeEvent
    public void onCapabilityAttaching(AttachCapabilitiesEvent.Entity event)
    {
        if (event.getEntity().getClass().equals(EntitySheep.class))
        {
            event.addCapability(new ResourceLocation(Reference.MOD_ID, "chiseledSheep"), new CapabilityProvider());
        }
    }

    @SubscribeEvent
    public void onCommonEntityJoinWorldEvent(EntityJoinWorldEvent event)
    {
        final Entity entity = event.getEntity();
        if (entity.hasCapability(CapabilityProvider.CHISELED_SHEEP, null))
        {
            final EntityLiving livingEntity = (EntityLiving) event.getEntity();
            final EntityAITasks tasks = livingEntity.tasks;
            tasks.addTask(0, new RedstoneSheepAI(livingEntity));
            tasks.addTask(0, new GlowstoneSheepAI(livingEntity));
            tasks.addTask(1, new FlyingSheepAI(livingEntity));
            tasks.addTask(1, new CactusSheepAI(livingEntity));
            tasks.addTask(1, new FireDamageSheepAI(livingEntity));
            tasks.addTask(1, new TNTSheepAI(livingEntity));
        }
    }

    @SubscribeEvent
    public void onEntityLivingDeathEvent(LivingDeathEvent event) {
        final Entity entity = event.getEntity();
        if (entity.hasCapability(CapabilityProvider.CHISELED_SHEEP, null)) {
            final IChiseledSheepCapability capability = entity.getCapability(CapabilityProvider.CHISELED_SHEEP, null);
            if (capability.isChiseled()) {
                final EntityLiving living = (EntityLiving) entity;

                final World world = entity.worldObj;
                BlockPos invisibleBlock = entity.getPosition();
                if (!living.isChild()) {
                    invisibleBlock = invisibleBlock.up();
                }
                final IBlockState blockState = world.getBlockState(invisibleBlock);

                final Block block = blockState.getBlock();
                if (block == BlockLibrary.invisibleRedstone || block == BlockLibrary.invisibleGlowstone) {
                    world.setBlockToAir(invisibleBlock);
                }
            }
        }
    }
}
