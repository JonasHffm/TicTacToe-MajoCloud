package de.majo.tictactoe.cloud.handler.commands;

public class CMD_clear extends CommandExecutor{

    @Override
    public void execute() {
        for(int i = 0; i < 100; i++) {
            System.out.println(" ");
        }
        System.out.println();
        System.out.println("  __  __              _  ____         _____ _      ____  _    _ _____ ");
        System.out.println(" |  \\/  |   /\\       | |/ __ \\       / ____| |    / __ \\| |  | |  __ \\");
        System.out.println(" | \\  / |  /  \\      | | |  | |     | |    | |   | |  | | |  | | |  | |");
        System.out.println(" | |\\/| | / /\\ \\ _   | | |  | |     | |    | |   | |  | | |  | | |  | |");
        System.out.println(" | |  | |/ ____ \\ |__| | |__| |     | |____| |___| |__| | |__| | |__| |");
        System.out.println(" |_|  |_/_/    \\_\\____/ \\____/       \\_____|______\\____/ \\____/|_____/ ");
        System.out.println();
        System.out.println("TicTacToe Cloud-Server by Malte Fuchs and Jonas Hoffmann");
        System.out.println();
        System.out.println();
    }
}
