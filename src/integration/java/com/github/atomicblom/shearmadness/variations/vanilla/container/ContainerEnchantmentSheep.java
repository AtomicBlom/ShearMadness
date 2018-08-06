package com.github.atomicblom.shearmadness.variations.vanilla.container;

import com.github.atomicblom.shearmadness.api.Capability;
import com.github.atomicblom.shearmadness.api.capability.IChiseledSheepCapability;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Random;

@ParametersAreNonnullByDefault
public class ContainerEnchantmentSheep extends ContainerEnchantment
{
    private final EntityLiving entity;
    private final Random rand;
    private final World world;

    public ContainerEnchantmentSheep(InventoryPlayer playerInventory, World worldIn, EntityLiving entity)
    {
        super(playerInventory, worldIn, entity.getPosition());
        this.entity = entity;
        rand = new Random();
        world = worldIn;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        if (!entity.hasCapability(Capability.CHISELED_SHEEP, null)) {
            return false;
        }
        final IChiseledSheepCapability capability = entity.getCapability(Capability.CHISELED_SHEEP, null);
        assert capability != null;
        final Item item = capability.getChiselItemStack().getItem();
        if (!(item instanceof ItemBlock) || ((ItemBlock) item).getBlock() != Blocks.ENCHANTING_TABLE) {
            return false;
        }

        return playerIn.getDistanceSq(entity.getPosition()) <= 64.0D;
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

            if (itemstack.isItemEnchantable())
            {
                if (!world.isRemote)
                {
                    float power = 0;
                    final BlockPos position = entity.getPosition();

                    for (int j = -1; j <= 1; ++j)
                    {
                        for (int k = -1; k <= 1; ++k)
                        {
                            if ((j != 0 || k != 0) && world.isAirBlock(position.add(k, 0, j)) && world.isAirBlock(position.add(k, 1, j)))
                            {
                                power += ForgeHooks.getEnchantPower(world, position.add(k * 2, 0, j * 2));
                                power += ForgeHooks.getEnchantPower(world, position.add(k * 2, 1, j * 2));
                                if (k != 0 && j != 0)
                                {
                                    power += ForgeHooks.getEnchantPower(world, position.add(k * 2, 0, j));
                                    power += ForgeHooks.getEnchantPower(world, position.add(k * 2, 1, j));
                                    power += ForgeHooks.getEnchantPower(world, position.add(k, 0, j * 2));
                                    power += ForgeHooks.getEnchantPower(world, position.add(k, 1, j * 2));
                                }
                            }
                        }
                    }

                    final AxisAlignedBB searchBox = new AxisAlignedBB(position.add(-5, -2, -5), position.add(5, 2, 5));
                    for (final Entity nearbyEntity : entity.getEntityWorld().getEntitiesWithinAABBExcludingEntity(entity, searchBox))
                    {
                        final double distance = entity.getDistanceSq(nearbyEntity);

                        //5^2
                        final IChiseledSheepCapability capability = nearbyEntity.getCapability(Capability.CHISELED_SHEEP, null);
                        if (distance < 25 && capability != null) {
                            final ItemStack chiselItemStack = capability.getChiselItemStack();
                            if (chiselItemStack.getItem() instanceof ItemBlock) {
                                power += ((ItemBlock) chiselItemStack.getItem()).getBlock().getEnchantPowerBonus(world, nearbyEntity.getPosition());
                            }
                        }
                    }

                    rand.setSeed(xpSeed);

                    for (int i1 = 0; i1 < 3; ++i1)
                    {
                        enchantLevels[i1] = EnchantmentHelper.calcItemStackEnchantability(rand, i1, (int)power, itemstack);
                        enchantClue[i1] = -1;
                        worldClue[i1] = -1;

                        if (enchantLevels[i1] < i1 + 1)
                        {
                            enchantLevels[i1] = 0;
                        }
                    }

                    for (int j1 = 0; j1 < 3; ++j1)
                    {
                        if (enchantLevels[j1] > 0)
                        {
                            final List<EnchantmentData> list = getEnchantmentList(itemstack, j1, enchantLevels[j1]);

                            if (!list.isEmpty())
                            {
                                final EnchantmentData enchantmentdata = list.get(rand.nextInt(list.size()));
                                enchantClue[j1] = Enchantment.getEnchantmentID(enchantmentdata.enchantment);
                                worldClue[j1] = enchantmentdata.enchantmentLevel;
                            }
                        }
                    }

                    detectAndSendChanges();
                }
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
