package com.github.atomicblom.shearmadness.variations.vanilla.behaviour;

import com.github.atomicblom.shearmadness.ShearMadnessMod;
import com.github.atomicblom.shearmadness.api.Capability;
import com.github.atomicblom.shearmadness.api.behaviour.BehaviourBase;
import com.github.atomicblom.shearmadness.api.capability.IChiseledSheepCapability;
import com.github.atomicblom.shearmadness.networking.SheepChiselDataUpdatedMessage;
import com.github.atomicblom.shearmadness.utility.ItemStackUtils;
import com.github.atomicblom.shearmadness.utility.Logger;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
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


    public FollowAutoCraftItems(SheepEntity entity) {

        super(entity);
        for (int i = 0; i < 9; i++) {
            itemsToCollect[i] = ItemStack.EMPTY;
            itemsConsumed[i] = ItemStack.EMPTY;
            originalCraftingGrid[i] = ItemStack.EMPTY;
        }
    }

    @Override
    public void updateTask() {
        final SheepEntity entity = getEntity();
        final World worldObj = entity.getEntityWorld();

        if (eatingItemTimer > 0) {
            entity.getNavigator().clearPath();
            eatingItemTimer = Math.max(0, eatingItemTimer - 1);

            if (eatingItemTimer == 4) {
                Logger.trace("Consuming Item");
                consumeItem(worldObj);
            }

            return;
        }

        final BlockPos position = entity.getPosition();
        if (poopingItemTimer > 0) {
            entity.getNavigator().clearPath();
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

    private boolean findItemToConsume(SheepEntity entity, World worldObj, BlockPos position) {
        final IChiseledSheepCapability capability = entity.getCapability(Capability.CHISELED_SHEEP, null);
        assert capability != null;
        final CompoundNBT extraData = capability.getExtraData();
        if (!extraData.hasKey("AUTO_CRAFT")) {
            return true;
        }
        final CompoundNBT craftMatrixNBT = extraData.getCompoundTag("AUTO_CRAFT");
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

                final ItemStack stack = ((EntityItem) e).getItem();

                for (final ItemStack itemStack : itemsToCollect) {
                    if (areItemStacksEqual(stack, itemStack)) {
                        return true;
                    }
                }
            }
            return false;
        });

        recipeItems.sort((e, e2) -> ((Double)e.getDistanceSq(entity)).compareTo(e2.getDistanceSq(entity)));

        for (final Entity recipeItem : recipeItems) {
            final double distanceSqToEntity = entity.getDistanceSq(recipeItem);
            if (distanceSqToEntity < 0.75) {
                //Eat thing.
                Logger.trace("Eating %s", recipeItem);

                eatingItemTimer = 40;
                worldObj.setEntityState(entity, (byte)10);
                targetedItem = (EntityItem)recipeItem;
                entity.getNavigator().clearPath();
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
        final SheepEntity entity = getEntity();
        final World entityWorld = entity.getEntityWorld();

        for (int i = 0; i < 9; ++i) {
            if (!itemsConsumed[i].isEmpty()) {
                final EntityItem entityItem = new EntityItem(entityWorld, entity.posX, entity.posY, entity.posZ, itemsConsumed[i]);
                entityItem.setDefaultPickupDelay();
                entityWorld.spawnEntity(entityItem);
            }
            if (!originalCraftingGrid[i].isEmpty()) {
                final EntityItem entityItem = new EntityItem(entityWorld, entity.posX, entity.posY, entity.posZ, originalCraftingGrid[i]);
                entityItem.setDefaultPickupDelay();
                entityWorld.spawnEntity(entityItem);
            }
        }
        final IChiseledSheepCapability capability = entity.getCapability(Capability.CHISELED_SHEEP, null);
        assert capability != null;
        final CompoundNBT extraData = capability.getExtraData();
        extraData.removeTag("AUTO_CRAFT");
    }

    private void consumeItem(World worldObj) {
        if (targetedItem != null && targetedItem.isEntityAlive()) {
            final ItemStack entityItem = targetedItem.getItem();
            while (entityItem.getCount() > 0 && consumeItem(entityItem)) {
                entityItem.shrink(1);
            }

            if (entityItem.getCount() == 0) {
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
            if (!itemsToCollect[i].isEmpty()) {
                itemsLeft++;
            }
        }
        return itemsLeft;
    }

    private void createItem(SheepEntity entity, World worldObj) {
        InventoryCrafting crafting = new InventoryCrafting(new DumbContainer(), 3, 3);
        for (int i = 0; i < 9; ++i) {
            crafting.setInventorySlotContents(i, itemsConsumed[i]);
        }

        final ItemStack craftedItem = CraftingManager.findMatchingResult(crafting, worldObj);
        if (!craftedItem.isEmpty()) {
            EntityItem entityItem = new EntityItem(worldObj, entity.posX, entity.posY, entity.posZ, craftedItem);

            entityItem.rotationYaw = entity.renderYawOffset + 180;
            entityItem.moveRelative(0, 0.05f, 0.4f, 1);
            worldObj.spawnEntity(entityItem);

            final NonNullList<ItemStack> remainingItems = CraftingManager.getRemainingItems(crafting, worldObj);
            for (final ItemStack remainingItem : remainingItems) {
                if (remainingItem.isEmpty()) continue;
                entityItem = new EntityItem(worldObj, entity.posX, entity.posY, entity.posZ, remainingItem);
                entityItem.rotationYaw = entity.renderYawOffset + 180;
                entityItem.moveRelative(0, 0.05f, 0.4f, 1);
                worldObj.spawnEntity(entityItem);
            }
        }

        System.arraycopy(originalCraftingGrid, 0, itemsToCollect, 0, 9);
        itemsConsumed = new ItemStack[9];
        for (int i = 0; i < 9; ++i) itemsConsumed[i] = ItemStack.EMPTY;
        updateItemsConsumed();
    }

    private void updateItemsConsumed() {
        final IChiseledSheepCapability capability = getEntity().getCapability(Capability.CHISELED_SHEEP, null);
        assert capability != null;
        final CompoundNBT extraData = capability.getExtraData();
        if (!extraData.hasKey("AUTO_CRAFT")) { return; }
        final CompoundNBT craftMatrixNBT = extraData.getCompoundTag("AUTO_CRAFT");

        if (!craftMatrixNBT.hasKey("CONSUMED")) {
            craftMatrixNBT.setTag("CONSUMED", new CompoundNBT());
        }
        final CompoundNBT consumed = craftMatrixNBT.getCompoundTag("CONSUMED");

        for (int i = 0; i < 9; ++i)
        {
            if (!itemsConsumed[i].isEmpty()) {
                final String key = ((Integer) i).toString();
                consumed.setTag(key, itemsConsumed[i].serializeNBT());
            }
        }
    }

    private void reloadAutoCraftProperties(CompoundNBT craftMatrixNBT) {
        if (!craftMatrixNBT.hasKey("CONSUMED")) {
            craftMatrixNBT.setTag("CONSUMED", new CompoundNBT());
        }
        final CompoundNBT consumed = craftMatrixNBT.getCompoundTag("CONSUMED");

        itemsToCollect = new ItemStack[9];
        originalCraftingGrid = new ItemStack[9];
        int itemCount = 0;
        for (int i = 0; i < 9; ++i)
        {
            itemsToCollect[i] = ItemStack.EMPTY;
            originalCraftingGrid[i] = ItemStack.EMPTY;

            final String key = ((Integer) i).toString();

            final boolean nbtHasItemInIndex = craftMatrixNBT.hasKey(key);

            if (nbtHasItemInIndex) {
                final ItemStack itemstack = new ItemStack(craftMatrixNBT.getCompoundTag(key));
                itemsToCollect[i] = itemstack;
                originalCraftingGrid[i] = itemstack;
                itemCount++;
            }
        }

        updateItemVariantFromCraftingGrid(originalCraftingGrid);

        Logger.trace("Looking for %d items", itemCount);

        final Entity entity = getEntity();
        final World entityWorld = entity.getEntityWorld();

        itemsConsumed = new ItemStack[9];
        for (int i = 0; i < 9; ++i) itemsConsumed[i] = ItemStack.EMPTY;

        for (final String key : consumed.getKeySet()) {
            final ItemStack consumedItemStack = new ItemStack(consumed.getCompoundTag(key));
            if (!consumeItem(consumedItemStack)) {
                final EntityItem entityItem = new EntityItem(entityWorld, entity.posX, entity.posY, entity.posZ, consumedItemStack);
                entityItem.setDefaultPickupDelay();
                entityWorld.spawnEntity(entityItem);
                Logger.trace("Unable to consume %s", consumedItemStack);
            } else {
                Logger.trace("Consumed %s", consumedItemStack);
            }
        }

        checkDigested();
    }

    static class DumbContainer extends Container {
        @Override
        public boolean canInteractWith(EntityPlayer playerIn)
        {
            return true;
        }
    }

    private void updateItemVariantFromCraftingGrid(ItemStack[] originalCraftingGrid) {
        final SheepEntity sheep = getEntity();
        final World world = sheep.world;
        InventoryCrafting crafting = new InventoryCrafting(new DumbContainer(), 3, 3);
        for (int i = 0; i < 9; ++i) {
            crafting.setInventorySlotContents(i, originalCraftingGrid[i]);
        }
        final ItemStack craftedItem = CraftingManager.findMatchingResult(crafting, world);
        final int hash = ItemStackUtils.getHash(craftedItem);

        final IChiseledSheepCapability capability = getEntity().getCapability(Capability.CHISELED_SHEEP, null);
        assert capability != null;
        capability.setItemVariantIdentifier(hash);

        ShearMadnessMod.CHANNEL.sendToAll(new SheepChiselDataUpdatedMessage(sheep));
    }

    private boolean consumeItem(ItemStack consumedItemStack) {
        for (int i = 0; i < 9; i++) {
            if (areItemStacksEqual(consumedItemStack, itemsToCollect[i])) {
                itemsConsumed[i] = itemsToCollect[i];
                itemsToCollect[i] = ItemStack.EMPTY;
                return true;
            }
        }
        return false;
    }

    private static boolean areItemStacksEqual(@Nonnull ItemStack stackA, @Nonnull ItemStack stackB)
    {
        return stackA.isEmpty() && stackB.isEmpty() || !stackA.isEmpty() && !stackB.isEmpty() && isItemStackEqual(stackA, stackB);
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
