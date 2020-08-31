package com.github.atomicblom.shearmadness.utility;

import com.github.atomicblom.shearmadness.inventory.container.NotAChiselContainer;
import com.github.atomicblom.shearmadness.api.CommonReference;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(CommonReference.MOD_ID)
public class ContainerTypeLibrary {
    public static final ContainerType<NotAChiselContainer> not_a_chisel_container_type;

    static {
        not_a_chisel_container_type = null;
    }
}
