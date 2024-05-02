package Server;

import Server.DynamicServer.DynamicServer;
import Server.StaticServer.StaticServer;



public class ServerManager {
    public static void main(String[] args) {
        int miniTaskSize = 3;
        if (args.length == 0) {
            System.err.println("No arguments provided.");
            return;
        } else if (args.length > 2) {
            System.err.println("Too many arguments.");
            return;
        } else if (args.length == 2) {
            try {
                miniTaskSize = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid mini task size.");
                return;
            }
        }
        if (args[0].equals("static")) {
            StaticServer staticServer = new StaticServer();
            staticServer.run();
        } else if (args[0].equals("dynamic")) {
            DynamicServer dynamicServer = new DynamicServer(miniTaskSize);
            dynamicServer.run();
        } else {
            System.err.println("Invalid server type.");
        }

    }
}
