package com.github.atomicblom.shearmadness.proxy;

import com.github.atomicblom.shearmadness.ai.CactusSheepAI;
import com.github.atomicblom.shearmadness.ai.FlyingSheepAI;
import com.github.atomicblom.shearmadness.ai.GlowstoneSheepAI;
import com.github.atomicblom.shearmadness.ai.RedstoneSheepAI;
import com.github.atomicblom.shearmadness.utility.BlockLibrary;
import com.github.atomicblom.shearmadness.utility.ChiselLibrary;
import com.github.atomicblom.shearmadness.Chiseling;
import com.github.atomicblom.shearmadness.utility.Reference;
import com.github.atomicblom.shearmadness.Shearing;
import com.github.atomicblom.shearmadness.capability.CapabilityProvider;
import com.github.atomicblom.shearmadness.capability.IChiseledSheepCapability;
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import java.util.List;

@SuppressWarnings("MethodMayBeStatic")
public class CommonRenderProxy implements IRenderProxy
{
    private SoundEvent sheepChiseledSound;

    @Override
    public void registerRenderers()
    {

    }

    @Override
    public void registerSounds()
    {
        sheepChiseledSound = registerSound("sheepchiseled");
    }

    private static SoundEvent registerSound(String soundName) {
        final ResourceLocation soundID = new ResourceLocation(Reference.MOD_ID, soundName);
        return GameRegistry.register(new SoundEvent(soundID).setRegistryName(soundID));
    }

    @SuppressWarnings({"ConstantConditions", "MethodWithMoreThanThreeNegations"})
    @SubscribeEvent
    public void onPlayerInteractionWithEntity(EntityInteract event)
    {
        //Process for shearing a sheep
        if (event.getWorld().isRemote) return;
        if (!(event.getTarget() instanceof EntitySheep)) return;
        final ItemStack itemStack = event.getItemStack();
        if (itemStack == null || !(itemStack.getItem() instanceof ItemShears)) return;

        final EntitySheep sheep = (EntitySheep) event.getTarget();
        if (!sheep.isShearable(itemStack, event.getWorld(), event.getPos())) return;

        final IChiseledSheepCapability capability = sheep.getCapability(CapabilityProvider.CHISELED_SHEEP, null);
        if (capability == null) return;
        if (!capability.isChiseled()) return;

        //Ok, we have a chiseled sheep, cancel vanilla.
        event.setCanceled(true);

        Shearing.ShearSheep(itemStack, sheep, capability);
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
        if (entity instanceof EntitySheep)
        {
            final EntityLiving livingEntity = (EntityLiving) event.getEntity();
            final EntityAITasks tasks = livingEntity.tasks;
            tasks.addTask(0, new RedstoneSheepAI(livingEntity));
            tasks.addTask(0, new GlowstoneSheepAI(livingEntity));
            tasks.addTask(1, new FlyingSheepAI(livingEntity));
            tasks.addTask(1, new CactusSheepAI(livingEntity));
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

    @Override
    public void fireRegistryEvent() {}

    @Override
    public SoundEvent getSheepChiseledSound()
    {
        return sheepChiseledSound;
    }
}
