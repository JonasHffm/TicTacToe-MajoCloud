package de.majo.tictactoe.cloud.handler.commands;

import de.majo.tictactoe.cloud.handler.singleserver.CloudServer;

import java.util.Arrays;
import java.util.Optional;

public class CMD_stopserver extends CommandExecutor{

    @Override
    public void execute() {
        String[] args = super.getArgs();
        if(args.length == 1) {
            try {
                int port = Integer.parseInt(args[0]);
                Optional<CloudServer> cloudServer = super.getInitializer().getServerHandler()
                        .getServerList().stream()
                        .filter(cloudServerObj -> cloudServerObj.getPort() == port)
                        .findFirst();
                if(cloudServer.isPresent()) {
                    if(!cloudServer.get().isProxyMode()) {
                        cloudServer.get().shutdown();
                        super.getInitializer().getServerHandler()
                                .getOpenPortList()
                                .add(String.valueOf(cloudServer.get().getPort()));
                        super.getInitializer().getServerHandler().getServerList().remove(cloudServer.get());
                    }else {
                        System.out.println(" > You cannot shut down the proxy-server!");
                    }
                }else {
                    System.out.println(" > There is no open server with this port -> " + port);
                }
            }catch (NumberFormatException ex) {
                System.out.println(" > Incorrect usage: stopserver <port [int]>");
            }
        }else {
            System.out.println(" > Incorrect usage: stopserver <port [int]>");
        }
    }
}
