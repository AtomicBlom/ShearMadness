package com.github.atomicblom.shearmadness.variations.chancecubes;

import com.github.atomicblom.shearmadness.variations.chancecubes.capability.IChanceCubeParticipationCapability;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ChanceCubeCommand extends CommandBase {
    @Override
    public String getName() {
        return "chancecubes";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "command.shearmadness:chancecube.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (sender instanceof ICapabilityProvider) {
            if (((ICapabilityProvider) sender).hasCapability(ChanceCubesReference.CHANCE_CUBE_PARTICIPATION, null)) {
                final IChanceCubeParticipationCapability capability = ((ICapabilityProvider) sender).getCapability(ChanceCubesReference.CHANCE_CUBE_PARTICIPATION, null);
                final boolean isParticipating = !capability.isParticipating();
                sender.sendMessage(new TextComponentTranslation(
                        isParticipating ?
                                "message.shearmadness:chancecube.participating" :
                                "message.shearmadness:chancecube.notparticipating"
                ));
                capability.setParticipation(isParticipating);
            }
        }
    }
}
