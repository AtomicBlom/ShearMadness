package com.github.atomicblom.chiselsheep;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

@Mod(modid = ChiselSheepMod.MODID, version = ChiselSheepMod.VERSION)
public class ChiselSheepMod
{
    public static final String MODID = "chiselsheep";
    public static final String VERSION = "1.0";

    @SidedProxy(clientSide = "com.github.atomicblom.chiselsheep.ClientProxy", serverSide = "com.github.atomicblom.chiselsheep.CommonProxy")
    public static IProxy proxy;

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(this);
        proxy.registerRenderers();
    }

    @SubscribeEvent
    public void onRightClick(RightClickItem event) {
        final ItemStack itemStack = event.getItemStack();
        boolean chiselOk = false;
        if (itemStack != null) {
            final Item item = itemStack.getItem();
            if (item.getUnlocalizedName().startsWith("item.chisel.chisel")) {
                chiselOk = true;
            }
        }

        final RayTraceResult mouseOver = Minecraft.getMinecraft().objectMouseOver;
        if (mouseOver != null && mouseOver.typeOfHit == Type.ENTITY) {
            if (chiselOk && mouseOver.entityHit.getClass().equals(EntitySheep.class))
            {
                event.setCanceled(true);
                final NBTTagCompound tagCompound = itemStack.getTagCompound();
                if (tagCompound != null)
                {
                    final NBTTagCompound chiselTarget = tagCompound.getCompoundTag("chiselTarget");
                    if (chiselTarget.hasKey("id"))
                    {
                        //Add Capability
                    } else
                    {
                        //Remove capability
                    }
                }
            }
        }
    }
}
