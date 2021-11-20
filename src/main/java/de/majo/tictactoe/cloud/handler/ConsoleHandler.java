package de.majo.tictactoe.cloud.handler;

import de.majo.tictactoe.cloud.handler.commands.*;
import de.majo.tictactoe.cloud.utils.Data;
import de.majo.tictactoe.cloud.utils.Initializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Time;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

public class ConsoleHandler extends Thread{

    private BufferedReader reader;
    private Data data_instance;
    private Initializer initializer_instance;

    public ConsoleHandler(Data data, Initializer initializer) {
        this.data_instance = data;
        this.initializer_instance = initializer;

       reader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run() {
        listen();
    }


    public void listen() {

        System.out.println();
        System.out.println("  __  __              _  ____         _____ _      ____  _    _ _____ ");
        System.out.println(" |  \\/  |   /\\       | |/ __ \\       / ____| |    / __ \\| |  | |  __ \\");
        System.out.println(" | \\  / |  /  \\      | | |  | |     | |    | |   | |  | | |  | | |  | |");
        System.out.println(" | |\\/| | / /\\ \\ _   | | |  | |     | |    | |   | |  | | |  | | |  | |");
        System.out.println(" | |  | |/ ____ \\ |__| | |__| |     | |____| |___| |__| | |__| | |__| |");
        System.out.println(" |_|  |_/_/    \\_\\____/ \\____/       \\_____|______\\____/ \\____/|_____/ ");
        System.out.println();
        System.out.println();

        System.out.println("Welcome to the MaJo TicTacToe-Cloud");
        System.out.println(">>");
        System.out.println();
        System.out.println();
        String command = "";
        while (true) {
            try {
                if (!(command = reader.readLine()).equals("exit")) {

                    Optional<Command> commandEnum = Command.getCommandByStr(command.split(" ")[0]);
                    if(commandEnum.isPresent()) {
                        String[] args = new String[command.split(" ").length-1];
                        for(int i = 1; i < command.split(" ").length; i++) {
                            args[i-1] = command.split(" ")[i];
                        }

                        System.out.println();
                        CommandExecutor commandExecutor = (CommandExecutor) commandEnum.get().command_listener.newInstance();
                        commandExecutor.setData(data_instance);
                        commandExecutor.setInitializer(initializer_instance);
                        commandExecutor.setArgs(args);
                        commandExecutor.execute();
                        System.out.println();
                    }else {
                        System.out.println();
                        System.out.println("This command is unknown!");
                        System.out.println();
                    }

                    sendEmptyCommandLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }

    }

    public static void sendEmptyCommandLine() {
        System.out.print(">> ");
    }

    public static void log(String message) {
        System.out.println(message);
        System.out.println();
        sendEmptyCommandLine();
    }

    public enum Command {
        STARTSERVER("startserver", CMD_startserver.class),
        HELP("help", CMD_help .class),
        SERVERLIST("serverlist", CMD_serverlist.class),
        STOPSERVER("stopserver", CMD_stopserver.class),
        CLEAR("clear", CMD_clear.class);

        private String command_str;
        private Class command_listener;

        Command(String command_str, Class command_listener) {
            this.command_str = command_str;
            this.command_listener = command_listener;
        }

        public static Optional<Command> getCommandByStr(String command) {
            return Arrays.stream(Command.values())
                    .filter(commandEnum -> commandEnum.command_str.toUpperCase(Locale.ROOT).equals(command.toUpperCase(Locale.ROOT)))
                    .findFirst();
        }
    }
}
