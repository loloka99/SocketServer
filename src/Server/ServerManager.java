package Server;

import Server.StaticServer.StaticServer;



public class ServerManager {
    public static void main(String[] args) {
        ServerStrategy server = new StaticServer();
        server.run();
        System.out.println("Total: " + server.getTotalSum() + ". Elapsed time: " + server.getElapsedTime() + "ms");

    }
}
