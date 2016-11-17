package com.github.atomicblom.shearmadness.variations.vanilla.behaviour;

import com.github.atomicblom.shearmadness.api.Capability;
import com.github.atomicblom.shearmadness.api.behaviour.BehaviourBase;
import com.github.atomicblom.shearmadness.api.capability.IChiseledSheepCapability;
import com.github.atomicblom.shearmadness.utility.Logger;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class FollowAutoCraftItems extends BehaviourBase<FollowAutoCraftItems> {

    private long lastExecutionTime;
    private int eatingItemTimer;
    private int poopingItemTimer;

    private EntityItem targetedItem;
    private long autocraftChangeTime;
    private ItemStack[] itemsToCollect = new ItemStack[9];
    private ItemStack[] itemsConsumed = new ItemStack[9];
    private ItemStack[] originalCraftingGrid = new ItemStack[9];


    public FollowAutoCraftItems(EntitySheep entity) {
        super(entity);
    }

    @Override
    public void updateTask() {
        final EntitySheep entity = getEntity();
        final World worldObj = entity.getEntityWorld();

        if (eatingItemTimer > 0) {
            entity.getNavigator().clearPathEntity();
            eatingItemTimer = Math.max(0, eatingItemTimer - 1);

            if (eatingItemTimer == 4) {
                Logger.trace("Consuming Item");
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
                Logger.trace("Crafting Item");
                createItem(entity, worldObj);
                lastExecutionTime = worldObj.getTotalWorldTime();
            }

            return;
        }

        if (lastExecutionTime + 20 < worldObj.getTotalWorldTime()) {
            lastExecutionTime = worldObj.getTotalWorldTime();
            if (findItemToConsume(entity, worldObj, position)) return;
            Logger.trace("No items found");
        }
    }

    private boolean findItemToConsume(EntitySheep entity, World worldObj, BlockPos position) {
        final IChiseledSheepCapability capability = entity.getCapability(Capability.CHISELED_SHEEP, null);
        final NBTTagCompound extraData = capability.getExtraData();
        if (!extraData.hasKey("AUTO_CRAFT")) {
            return true;
        }
        final NBTTagCompound craftMatrixNBT = extraData.getCompoundTag("AUTO_CRAFT");
        if (craftMatrixNBT.hasKey("lastChanged")) {
            final long lastChanged = craftMatrixNBT.getLong("lastChanged");
            if (lastChanged != autocraftChangeTime) {
                updateItemsConsumed();
                reloadAutoCraftProperties(craftMatrixNBT);
            }
            autocraftChangeTime = lastChanged;
        } else {
            return true;
        }

        Logger.trace("Checking surroundings for items");

        final AxisAlignedBB offset = new AxisAlignedBB(position).expand(16, 16, 16).offset(-8, -8, -8);
        final List<Entity> recipeItems = worldObj.getEntitiesInAABBexcluding(entity, offset, (e) -> {
            if (e instanceof EntityItem) {
                if (e.isAirBorne) return false;
                final int age = ((EntityItem) e).age;
                //Immersive Engineering sets the age to -some silly number to work around something.
                //So I'ma ignore - numbers to work around that something he's working around.
                if (age > 0 && age < 20 * 2) return false;

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
                Logger.trace("Eating %s", recipeItem);

                eatingItemTimer = 40;
                worldObj.setEntityState(entity, (byte)10);
                targetedItem = (EntityItem)recipeItem;
                entity.getNavigator().clearPathEntity();
                entity.getLookHelper().setLookPositionWithEntity(recipeItem, entity.getHorizontalFaceSpeed(), entity.getVerticalFaceSpeed());
                return true;
            }

            if (entity.getNavigator().tryMoveToEntityLiving(recipeItem, 1)) {

                Logger.trace("Moving to %s", recipeItem);
                return true;
            }
        }
        return false;
    }


    @Override
    public void onBehaviourStopped(BlockPos previousPos) {
        final EntitySheep entity = getEntity();
        final World entityWorld = entity.getEntityWorld();

        for (int i = 0; i < 9; ++i) {
            if (itemsConsumed[i] != null) {
                final EntityItem entityItem = new EntityItem(entityWorld, entity.posX, entity.posY, entity.posZ, itemsConsumed[i]);
                entityItem.setDefaultPickupDelay();
                entityWorld.spawnEntityInWorld(entityItem);
            }
            if (originalCraftingGrid[i] != null) {
                final EntityItem entityItem = new EntityItem(entityWorld, entity.posX, entity.posY, entity.posZ, originalCraftingGrid[i]);
                entityItem.setDefaultPickupDelay();
                entityWorld.spawnEntityInWorld(entityItem);
            }
        }
        final NBTTagCompound extraData = entity.getCapability(Capability.CHISELED_SHEEP, null).getExtraData();
        extraData.removeTag("AUTO_CRAFT");
    }

    private void consumeItem(World worldObj) {
        if (targetedItem != null && targetedItem.isEntityAlive()) {
            final ItemStack entityItem = targetedItem.getEntityItem();
            while (entityItem.func_190916_E() > 0 && consumeItem(entityItem)) {
                entityItem.func_190918_g(1);
            }

            if (entityItem.func_190916_E() == 0) {
                worldObj.removeEntity(targetedItem);
            }

            Logger.trace("Add item to consumed");
            updateItemsConsumed();

            checkDigested();
        }
    }

    private void checkDigested() {
        int itemsLeft = checkItemsLeftToConsume();

        if (itemsLeft == 0) {
            poopingItemTimer = 44;
            Logger.trace("Digesting Item");
        } else {
            Logger.trace("%d items left", itemsLeft);
        }
    }

    private int checkItemsLeftToConsume() {
        int itemsLeft = 0;
        for (int i = 0; i < 9; ++i) {
            if (itemsToCollect[i] != null) {
                itemsLeft++;
            }
        }
        return itemsLeft;
    }

    private void createItem(EntitySheep entity, World worldObj) {
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

            final NonNullList<ItemStack> remainingItems = instance.getRemainingItems(container.craftMatrix, worldObj);
            for (final ItemStack remainingItem : remainingItems) {
                if (remainingItem == null) continue;
                entityItem = new EntityItem(worldObj, entity.posX, entity.posY, entity.posZ, remainingItem);
                worldObj.spawnEntityInWorld(entityItem);
                entityItem.rotationYaw = entity.renderYawOffset + 180;
                entityItem.moveRelative(0, 0.3f, 1);
            }
        }

        System.arraycopy(originalCraftingGrid, 0, itemsToCollect, 0, 9);
        itemsConsumed = new ItemStack[9];
        updateItemsConsumed();
    }

    private void updateItemsConsumed() {
        final IChiseledSheepCapability capability = getEntity().getCapability(Capability.CHISELED_SHEEP, null);
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
        originalCraftingGrid = new ItemStack[9];
        int itemCount = 0;
        for (int i = 0; i < 9; ++i)
        {
            final String key = ((Integer) i).toString();

            final boolean nbtHasItemInIndex = craftMatrixNBT.hasKey(key);

            if (nbtHasItemInIndex) {
                final ItemStack itemstack = new ItemStack(craftMatrixNBT.getCompoundTag(key));
                itemsToCollect[i] = itemstack;
                originalCraftingGrid[i] = itemstack;
                itemCount++;
            }
        }

        Logger.trace("Looking for %d items", itemCount);

        final Entity entity = getEntity();
        final World entityWorld = entity.getEntityWorld();

        itemsConsumed = new ItemStack[9];
        for (final String key : consumed.getKeySet()) {
            final ItemStack consumedItemStack = new ItemStack(consumed.getCompoundTag(key));
            if (!consumeItem(consumedItemStack)) {
                final EntityItem entityItem = new EntityItem(entityWorld, entity.posX, entity.posY, entity.posZ, consumedItemStack);
                entityItem.setDefaultPickupDelay();
                entityWorld.spawnEntityInWorld(entityItem);
                Logger.trace("Unable to consume %s", consumedItemStack);
            } else {
                Logger.trace("Consumed %s", consumedItemStack);
            }
        }

        checkDigested();
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
