package com.github.atomicblom.shearmadness;

import com.github.atomicblom.shearmadness.api.BehaviourRegistry;
import com.github.atomicblom.shearmadness.api.Capability;
import com.github.atomicblom.shearmadness.api.ai.ShearMadnessGoal;
import com.github.atomicblom.shearmadness.api.capability.IChiseledSheepCapability;
import com.github.atomicblom.shearmadness.networking.SheepChiseledMessage;
import com.github.atomicblom.shearmadness.utility.ItemStackUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nonnull;
import java.util.stream.Collectors;

import static com.github.atomicblom.shearmadness.ShearMadnessMod.CHANNEL;

@SuppressWarnings({"BooleanMethodNameMustStartWithQuestion", "UtilityClass"})
public final class Chiseling
{
    private Chiseling() {}

    public static void chiselSheep(Entity sheep, PlayerEntity entityPlayer, ItemStack activeStack, Hand hand)
    {
        LazyOptional<IChiseledSheepCapability> possibleCapability = sheep.getCapability(Capability.CHISELED_SHEEP);
        possibleCapability.ifPresent(capability -> {
            final ItemStack chiselItemStack = capability.getChiselItemStack();
            if (updateCapability(activeStack, capability, entityPlayer.isCreative()))
            {
                activeStack.damageItem(1, entityPlayer, player -> player.sendBreakAnimation(hand));

                SheepEntity mobEntity = (SheepEntity)sheep;
                mobEntity.goalSelector.getRunningGoals()
                        .map(PrioritizedGoal::getGoal)
                        .filter(ShearMadnessGoal.class::isInstance)
                        .map(ShearMadnessGoal.class::cast)
                        .collect(Collectors.toList())
                        .forEach(mobEntity.goalSelector::removeGoal);


                BehaviourRegistry.INSTANCE.getApplicableGoals(capability.getChiselItemStack(), mobEntity)
                        .forEach(smg -> mobEntity.goalSelector.addGoal(smg.getPriority(), smg));

                if (!sheep.getEntityWorld().isRemote)
                {
                    CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> sheep), new SheepChiseledMessage(sheep));

                    if (!chiselItemStack.isEmpty())
                    {
                        ItemStackUtils.dropItem(sheep, chiselItemStack);
                    }
                }
            }
        });
    }

    private static boolean updateCapability(ItemStack heldChisel, IChiseledSheepCapability capability, boolean isCreative)
    {
        boolean changed = false;

        if (hasChiselBlock(heldChisel))
        {
            changed = changeChiselBlock(heldChisel, capability, isCreative);
        } else if (capability.isChiseled())
        {
            capability.unChisel();
            changed = true;
        }

        return changed;
    }

    private static boolean changeChiselBlock(ItemStack heldChisel, IChiseledSheepCapability capability, boolean isCreative)
    {
        final CompoundNBT tagCompound = heldChisel.getTag();
        assert tagCompound != null;
        final CompoundNBT chiselData = tagCompound.getCompound("chiseldata");
        final CompoundNBT chiselTarget = chiselData.getCompound("target");
        if (!chiselTarget.contains("id")) {
            return false;
        }
        final ItemStack currentChisel = capability.getChiselItemStack();
        final ItemStack chiselItemStack = ItemStack.read(chiselTarget);

        if (!checkItemStacksEqual(currentChisel, chiselItemStack))
        {
            final ItemStack copy = chiselItemStack.copy();
            copy.setCount(1);
            capability.chisel(copy);

            if (!isCreative)
            {
                chiselItemStack.shrink(1);
                if (!chiselItemStack.isEmpty())
                {
                    chiselItemStack.write(chiselTarget);
                    chiselData.put("target", chiselTarget);
                } else
                {
                    chiselData.remove("target");
                }
            }
            return true;
        }
        return false;
    }

    private static boolean hasChiselBlock(ItemStack heldChisel)
    {
        final CompoundNBT tagCompound = heldChisel.getTag();
        if (tagCompound != null)
        {
            final CompoundNBT chiselData = tagCompound.getCompound("chiseldata");
            final CompoundNBT chiselTarget = chiselData.getCompound("target");
            return chiselTarget.contains("id");
        }
        return false;
    }

    @SuppressWarnings({"ObjectEquality", "RedundantIfStatement", "MethodWithMoreThanThreeNegations"})
    private static boolean checkItemStacksEqual(@Nonnull ItemStack currentChisel, @Nonnull ItemStack newChisel)
    {
        if (currentChisel.isEmpty() && newChisel.isEmpty())
        {
            return true;
        }
        if (currentChisel.isEmpty() ^ newChisel.isEmpty())
        {
            return false;
        }
        if (newChisel.getItem() != currentChisel.getItem())
        {
            return false;
        }
        if (!ItemStack.areItemStackTagsEqual(newChisel, currentChisel))
        {
            return false;
        }
        if (!newChisel.isDamageable() && newChisel.getDamage() != currentChisel.getDamage())
        {
            return false;
        }
        return true;
    }
}
