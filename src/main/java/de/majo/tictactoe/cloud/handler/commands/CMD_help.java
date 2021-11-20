package de.majo.tictactoe.cloud.handler.commands;

import de.majo.tictactoe.cloud.handler.ConsoleHandler;
import de.majo.tictactoe.cloud.utils.Data;

import java.util.Arrays;
import java.util.Locale;

public class CMD_help extends CommandExecutor{

    @Override
    public void execute() {
        System.out.println("Command list:");
        Arrays.stream(ConsoleHandler.Command.values()).forEach(commandEnum -> {
            System.out.println(" > " + commandEnum.name().toLowerCase(Locale.ROOT));
        });

    }
}
