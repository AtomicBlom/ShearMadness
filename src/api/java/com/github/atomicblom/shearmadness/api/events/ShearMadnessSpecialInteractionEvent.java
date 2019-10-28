package com.github.atomicblom.shearmadness.api.events;

import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.Event;
import com.github.atomicblom.shearmadness.api.capability.IChiseledSheepCapability;

public class ShearMadnessSpecialInteractionEvent extends Event {

    private final World world;
    private final ServerPlayerEntity player;
    private final SheepEntity sheep;
    private final IChiseledSheepCapability capability;
    private final ItemStack itemStack;

    public ShearMadnessSpecialInteractionEvent(World world, ServerPlayerEntity player, SheepEntity sheep, IChiseledSheepCapability capability) {
        this.world = world;
        this.player = player;
        this.sheep = sheep;
        this.capability = capability;
        this.itemStack = capability.getChiselItemStack();
    }

    public World getWorld() {
        return world;
    }

    public SheepEntity getSheep() {
        return sheep;
    }

    public IChiseledSheepCapability getCapability() {
        return capability;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public ServerPlayerEntity getPlayer() {
        return player;
    }
}