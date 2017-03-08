package com.github.atomicblom.shearmadness.api.events;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.List;

public class ShearMadnessSheepKilledEvent extends Event {
    private final List<EntityItem> drops;
    private final DamageSource source;
    private final int lootingLevel;
    private final ItemStack chiselItemStack;
    private final Entity entity;

    public ShearMadnessSheepKilledEvent(List<EntityItem> drops, DamageSource source, int lootingLevel, ItemStack chiselItemStack, Entity entity) {

        this.drops = drops;
        this.source = source;
        this.lootingLevel = lootingLevel;
        this.chiselItemStack = chiselItemStack;
        this.entity = entity;
    }

    public List<EntityItem> getDrops() {
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
