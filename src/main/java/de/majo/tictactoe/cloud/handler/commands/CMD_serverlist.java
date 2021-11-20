package de.majo.tictactoe.cloud.handler.commands;

public class CMD_serverlist extends CommandExecutor{

    @Override
    public void execute() {
        System.out.println("All online servers: ");
        getInitializer().getServerHandler().getServerList().forEach(cloudServer -> {
            if(!cloudServer.isProxyMode()) {
                System.out.println(" > UID: " + cloudServer.getUID() + " - [" + cloudServer.getPort() + "] - User: " + cloudServer.getClient_list().size() + " / " + cloudServer.getMAX_CLIENT_ACCEPTION());
                System.out.println("  >> " + cloudServer.getMotd());
            }else {
                System.out.println("Proxy-Server:");
                System.out.println(" > UID: " + cloudServer.getUID() + " - [" + cloudServer.getPort() + "] - User: " + cloudServer.getClient_list().size() + " / " + 1000);
                System.out.println("  >> " + cloudServer.getMotd());
                System.out.println();
            }
        });
    }
}
