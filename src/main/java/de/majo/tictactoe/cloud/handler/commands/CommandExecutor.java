package de.majo.tictactoe.cloud.handler.commands;

import de.majo.tictactoe.cloud.utils.Data;
import de.majo.tictactoe.cloud.utils.Initializer;

public class CommandExecutor {

    private Data data;
    private Initializer initializer;
    private String[] args;

    public CommandExecutor() {
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public Initializer getInitializer() {
        return initializer;
    }

    public void setInitializer(Initializer initializer) {
        this.initializer = initializer;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    public void execute() {
    }

}
