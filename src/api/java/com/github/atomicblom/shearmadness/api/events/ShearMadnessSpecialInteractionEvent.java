package com.github.atomicblom.shearmadness.api.events;

import com.github.atomicblom.shearmadness.api.capability.IChiseledSheepCapability;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;

public class ShearMadnessSpecialInteractionEvent extends Event {

    private final World world;
    private final EntityPlayer player;
    private final EntitySheep sheep;
    private final IChiseledSheepCapability capability;
    private final ItemStack itemStack;

    public ShearMadnessSpecialInteractionEvent(World world, EntityPlayer player, EntitySheep sheep, IChiseledSheepCapability capability) {
        this.world = world;
        this.player = player;
        this.sheep = sheep;
        this.capability = capability;
        this.itemStack = capability.getChiselItemStack();
    }

    public World getWorld() {
        return world;
    }

    public EntitySheep getSheep() {
        return sheep;
    }

    public IChiseledSheepCapability getCapability() {
        return capability;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public EntityPlayer getPlayer() {
        return player;
    }
}
