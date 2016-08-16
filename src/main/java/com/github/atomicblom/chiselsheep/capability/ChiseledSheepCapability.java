package com.github.atomicblom.chiselsheep.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

/**
 * Created by steblo on 16/08/2016.
 */
public class ChiseledSheepCapability implements IChiseledSheepCapability {
    private String chiselBlockUnlocalizedName = "";
    private boolean isChiseled = false;

    @Override
    public String getChiselBlockUnlocalizedName() {
        return chiselBlockUnlocalizedName;
    }

    @Override
    public void setChiselBlockUnlocalizedName(String chiselBlockUnlocalizedName) {
        if (chiselBlockUnlocalizedName == null || "".equals(chiselBlockUnlocalizedName)) {
            setChiseled(false);
        } else {
            setChiseled(true);
            this.chiselBlockUnlocalizedName = chiselBlockUnlocalizedName;
        }
    }

    public boolean isChiseled() {
        return isChiseled;
    }

    public void setChiseled(boolean chiseled) {
        if (!chiseled) {
            chiselBlockUnlocalizedName = "";
        }
        isChiseled = chiseled;
    }

    public static class ExtentionStorage implements Capability.IStorage<IChiseledSheepCapability> {
        public static final ExtentionStorage instance = new ExtentionStorage();

        @Override
        public NBTBase writeNBT(Capability<IChiseledSheepCapability> capability, IChiseledSheepCapability instance, EnumFacing side) {
            // return an NBT tag

            NBTTagCompound compound = new NBTTagCompound();
            compound.setBoolean("isChiseled", instance.isChiseled());
            if (instance.isChiseled()) {
                compound.setString("chiselBlock", instance.getChiselBlockUnlocalizedName());
            }
            return compound;
        }

        @Override
        public void readNBT(Capability<IChiseledSheepCapability> capability, IChiseledSheepCapability instance, EnumFacing side, NBTBase nbt) {
            NBTTagCompound compound = (NBTTagCompound)nbt;

            instance.setChiseled(compound.getBoolean("isChiseled"));
            if (instance.isChiseled()) {
                // load from the NBT tag
                instance.setChiselBlockUnlocalizedName(compound.getString("chiselBlock"));
            }
        }
    }

}

