package client.util;

import java.util.ArrayList;
import java.util.List;

public class ServerData {

    public List<Server> serverCollection;

    public ServerData() {
        this.serverCollection = new ArrayList<>();
    }

    public List<Server> getServerCollection() {
        return serverCollection;
    }

    public void setServerCollection(List<Server> serverCollection) {
        this.serverCollection = serverCollection;
    }
}
