package de.majo.tictactoe.cloud.handler.commands;

import de.majo.tictactoe.cloud.main.Main;
import de.majo.tictactoe.cloud.utils.Data;

public class CMD_startserver extends CommandExecutor {

    @Override
    public void execute() {
        System.out.println("Starting new server.....");
        if(super.getArgs().length == 0) {
            super.getInitializer().getServerHandler().createNewServer();
        }else {
            StringBuilder motd = new StringBuilder();
            for(String tile : super.getArgs()) {
                motd.append(tile).append(" ");
            }
            super.getInitializer().getServerHandler().createNewServer(motd.toString());
        }
    }
}
