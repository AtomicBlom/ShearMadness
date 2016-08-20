package com.github.atomicblom.chiselsheep;

import com.github.atomicblom.chiselsheep.capability.ChiseledSheepCapability;
import com.github.atomicblom.chiselsheep.capability.ChiseledSheepCapabilityProvider;
import com.github.atomicblom.chiselsheep.capability.ChiseledSheepCapabilitySerializer;
import com.github.atomicblom.chiselsheep.capability.IChiseledSheepCapability;
import com.github.atomicblom.chiselsheep.networking.CheckSheepChiseledRequestMessage;
import com.github.atomicblom.chiselsheep.networking.CheckSheepChiseledRequestMessageHandler;
import com.github.atomicblom.chiselsheep.networking.SheepChiseledMessage;
import com.github.atomicblom.chiselsheep.networking.SheepChiseledMessageHandler;
import com.github.atomicblom.chiselsheep.proxy.IProxy;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import java.util.Random;

@SuppressWarnings("MethodMayBeStatic")
@Mod(modid = ChiselSheepMod.MODID, version = ChiselSheepMod.VERSION)
public class ChiselSheepMod
{
    public static final String MODID = "chiselsheep";
    public static final String VERSION = "1.0";
    public static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);


    @SidedProxy(clientSide = "com.github.atomicblom.chiselsheep.proxy.ClientProxy", serverSide = "com.github.atomicblom.chiselsheep.proxy.CommonProxy")
    public static IProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        CHANNEL.registerMessage(CheckSheepChiseledRequestMessageHandler.class, CheckSheepChiseledRequestMessage.class, 0, Side.SERVER);
        CHANNEL.registerMessage(SheepChiseledMessageHandler.class, SheepChiseledMessage.class, 1, Side.CLIENT);
        CapabilityManager.INSTANCE.register(IChiseledSheepCapability.class, ChiseledSheepCapabilitySerializer.instance, ChiseledSheepCapability::new);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.registerRenderers();
    }

    @SubscribeEvent
    public void onAttack(AttackEntityEvent event) {
        final Entity sheep = event.getTarget();
        if (sheep == null) return;

        final EntityPlayer serverPlayer = event.getEntityPlayer();
        ItemStack activeStack = serverPlayer.inventory.getCurrentItem();
        boolean attackedWithChisel = false;
        if (activeStack != null && ChiselLibrary.isChisel(activeStack.getItem())) {
            attackedWithChisel = true;
        } else {
            activeStack = serverPlayer.inventory.offHandInventory[0];
            if (activeStack != null && ChiselLibrary.isChisel(activeStack.getItem())) {
                attackedWithChisel = true;
            }
        }
        if (!attackedWithChisel) { return; }

        event.setCanceled(true);

        if (sheep.hasCapability(ChiseledSheepCapabilityProvider.CHISELED_SHEEP, null))
        {
            final IChiseledSheepCapability capability = sheep.getCapability(ChiseledSheepCapabilityProvider.CHISELED_SHEEP, null);
            final ItemStack chiselItemStack = capability.getChiselItemStack();
            if (updateCapability(activeStack, capability, event.getEntityPlayer().isCreative()))
            {
                activeStack.damageItem(1, event.getEntityPlayer());
                CHANNEL.sendToAll(new SheepChiseledMessage(sheep));

                if (!event.getTarget().worldObj.isRemote && chiselItemStack != null)
                {
                    final EntityItem item = sheep.entityDropItem(chiselItemStack, 1.0F);
                    final Random rand = new Random();
                    item.motionY += rand.nextFloat() * 0.05F;
                    item.motionX += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
                    item.motionZ += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
                }
            }
        }
    }

    private boolean updateCapability(ItemStack activeStack, IChiseledSheepCapability capability, boolean isCreative)
    {
        boolean changed = false;
        final NBTTagCompound tagCompound = activeStack.getTagCompound();
        if (tagCompound != null)
        {
            final NBTTagCompound chiselTarget = tagCompound.getCompoundTag("chiselTarget");

            if (chiselTarget.hasKey("id"))
            {
                final ItemStack currentChisel = capability.getChiselItemStack();
                final ItemStack chiselItemStack = ItemStack.loadItemStackFromNBT(chiselTarget);

                if (!checkItemStacksEqual(currentChisel, chiselItemStack)) {
                    final ItemStack copy = chiselItemStack.copy();
                    copy.stackSize = 1;
                    capability.setChiselItemStack(copy);

                    chiselItemStack.stackSize--;
                    if (chiselItemStack.stackSize > 0) {
                        chiselItemStack.writeToNBT(chiselTarget);
                        tagCompound.setTag("chiselTarget", chiselTarget);
                    } else {
                        tagCompound.removeTag("chiselTarget");
                    }
                    changed = true;
                }
            } else {
                capability.setChiseled(false);
                changed = true;
            }
        } else {
            capability.setChiseled(false);
            changed = true;
        }
        return changed;
    }

    private boolean checkItemStacksEqual(ItemStack currentChisel, ItemStack newChisel)
    {
        if (currentChisel == null && newChisel == null) {
            return true;
        }
        if (currentChisel == null ^ newChisel == null) {
            return false;
        }
        if (newChisel.getItem() != currentChisel.getItem()) {
            return false;
        }
        if (!ItemStack.areItemStackTagsEqual(newChisel, currentChisel)) {
            return false;
        }
        if (newChisel.isItemStackDamageable() && newChisel.getMetadata() != currentChisel.getMetadata()) {
            return false;
        }
        return true;
    }

    @SubscribeEvent
    public void onEntityJoinWorldEvent(EntityJoinWorldEvent event)
    {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT){
            CHANNEL.sendToServer(new CheckSheepChiseledRequestMessage(event.getEntity()));
        }
    }

    @SubscribeEvent
    public void onCapabilityAttaching(AttachCapabilitiesEvent.Entity event) {
        if (event.getEntity().getClass().equals(EntitySheep.class)) {
            event.addCapability(new ResourceLocation(MODID, "chiseledSheep"), new ChiseledSheepCapabilityProvider());
        }
    }

    @SubscribeEvent
    public void onPlayerInteractionWithEntity(EntityInteract event) {
        if (event.getWorld().isRemote) return;
        if (!(event.getTarget() instanceof EntitySheep)) return;
        final ItemStack itemStack = event.getItemStack();
        if (itemStack == null || !(itemStack.getItem() instanceof ItemShears)) return;

        final EntitySheep sheep = (EntitySheep)event.getTarget();
        if (!sheep.isShearable(itemStack, event.getWorld(), event.getPos())) return;

        final IChiseledSheepCapability capability = sheep.getCapability(ChiseledSheepCapabilityProvider.CHISELED_SHEEP, null);
        if (capability == null) return;
        if (!capability.isChiseled()) return;

        final int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, itemStack);

        //Ok, we have a chiseled sheep, cancel vanilla.
        event.setCanceled(true);

        sheep.setSheared(true);
        final ItemStack itemToDrop = capability.getChiselItemStack().copy();
        itemToDrop.stackSize = 1;

        final EntityItem item = sheep.entityDropItem(itemToDrop, 1.0F);
        final Random rand = new Random();
        item.motionY += rand.nextFloat() * 0.05F;
        item.motionX += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
        item.motionZ += (rand.nextFloat() - rand.nextFloat()) * 0.1F;

        sheep.playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1.0F, 1.0F);
        itemStack.damageItem(1, sheep);
    }
}
