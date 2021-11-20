package de.majo.tictactoe.cloud.utils;

public class Data {

    public Initializer initializer;

    public Data() {
        initializer = new Initializer(this);
    }

    public Initializer getInitializer() {
        return initializer;
    }
}
