package com.github.atomicblom.chiselsheep.capability;

/**
 * Created by steblo on 16/08/2016.
 */
public interface IChiseledSheepCapability {
    String getChiselBlockUnlocalizedName();

    void setChiselBlockUnlocalizedName(String chiselBlockUnlocalizedName);

    boolean isChiseled();

    void setChiseled(boolean chiselled);
}
