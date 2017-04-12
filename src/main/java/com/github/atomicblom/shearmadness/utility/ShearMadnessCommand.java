package com.github.atomicblom.shearmadness.utility;

import com.google.common.collect.Lists;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ShearMadnessCommand extends CommandBase {
    private final List<CommandBase> childCommands;

    public ShearMadnessCommand(List<CommandBase> childCommands) {
        this.childCommands = Lists.newArrayList(childCommands);
    }

    @Override
    public String getName() {
        return "shearmadness";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "shearmadness [subcommand]";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            throw new CommandException("nosubcommandspecified");
        }
        final Optional<CommandBase> first = childCommands
                .stream()
                .sorted(Comparator.comparing(ICommand::getName))
                .filter(command -> command.getName().equals(args[0]))
                .findFirst();
        if (!first.isPresent()) {
            throw new CommandException("nosuchsubcommand");
        }
        final CommandBase commandBase = first.get();

        final String[] parameters = Arrays.copyOfRange(args, 1, args.length);

        commandBase.execute(server, sender, parameters);
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {

        if (args.length <= 1) {
            return childCommands
                    .stream()
                    .map(ICommand::getName)
                    .sorted()
                    .filter(command -> command.startsWith(args[0]))
                    .collect(Collectors.toList());
        }

        final Optional<CommandBase> first = childCommands
                .stream()
                .sorted(Comparator.comparing(ICommand::getName))
                .filter(command -> command.getName() == args[0])
                .findFirst();

        if (!first.isPresent()) {
            return Lists.newArrayList();
        }

        final String[] parameters = Arrays.copyOfRange(args, 1, args.length - 2);

        return first.get().getTabCompletions(server, sender, parameters, pos);
    }
}
