package com.github.atomicblom.shearmadness.variations.chancecubes;

import com.github.atomicblom.shearmadness.variations.chancecubes.capability.IChanceCubeParticipationCapability;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ChanceCubeCommand extends CommandBase{
    @Override
    public String getCommandName() {
        return "chancecubes";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "chancecubes";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (sender instanceof ICapabilityProvider) {
            if (((ICapabilityProvider) sender).hasCapability(ChanceCubesReference.CHANCE_CUBE_PARTICIPATION, null)) {
                final IChanceCubeParticipationCapability capability = ((ICapabilityProvider) sender).getCapability(ChanceCubesReference.CHANCE_CUBE_PARTICIPATION, null);
                final boolean isParticipating = !capability.isParticipating();
                sender.addChatMessage(new TextComponentTranslation(isParticipating ? "chancecubes_participating" : "chancecubes_notparticipating"));
                capability.setParticipation(isParticipating);
            }
        }
    }
}
