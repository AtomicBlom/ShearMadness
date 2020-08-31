package com.github.atomicblom.shearmadness.variations.vanilla.behaviour;

import com.github.atomicblom.shearmadness.api.Capability;
import com.github.atomicblom.shearmadness.api.behaviour.BehaviourBase;
import com.github.atomicblom.shearmadness.api.capability.IChiseledSheepCapability;
import net.minecraft.block.Blocks;
import net.minecraft.block.NoteBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.world.NoteBlockEvent;

import java.util.function.Supplier;

public class NoteBlockBehaviour extends BehaviourBase<NoteBlockBehaviour> {

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
            triggerNoteBlock(world, currentLocation, capability);
        }

        isTriggered = powered;
    }

    public static void tuneNoteBlockSheep(SheepEntity sheep) {
        final World world = sheep.getEntityWorld();

        final IChiseledSheepCapability capability = sheep.getCapability(Capability.CHISELED_SHEEP, null);
        assert capability != null;
        final CompoundNBT extraData = capability.getExtraData();
        byte note = getTuning(extraData);
        note = (byte)((note + 1) % 25);
        extraData.setByte("NOTEBLOCK_TUNING", note);

        triggerNoteBlock(world, sheep.getPosition(), capability);

    }

    public static byte getTuning(CompoundNBT extraData) {
        return extraData.contains("NOTEBLOCK_TUNING") ? extraData.getByte("NOTEBLOCK_TUNING") : 12;
    }

    public static void triggerNoteBlock(World world, BlockPos currentLocation, IChiseledSheepCapability capability) {
        final BlockState blockState = world.getBlockState(currentLocation);
        final BlockState blockStateBeneath = world.getBlockState(currentLocation.down());
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

        final CompoundNBT extraData = capability.getExtraData();

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

            if (world instanceof ServerWorld) {
                ServerWorld worldserver = (ServerWorld) world;
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
        if (id < 0 || id >= NoteBlock.INSTRUMENTS.size())
        {
            id = 0;
        }

        return NoteBlock.INSTRUMENTS.get(id);
    }

    @Override
    public boolean isEquivalentTo(NoteBlockBehaviour other) {
        return super.isEquivalentTo(other);
    }
}
