package com.github.atomicblom.shearmadness.variations.vanilla.behaviour;

import com.github.atomicblom.shearmadness.api.behaviour.BehaviourBase;
import com.github.atomicblom.shearmadness.api.capability.IChiseledSheepCapability;
import com.github.atomicblom.shearmadness.capability.CapabilityProvider;
import com.github.atomicblom.shearmadness.utility.Logger;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class FollowAutoCraftItems extends BehaviourBase<FollowAutoCraftItems> {

    long lastExecutionTime;
    private int eatingItemTimer;
    private int poopingItemTimer;

    private EntityItem itemBeingConsumed;
    private long autocraftChangeTime;
    private ItemStack[] itemsToCollect;
    private ItemStack[] itemsConsumed;
    private ItemStack[] originalCraftingGrid;


    public FollowAutoCraftItems(EntitySheep entity) {
        super(entity);
    }

    @Override
    public void updateTask() {
        final EntitySheep entity = getEntity();
        final World worldObj = entity.worldObj;

        if (eatingItemTimer > 0) {
            entity.getNavigator().clearPathEntity();
            eatingItemTimer = Math.max(0, eatingItemTimer - 1);

            if (eatingItemTimer == 4) {
                Logger.info("Consuming Item");
                consumeItem(worldObj);
            }

            return;
        }

        final BlockPos position = entity.getPosition();
        if (poopingItemTimer > 0) {
            entity.getNavigator().clearPathEntity();
            poopingItemTimer = Math.max(0, poopingItemTimer - 1);

            if (poopingItemTimer == 1) {
                //calculate recipe and fire item out bum.
                Logger.info("Crafting Item");
                createItem(entity, worldObj);
                lastExecutionTime = worldObj.getTotalWorldTime();
            }

            return;
        }

        if (lastExecutionTime + 20 < worldObj.getTotalWorldTime()) {
            lastExecutionTime = worldObj.getTotalWorldTime();
            if (findItemToConsume(entity, worldObj, position)) return;
            Logger.info("No items found");
        }
    }

    public boolean findItemToConsume(EntitySheep entity, World worldObj, BlockPos position) {
        final IChiseledSheepCapability capability = entity.getCapability(CapabilityProvider.CHISELED_SHEEP, null);
        final NBTTagCompound extraData = capability.getExtraData();
        if (!extraData.hasKey("AUTO_CRAFT")) {
            return true;
        }
        final NBTTagCompound craftMatrixNBT = extraData.getCompoundTag("AUTO_CRAFT");
        if (craftMatrixNBT.hasKey("lastChanged")) {
            long lastChanged = craftMatrixNBT.getLong("lastChanged");
            if (lastChanged != autocraftChangeTime) {
                reloadAutoCraftProperties(craftMatrixNBT);
            }
            autocraftChangeTime = lastChanged;
        } else {
            return true;
        }

        Logger.info("Checking surroundings for items");

        final List<Entity> recipeItems = worldObj.getEntitiesInAABBexcluding(entity, new AxisAlignedBB(position).expand(16, 16, 16).offset(-8, -8, -8), (e) -> {
            if (e instanceof EntityItem) {
                if (e.isAirBorne) return false;
                if (((EntityItem) e).getAge() < 20 * 2) return false;

                final ItemStack stack = ((EntityItem) e).getEntityItem();

                for (final ItemStack itemStack : itemsToCollect) {
                    if (areItemStacksEqual(stack, itemStack)) {
                        return true;
                    }
                }
            }
            return false;
        });

        recipeItems.sort((e, e2) -> ((Double)e.getDistanceSqToEntity(entity)).compareTo(e2.getDistanceSqToEntity(entity)));

        for (final Entity recipeItem : recipeItems) {
            final double distanceSqToEntity = entity.getDistanceSqToEntity(recipeItem);
            if (distanceSqToEntity < 0.75) {
                //Eat thing.
                Logger.info("Eating %s", recipeItem);

                eatingItemTimer = 40;
                worldObj.setEntityState(entity, (byte)10);
                itemBeingConsumed = (EntityItem)recipeItem;
                entity.getNavigator().clearPathEntity();
                entity.getLookHelper().setLookPositionWithEntity(recipeItem, entity.getHorizontalFaceSpeed(), entity.getVerticalFaceSpeed());
                return true;
            }

            if (entity.getNavigator().tryMoveToEntityLiving(recipeItem, 1)) {

                Logger.info("Moving to %s", recipeItem);
                return true;
            }
        }
        return false;
    }

    public void consumeItem(World worldObj) {
        if (itemBeingConsumed != null && itemBeingConsumed.isEntityAlive()) {
            final ItemStack entityItem = itemBeingConsumed.getEntityItem();
            while (entityItem.stackSize > 0 && consumeItem(entityItem)) {
                entityItem.stackSize--;
            }

            if (entityItem.stackSize == 0) {
                worldObj.removeEntity(itemBeingConsumed);
            }

            Logger.info("Add item to consumed");
            updateItemsConsumed();

            int itemsLeft = 0;
            for (int i = 0; i < 9; ++i) {
                if (itemsToCollect[i] != null) {
                    itemsLeft++;
                }
            }

            if (itemsLeft == 0) {
                poopingItemTimer = 44;
                Logger.info("Digesting Item");
            } else {
                Logger.info("%d items left", itemsLeft);
            }
        }
    }

    public void createItem(EntitySheep entity, World worldObj) {
        final ContainerWorkbench container = new ContainerWorkbench(new InventoryPlayer(null), worldObj, entity.getPosition());
        for (int i = 0; i < 9; ++i) {
            container.craftMatrix.setInventorySlotContents(i, itemsConsumed[i]);
        }

        final CraftingManager instance = CraftingManager.getInstance();
        final ItemStack craftedItem = instance.findMatchingRecipe(container.craftMatrix, worldObj);
        if (craftedItem != null) {
            EntityItem entityItem = new EntityItem(worldObj, entity.posX, entity.posY, entity.posZ, craftedItem);
            worldObj.spawnEntityInWorld(entityItem);
            entityItem.rotationYaw = entity.renderYawOffset + 180;
            entityItem.moveRelative(0, 0.3f, 1);

            final ItemStack[] remainingItems = instance.getRemainingItems(container.craftMatrix, worldObj);
            for (ItemStack remainingItem : remainingItems) {
                if (remainingItem == null) continue;
                entityItem = new EntityItem(worldObj, entity.posX, entity.posY, entity.posZ, remainingItem);
                worldObj.spawnEntityInWorld(entityItem);
                entityItem.rotationYaw = entity.renderYawOffset + 180;
                entityItem.moveRelative(0, 0.3f, 1);
            }
        }

        for (int i = 0; i < 9; ++i) {
            itemsToCollect[i] = originalCraftingGrid[i];
        }
    }

    private void updateItemsConsumed() {
        final IChiseledSheepCapability capability = getEntity().getCapability(CapabilityProvider.CHISELED_SHEEP, null);
        final NBTTagCompound extraData = capability.getExtraData();
        if (!extraData.hasKey("AUTO_CRAFT")) { return; }
        final NBTTagCompound craftMatrixNBT = extraData.getCompoundTag("AUTO_CRAFT");

        if (!craftMatrixNBT.hasKey("CONSUMED")) {
            craftMatrixNBT.setTag("CONSUMED", new NBTTagCompound());
        }
        final NBTTagCompound consumed = craftMatrixNBT.getCompoundTag("CONSUMED");

        for (int i = 0; i < 9; ++i)
        {
            if (itemsConsumed[i] != null) {
                final String key = ((Integer) i).toString();
                consumed.setTag(key, itemsConsumed[i].serializeNBT());
            }
        }
    }

    private void reloadAutoCraftProperties(NBTTagCompound craftMatrixNBT) {
        if (!craftMatrixNBT.hasKey("CONSUMED")) {
            craftMatrixNBT.setTag("CONSUMED", new NBTTagCompound());
        }
        final NBTTagCompound consumed = craftMatrixNBT.getCompoundTag("CONSUMED");

        itemsToCollect = new ItemStack[9];
        ItemStack[] updatedOriginalCraftingGrid = new ItemStack[9];
        int itemCount = 0;
        boolean craftedItemChanged = false;
        for (int i = 0; i < 9; ++i)
        {
            final String key = ((Integer) i).toString();

            if (craftMatrixNBT.hasKey(key)) {
                final ItemStack itemstack = ItemStack.loadItemStackFromNBT(craftMatrixNBT.getCompoundTag(key));
                itemsToCollect[i] = itemstack;
                originalCraftingGrid[i] = itemstack;
                itemCount++;
            }
        }

        Logger.info("Looking for %d items", itemCount);

        final int itemConsumedCount = consumed.getInteger("itemsConsumed");
        itemsConsumed = new ItemStack[9];
        for (int i = 0; i < itemConsumedCount; ++i) {
            final String key = ((Integer) i).toString();

            if (consumed.hasKey(key)) {
                final ItemStack consumedItemStack = ItemStack.loadItemStackFromNBT(consumed.getCompoundTag(key));
                consumeItem(consumedItemStack);
            }
        }
    }

    private boolean consumeItem(ItemStack consumedItemStack) {
        for (int i = 0; i < 9; i++) {
            if (areItemStacksEqual(consumedItemStack, itemsToCollect[i])) {
                itemsConsumed[i] = itemsToCollect[i];
                itemsToCollect[i] = null;
                return true;
            }
        }
        return false;
    }

    private static boolean areItemStacksEqual(@Nullable ItemStack stackA, @Nullable ItemStack stackB)
    {
        return stackA == null && stackB == null ? true : (stackA != null && stackB != null ? isItemStackEqual(stackA, stackB) : false);
    }

    /**
     * compares ItemStack argument to the instance ItemStack; returns true if both ItemStacks are equal
     */
    private static boolean isItemStackEqual(ItemStack thisa, ItemStack other)
    {
        if (thisa.getItem() != other.getItem()) return false;
        if (thisa.getItemDamage() != other.getItemDamage()) return false;
        if (!ItemStack.areItemStackTagsEqual(thisa, other)) return false;
        return true;
    }

}
