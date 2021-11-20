package de.majo.tictactoe.cloud.utils;

import de.majo.tictactoe.cloud.handler.ConsoleHandler;
import de.majo.tictactoe.cloud.handler.ServerHandler;

public class Initializer {

    private ConsoleHandler consoleHandler;
    private ServerHandler serverHandler;

    public Initializer(Data data) {
        this.serverHandler = new ServerHandler();

        this.consoleHandler = new ConsoleHandler(data, this);
        this.consoleHandler.run();
    }

    public ConsoleHandler getConsoleHandler() {
        return consoleHandler;
    }

    public void setConsoleHandler(ConsoleHandler consoleHandler) {
        this.consoleHandler = consoleHandler;
    }

    public ServerHandler getServerHandler() {
        return serverHandler;
    }

    public void setServerHandler(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;
    }
}
