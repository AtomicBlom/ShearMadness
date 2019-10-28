package com.github.atomicblom.shearmadness.variations.vanilla.behaviour;

import com.github.atomicblom.shearmadness.api.Capability;
import com.github.atomicblom.shearmadness.api.behaviour.BehaviourBase;
import com.github.atomicblom.shearmadness.api.capability.IChiseledSheepCapability;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.properties.NoteBlockInstrument;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.world.NoteBlockEvent;

import java.util.function.Supplier;

public class NoteBlockBehaviour extends BehaviourBase {

    private final World world;
    private final LazyOptional<IChiseledSheepCapability> capability;
    private boolean isTriggered = false;
    private BlockPos currentLocation = null;

    public NoteBlockBehaviour(SheepEntity entity, Supplier<Boolean> configuration) {
        super(entity, configuration);
        world = entity.getEntityWorld();
        capability = entity.getCapability(Capability.CHISELED_SHEEP);
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
            capability.ifPresent(capability -> {
                triggerNoteBlock(world, currentLocation, capability.getExtraData());
            });

        }

        isTriggered = powered;
    }

    public static void tuneNoteBlockSheep(SheepEntity sheep) {
        final World world = sheep.getEntityWorld();

        sheep.getCapability(Capability.CHISELED_SHEEP).ifPresent(capability -> {
            final CompoundNBT extraData = capability.getExtraData();
            byte note = getTuning(extraData);
            note = (byte)((note + 1) % 25);
            extraData.putByte("NOTEBLOCK_TUNING", note);

            triggerNoteBlock(world, sheep.getPosition(), capability.getExtraData());
        });
    }

    public static byte getTuning(CompoundNBT extraData) {
        return extraData.contains("NOTEBLOCK_TUNING") ? extraData.getByte("NOTEBLOCK_TUNING") : 12;
    }

    public static void triggerNoteBlock(World world, BlockPos currentLocation, CompoundNBT extraData) {
        final BlockState blockState = world.getBlockState(currentLocation);
        final BlockState blockStateBeneath = world.getBlockState(currentLocation.down());

        int param = getTuning(extraData);

        NoteBlockInstrument instrument = NoteBlockInstrument.byState(blockStateBeneath);
        NoteBlockEvent.Play e = new NoteBlockEvent.Play(world, currentLocation, blockState, param, instrument);
        if (!MinecraftForge.EVENT_BUS.post(e)) {
            instrument = e.getInstrument();
            SoundEvent sound = instrument.getSound();
            if (blockStateBeneath.getBlock().isIn(BlockTags.WOOL)) {
                sound = SoundEvents.ENTITY_SHEEP_AMBIENT;
            }
            param = e.getVanillaNoteId();
            float f = (float) Math.pow(2.0D, (param - 12) / 12.0D);

            world.playSound(null, currentLocation, sound, SoundCategory.RECORDS, 3.0F, f);

            if (world instanceof ServerWorld) {
                ServerWorld serverWorld = (ServerWorld)world;
                serverWorld.spawnParticle(
                        ParticleTypes.NOTE,
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
}
