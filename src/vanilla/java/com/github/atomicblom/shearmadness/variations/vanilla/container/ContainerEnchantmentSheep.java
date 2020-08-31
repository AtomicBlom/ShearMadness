package com.github.atomicblom.shearmadness.variations.vanilla.container;

import com.github.atomicblom.shearmadness.api.Capability;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.EnchantmentContainer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Random;

@ParametersAreNonnullByDefault
public class ContainerEnchantmentSheep extends EnchantmentContainer
{
    private final LivingEntity entity;
    private final Random rand;

    public ContainerEnchantmentSheep(int windowId, PlayerInventory playerInventory, LivingEntity sheep) {
        super(windowId, playerInventory, IWorldPosCallable.of(sheep.world, sheep.getPosition()));

        this.entity = sheep;
        rand = new Random();
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return entity.getCapability(Capability.CHISELED_SHEEP).map(capability -> {
            final Item item = capability.getChiselItemStack().getItem();
            if (!(item instanceof BlockItem) || ((BlockItem) item).getBlock() != Blocks.ENCHANTING_TABLE) {
                return false;
            }

            return playerIn.getDistanceSq(entity) <= 64.0D;
        }).orElse(false);
    }

    private float getPower(World world, BlockPos pos) {
        return world.getBlockState(pos).getEnchantPowerBonus(world, pos);
    }

    /**
     * Callback for when the crafting matrix is changed.
     */
    @Override
    public void onCraftMatrixChanged(IInventory inventoryIn)
    {
        if (inventoryIn == tableInventory)
        {
            final ItemStack itemstack = inventoryIn.getStackInSlot(0);

            if (!itemstack.isEmpty() && itemstack.isEnchantable())
            {
                field_217006_g.consume((world, position) ->
                {
                    float power = 0;

                    for (int j = -1; j <= 1; ++j)
                    {
                        for (int k = -1; k <= 1; ++k)
                        {
                            if ((j != 0 || k != 0) && world.isAirBlock(position.add(k, 0, j)) && world.isAirBlock(position.add(k, 1, j)))
                            {
                                power += this.getPower(world, position.add(k * 2, 0, j * 2));
                                power += this.getPower(world, position.add(k * 2, 1, j * 2));
                                if (k != 0 && j != 0)
                                {
                                    power += this.getPower(world, position.add(k * 2, 0, j));
                                    power += this.getPower(world, position.add(k * 2, 1, j));
                                    power += this.getPower(world, position.add(k, 0, j * 2));
                                    power += this.getPower(world, position.add(k, 1, j * 2));
                                }
                            }
                        }
                    }

                    final AxisAlignedBB searchBox = new AxisAlignedBB(position.add(-5, -2, -5), position.add(5, 2, 5));
                    for (final Entity nearbyEntity : entity.getEntityWorld().getEntitiesWithinAABBExcludingEntity(entity, searchBox))
                    {
                        final double distance = entity.getDistanceSq(nearbyEntity);

                        //5^2
                        if (distance < 25) {
                            power += nearbyEntity.getCapability(Capability.CHISELED_SHEEP).map(capability -> {
                                final ItemStack chiselItemStack = capability.getChiselItemStack();
                                if (chiselItemStack.getItem() instanceof BlockItem) {
                                    BlockItem blockItem = (BlockItem)chiselItemStack.getItem();
                                    return blockItem.getBlock().getDefaultState().getEnchantPowerBonus(world, nearbyEntity.getPosition());
                                }
                                return 0f;
                            }).orElse(0f);

                        }
                    }

                    rand.setSeed(xpSeed.get());

                    for (int i1 = 0; i1 < 3; ++i1)
                    {
                        enchantLevels[i1] = EnchantmentHelper.calcItemStackEnchantability(rand, i1, (int)power, itemstack);
                        enchantClue[i1] = -1;
                        worldClue[i1] = -1;
                        if (enchantLevels[i1] < i1 + 1)
                        {
                            enchantLevels[i1] = 0;
                        }

                        enchantLevels[i1] = ForgeEventFactory.onEnchantmentLevelSet(world, position, i1, (int)power, itemstack, enchantLevels[i1]);
                    }

                    for (int j1 = 0; j1 < 3; ++j1)
                    {
                        if (enchantLevels[j1] > 0)
                        {
                            final List<EnchantmentData> list = getEnchantmentList(itemstack, j1, enchantLevels[j1]);
                            if (list != null && !list.isEmpty())
                            {
                                final EnchantmentData enchantmentdata = list.get(rand.nextInt(list.size()));
                                enchantClue[j1] = Registry.ENCHANTMENT.getId(enchantmentdata.enchantment);
                                worldClue[j1] = enchantmentdata.enchantmentLevel;
                            }
                        }
                    }

                    detectAndSendChanges();
                });
            }
            else
            {
                for (int i = 0; i < 3; ++i)
                {
                    enchantLevels[i] = 0;
                    enchantClue[i] = -1;
                    worldClue[i] = -1;
                }
            }
        }
    }
}
