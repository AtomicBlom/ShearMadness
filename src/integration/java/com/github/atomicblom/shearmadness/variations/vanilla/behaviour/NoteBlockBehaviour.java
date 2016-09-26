package com.github.atomicblom.shearmadness.variations.vanilla.behaviour;

import com.github.atomicblom.shearmadness.api.behaviour.BehaviourBase;
import com.github.atomicblom.shearmadness.api.capability.IChiseledSheepCapability;
import com.github.atomicblom.shearmadness.capability.CapabilityProvider;
import net.minecraft.block.BlockNote;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.NoteBlockEvent;

import java.util.function.Supplier;

/**
 * Created by codew on 19/09/2016.
 */
public class NoteBlockBehaviour extends BehaviourBase<NoteBlockBehaviour> {

    private final World world;
    private final IChiseledSheepCapability capability;
    private boolean isTriggered = false;
    private BlockPos currentLocation = null;

    public NoteBlockBehaviour(EntitySheep entity, Supplier<Boolean> configuration) {
        super(entity, configuration);
        world = entity.getEntityWorld();
        capability = entity.getCapability(CapabilityProvider.CHISELED_SHEEP, null);
    }


    @Override
    public void onBehaviourStarted(BlockPos currentPos) {
        currentLocation = currentPos;
    }

    @Override
    public void onSheepMovedBlock(BlockPos previousLocation, BlockPos newLocation) {
        isTriggered = false;
        currentLocation = newLocation;

    }

    @Override
    public void updateTask() {
        final boolean powered = world.isBlockPowered(currentLocation);
        if (powered && !isTriggered) {
            triggerNoteBlock(world, currentLocation, capability);
        }

        isTriggered = powered;
    }

    public static void tuneNoteBlockSheep(EntitySheep sheep) {
        final IChiseledSheepCapability capability = sheep.getCapability(CapabilityProvider.CHISELED_SHEEP, null);
        final World world = sheep.getEntityWorld();

        final NBTTagCompound extraData = capability.getExtraData();
        byte note = getTuning(extraData);
        note = (byte)((note + 1) % 25);
        extraData.setByte("NOTEBLOCK_TUNING", note);

        triggerNoteBlock(world, sheep.getPosition(), capability);

    }

    public static byte getTuning(NBTTagCompound extraData) {
        return extraData.hasKey("NOTEBLOCK_TUNING") ? extraData.getByte("NOTEBLOCK_TUNING") : 12;
    }

    public static void triggerNoteBlock(World world, BlockPos currentLocation, IChiseledSheepCapability capability) {
        final IBlockState blockState = world.getBlockState(currentLocation);
        final IBlockState blockStateBeneath = world.getBlockState(currentLocation.down());
        Material material = blockStateBeneath.getMaterial();
        int id = 0;

        if (material == Material.ROCK) {
            id = 1;
        }
        if (material == Material.SAND) {
            id = 2;
        }
        if (material == Material.GLASS) {
            id = 3;
        }
        if (material == Material.WOOD) {
            id = 4;
        }

        final NBTTagCompound extraData = capability.getExtraData();

        int param = getTuning(extraData);

        NoteBlockEvent.Play e = new NoteBlockEvent.Play(world, currentLocation, blockState, param, id);
        if (!MinecraftForge.EVENT_BUS.post(e)) {
            id = e.getInstrument().ordinal();
            if (blockStateBeneath.getBlock() == Blocks.WOOL) {
                id = -1;
            }
            param = e.getVanillaNoteId();
            float f = (float) Math.pow(2.0D, (param - 12) / 12.0D);
            world.playSound(null, currentLocation, getInstrument(id), SoundCategory.RECORDS, 3.0F, f);

            if (world instanceof WorldServer) {
                WorldServer worldserver = (WorldServer) world;
                worldserver.spawnParticle(
                        EnumParticleTypes.NOTE,
                        true,
                        currentLocation.getX() + 0.5D,
                        currentLocation.getY() + 1.2D,
                        currentLocation.getZ() + 0.5D,
                        0, //number of particles
                        param / 24.0D,
                        0.0D,
                        0.0D,
                        1
                );
            }
        }
    }

    private static SoundEvent getInstrument(int id) {
        if (id == -1) {
            return SoundEvents.ENTITY_SHEEP_AMBIENT;
        }
        if (id < 0 || id >= BlockNote.INSTRUMENTS.size())
        {
            id = 0;
        }

        return BlockNote.INSTRUMENTS.get(id);
    }

    @Override
    public boolean isEquivalentTo(NoteBlockBehaviour other) {
        return super.isEquivalentTo(other);
    }
}
