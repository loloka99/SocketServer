package Server;

public class ServerManager {
    public static void main(String[] args) {
        ServerStrategy server = new StaticServer();
        server.run();
        System.out.println(server.calculateTotal());
    }
}
