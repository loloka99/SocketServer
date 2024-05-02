package Server;

import Server.DynamicServer.DynamicServer;
import Server.StaticServer.StaticServer;



public class ServerManager {
    public static void main(String[] args) {
        ServerStrategy server = new DynamicServer();
        server.run();
        System.out.println("Total: " + server.getTotalSum() + ". Elapsed time: " + server.getElapsedTime() + "ms");

    }
}
