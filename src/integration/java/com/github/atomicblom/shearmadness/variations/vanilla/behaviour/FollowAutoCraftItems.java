package com.github.atomicblom.shearmadness.variations.vanilla.behaviour;

import com.github.atomicblom.shearmadness.ShearMadnessMod;
import com.github.atomicblom.shearmadness.api.Capability;
import com.github.atomicblom.shearmadness.api.behaviour.BehaviourBase;
import com.github.atomicblom.shearmadness.networking.SheepChiselDataUpdatedMessage;
import com.github.atomicblom.shearmadness.utility.ItemStackUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

public class FollowAutoCraftItems extends BehaviourBase {

    private Logger Logger = LogManager.getLogger("shearmadness");

    private long lastExecutionTime;
    private int eatingItemTimer;
    private int poopingItemTimer;

    private ItemEntity targetedItem;
    private long autocraftChangeTime;
    private ItemStack[] itemsToCollect = new ItemStack[9];
    private ItemStack[] itemsConsumed = new ItemStack[9];
    private ItemStack[] originalCraftingGrid = new ItemStack[9];


    public FollowAutoCraftItems(SheepEntity entity, Supplier<Boolean> configuration) {
        super(entity, configuration);

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
                lastExecutionTime = worldObj.getGameTime();
            }

