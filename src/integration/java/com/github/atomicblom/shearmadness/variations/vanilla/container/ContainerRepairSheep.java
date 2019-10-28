package com.github.atomicblom.shearmadness.variations.vanilla.container;

import com.github.atomicblom.shearmadness.api.Capability;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.RepairContainer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
public class ContainerRepairSheep extends RepairContainer
{
    private final LivingEntity entity;

    public ContainerRepairSheep(int windowId, PlayerInventory playerInventory, LivingEntity entity)
    {
        super(windowId, playerInventory);
        this.entity = entity;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return entity.getCapability(Capability.CHISELED_SHEEP).map(capability -> {
            final Item item = capability.getChiselItemStack().getItem();
            if (!(item instanceof BlockItem) || !Objects.equals(((BlockItem) item).getBlock(), Blocks.ANVIL)) {
                return false;
            }

            return playerIn.getDistanceSq(entity) <= 64.0D;
        }).orElse(false);
    }
}
