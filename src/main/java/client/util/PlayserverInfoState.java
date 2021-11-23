package client.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlayserverInfoState {

    private String turn;
    private ArrayList<String> user;
    private boolean won;
    private ArrayList<String> field;
    private Map<String, String> playerSymbols;

    public PlayserverInfoState() {
        this.playerSymbols = new HashMap<>();
    }

    public String getTurn() {
        return turn;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }

    public ArrayList<String> getUser() {
        return user;
    }

    public void setUser(ArrayList<String> user) {
        this.user = user;
    }

    public boolean isWon() {
        return won;
    }

    public void setWon(boolean won) {
        this.won = won;
    }

    public ArrayList<String> getField() {
        return field;
    }

    public void setField(ArrayList<String> field) {
        this.field = field;
    }

    public Map<String, String> getPlayerSymbols() {
        return playerSymbols;
    }

    public void setPlayerSymbols(Map<String, String> playerSymbols) {
        this.playerSymbols = playerSymbols;
    }
}