            return;
        }

        if (lastExecutionTime + 20 < worldObj.getGameTime()) {
            lastExecutionTime = worldObj.getGameTime();
            if (findItemToConsume(entity, worldObj, position)) return;
            Logger.trace("No items found");
        }
    }

    private boolean findItemToConsume(SheepEntity entity, World worldObj, BlockPos position) {
        return entity.getCapability(Capability.CHISELED_SHEEP).map(capability -> {
            final CompoundNBT extraData = capability.getExtraData();
            if (!extraData.contains("AUTO_CRAFT")) {
                return true;
            }
            final CompoundNBT craftMatrixNBT = extraData.getCompound("AUTO_CRAFT");
            if (craftMatrixNBT.contains("lastChanged")) {
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
                if (e instanceof ItemEntity) {
                    if (e.isAirBorne) return false;
                    final int age = ((ItemEntity) e).age;
                    //Immersive Engineering sets the age to -some silly number to work around something.
                    //So I'ma ignore - numbers to work around that something he's working around.
                    if (age > 0 && age < 20 * 2) return false;

                    final ItemStack stack = ((ItemEntity) e).getItem();

                    for (final ItemStack itemStack : itemsToCollect) {
                        if (areItemStacksEqual(stack, itemStack)) {
                            return true;
                        }
                    }
                }
                return false;
            });

            recipeItems.sort(Comparator.comparingDouble(e -> e.getDistanceSq(entity)));

            for (final Entity recipeItem : recipeItems) {
                final double distanceSqToEntity = entity.getDistanceSq(recipeItem);
                if (distanceSqToEntity < 0.75) {
                    //Eat thing.
                    Logger.trace("Eating {}", recipeItem);

                    eatingItemTimer = 40;
                    worldObj.setEntityState(entity, (byte)10);
                    targetedItem = (ItemEntity) recipeItem;
                    entity.getNavigator().clearPath();
                    entity.getLookController().setLookPositionWithEntity(recipeItem, entity.getHorizontalFaceSpeed(), entity.getVerticalFaceSpeed());
                    return true;
                }

                if (entity.getNavigator().tryMoveToEntityLiving(recipeItem, 1)) {
                    Logger.trace("Moving to {}", recipeItem);
                    return true;
                }
            }
            return false;
        }).orElse(false);
    }


    @Override
    public void onBehaviourStopped(BlockPos previousPos) {
        final SheepEntity entity = getEntity();
        final World entityWorld = entity.getEntityWorld();

        for (int i = 0; i < 9; ++i) {
            if (!itemsConsumed[i].isEmpty()) {
                final ItemEntity entityItem = new ItemEntity(entityWorld, entity.posX, entity.posY, entity.posZ, itemsConsumed[i]);
                entityItem.setDefaultPickupDelay();
                entityWorld.addEntity(entityItem);
            }
            if (!originalCraftingGrid[i].isEmpty()) {
                final ItemEntity entityItem = new ItemEntity(entityWorld, entity.posX, entity.posY, entity.posZ, originalCraftingGrid[i]);
                entityItem.setDefaultPickupDelay();
                entityWorld.addEntity(entityItem);
            }
        }
        entity.getCapability(Capability.CHISELED_SHEEP).ifPresent(capability -> {
            final CompoundNBT extraData = capability.getExtraData();
            extraData.remove("AUTO_CRAFT");
        });
    }

    private void consumeItem(World worldObj) {
        if (targetedItem != null && targetedItem.isAlive()) {
            final ItemStack entityItem = targetedItem.getItem();
            while (entityItem.getCount() > 0 && consumeItem(entityItem)) {
                entityItem.shrink(1);
            }

            if (entityItem.getCount() == 0) {
                targetedItem.remove();
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
            Logger.trace("{} items left", itemsLeft);
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
        //FIXME: WindowID?
        CraftingInventory crafting = new CraftingInventory(DumbContainer.create(200), 3, 3);
        for (int i = 0; i < 9; ++i) {
            crafting.setInventorySlotContents(i, itemsConsumed[i]);
        }

        worldObj.getRecipeManager().getRecipe(IRecipeType.CRAFTING, crafting, worldObj).ifPresent(recipe -> {
            final ItemStack craftedItem = recipe.getRecipeOutput();
            if (!craftedItem.isEmpty()) {
                ItemEntity entityItem = new ItemEntity(worldObj, entity.posX, entity.posY, entity.posZ, craftedItem);

                entityItem.rotationYaw = entity.renderYawOffset + 180;
                entityItem.moveRelative(0,  new Vec3d(0.05f, 0.4f, 1));
                worldObj.addEntity(entityItem);

                final NonNullList<ItemStack> remainingItems = recipe.getRemainingItems(crafting);
                for (final ItemStack remainingItem : remainingItems) {
                    if (remainingItem.isEmpty()) continue;
                    entityItem = new ItemEntity(worldObj, entity.posX, entity.posY, entity.posZ, remainingItem);
                    entityItem.rotationYaw = entity.renderYawOffset + 180;
                    entityItem.moveRelative(0, new Vec3d(0.05f, 0.4f, 1));
                    worldObj.addEntity(entityItem);
                }
            }

            System.arraycopy(originalCraftingGrid, 0, itemsToCollect, 0, 9);
            itemsConsumed = new ItemStack[9];
            for (int i = 0; i < 9; ++i) itemsConsumed[i] = ItemStack.EMPTY;
            updateItemsConsumed();
        });

    }

    private void updateItemsConsumed() {
        getEntity().getCapability(Capability.CHISELED_SHEEP).ifPresent(capability -> {
            final CompoundNBT extraData = capability.getExtraData();
            if (!extraData.contains("AUTO_CRAFT")) {
                return;
            }
            final CompoundNBT craftMatrixNBT = extraData.getCompound("AUTO_CRAFT");

            if (!craftMatrixNBT.contains("CONSUMED")) {
                craftMatrixNBT.put("CONSUMED", new CompoundNBT());
            }
            final CompoundNBT consumed = craftMatrixNBT.getCompound("CONSUMED");

            for (int i = 0; i < 9; ++i) {
                if (!itemsConsumed[i].isEmpty()) {
                    final String key = ((Integer) i).toString();
                    consumed.put(key, itemsConsumed[i].serializeNBT());
                }
            }
        });
    }

    private void reloadAutoCraftProperties(CompoundNBT craftMatrixNBT) {
        if (!craftMatrixNBT.contains("CONSUMED")) {
            craftMatrixNBT.put("CONSUMED", new CompoundNBT());
        }
        final CompoundNBT consumed = craftMatrixNBT.getCompound("CONSUMED");

        itemsToCollect = new ItemStack[9];
        originalCraftingGrid = new ItemStack[9];
        int itemCount = 0;
        for (int i = 0; i < 9; ++i)
        {
            itemsToCollect[i] = ItemStack.EMPTY;
            originalCraftingGrid[i] = ItemStack.EMPTY;

            final String key = ((Integer) i).toString();

            final boolean nbtHasItemInIndex = craftMatrixNBT.contains(key);

            if (nbtHasItemInIndex) {
                final ItemStack itemstack = ItemStack.read(craftMatrixNBT.getCompound(key));
                itemsToCollect[i] = itemstack;
                originalCraftingGrid[i] = itemstack;
                itemCount++;
            }
        }

        updateItemVariantFromCraftingGrid(originalCraftingGrid);

        Logger.trace("Looking for {} items", itemCount);

        final Entity entity = getEntity();
        final World entityWorld = entity.getEntityWorld();

        itemsConsumed = new ItemStack[9];
        for (int i = 0; i < 9; ++i) itemsConsumed[i] = ItemStack.EMPTY;

        for (final String key : consumed.keySet()) {
            final ItemStack consumedItemStack = ItemStack.read(consumed.getCompound(key));
            if (!consumeItem(consumedItemStack)) {
                final ItemEntity entityItem = new ItemEntity(entityWorld, entity.posX, entity.posY, entity.posZ, consumedItemStack);
                entityItem.setDefaultPickupDelay();
                entityWorld.addEntity(entityItem);
                Logger.trace("Unable to consume {}", consumedItemStack);
            } else {
                Logger.trace("Consumed {}", consumedItemStack);
            }
        }

        checkDigested();
    }

    static class DumbContainer extends Container {
        DumbContainer(@Nullable ContainerType<?> p_i50105_1_, int p_i50105_2_) {
            super(p_i50105_1_, p_i50105_2_);
        }

        static Container create(int windowId) {
            return new DumbContainer(ContainerType.GENERIC_3X3, windowId);
        }

        @Override
        public boolean canInteractWith(PlayerEntity playerIn)
        {
            return true;
        }
    }

    private void updateItemVariantFromCraftingGrid(ItemStack[] originalCraftingGrid) {
        final SheepEntity sheep = getEntity();
        final World world = sheep.world;
        //FIXME: window ID?
        CraftingInventory crafting = new CraftingInventory(DumbContainer.create(200), 3, 3);
        for (int i = 0; i < 9; ++i) {
            crafting.setInventorySlotContents(i, originalCraftingGrid[i]);
        }
        final ItemStack craftedItem = world.getRecipeManager().getRecipe(IRecipeType.CRAFTING, crafting, world).map(IRecipe::getRecipeOutput).orElse(ItemStack.EMPTY);
        final int hash = ItemStackUtils.getHash(craftedItem);

        getEntity().getCapability(Capability.CHISELED_SHEEP).ifPresent(capability -> {
            capability.setItemVariantIdentifier(hash);

            ShearMadnessMod.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> sheep), new SheepChiselDataUpdatedMessage(sheep));
        });
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
        if (thisa.getDamage() != other.getDamage()) return false;
        if (!ItemStack.areItemStackTagsEqual(thisa, other)) return false;
        return true;
    }
}
