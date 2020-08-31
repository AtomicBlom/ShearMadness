package com.github.atomicblom.shearmadness.api.events;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.eventbus.api.Event;

import java.util.Collection;

public class ShearMadnessSheepKilledEvent extends Event {
    private final Collection<ItemEntity> drops;
    private final DamageSource source;
    private final int lootingLevel;
    private final ItemStack chiselItemStack;
    private final Entity entity;

    public ShearMadnessSheepKilledEvent(Collection<ItemEntity> drops, DamageSource source, int lootingLevel, ItemStack chiselItemStack, Entity entity) {

        this.drops = drops;
        this.source = source;
        this.lootingLevel = lootingLevel;
        this.chiselItemStack = chiselItemStack;
        this.entity = entity;
    }

    public Collection<ItemEntity> getDrops() {
        return drops;
    }

    public DamageSource getSource() {
        return source;
    }

    public int getLootingLevel() {
        return lootingLevel;
    }

    public ItemStack getChiselItemStack() {
        return chiselItemStack;
    }

    public Entity getEntity() {
        return entity;
    }

    public void noDrops() {
        drops.clear();
    }
}