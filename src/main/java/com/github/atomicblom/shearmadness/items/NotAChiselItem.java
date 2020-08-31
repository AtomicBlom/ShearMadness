package com.github.atomicblom.shearmadness.items;

import com.github.atomicblom.shearmadness.utility.Reference;
import com.github.atomicblom.shearmadness.inventory.container.NotAChiselContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class NotAChiselItem extends Item {
    public NotAChiselItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (playerIn instanceof ServerPlayerEntity) {
            NetworkHooks.openGui((ServerPlayerEntity)playerIn, new NotAChiselContainerProvider(handIn), packetBuffer -> {
                packetBuffer.writeInt(handIn.ordinal());
            });

            return ActionResult.resultSuccess(playerIn.getHeldItem(handIn));
        }

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    private class NotAChiselContainerProvider implements INamedContainerProvider
    {
        private final Hand hand;

        public NotAChiselContainerProvider(Hand hand) {

            this.hand = hand;
        }

        @Override
        public ITextComponent getDisplayName() {
            return new TranslationTextComponent(Reference.Gui.NOT_A_CHISEL_TITLE);
        }

        @Nullable
        @Override
        public Container createMenu(int guiId, PlayerInventory playerInventory, PlayerEntity player) {
            return new NotAChiselContainer(guiId, playerInventory, hand);
        }
    }
}
