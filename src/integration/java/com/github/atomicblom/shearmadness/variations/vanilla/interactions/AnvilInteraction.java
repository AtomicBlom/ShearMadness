package com.github.atomicblom.shearmadness.variations.vanilla.interactions;

import com.github.atomicblom.shearmadness.variations.vanilla.container.ContainerRepairSheep;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Blocks;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class AnvilInteraction implements INamedContainerProvider
{
    private final SheepEntity sheep;

    public AnvilInteraction(SheepEntity sheep) {
        this.sheep = sheep;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent(Blocks.ANVIL.getTranslationKey());
    }

    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new ContainerRepairSheep(i, playerInventory, sheep);
    }
}
