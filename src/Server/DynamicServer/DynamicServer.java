package Server.DynamicServer;

import Server.ServerStrategy;
//import Server.StaticServer.ClientHandler;
import Server.Task;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class DynamicServer implements ServerStrategy {
    private ServerSocket server;
    
    private final int[] array = (new Task()).readTaskFromFile("StaticServer/input.txt");
    private int chunkSize;
    private int totalSum = 0;
    private int [][]chunks;
    private final ArrayList<Server.DynamicServer.ClientHandler> clientHandlers = new ArrayList<>();
    public DynamicServer() {
        try {
            server = new ServerSocket(9999);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("Server is running...");
        int clientNumber=0;
        List<Thread> clientThreads = new ArrayList<>();
        try {
            while (clientNumber < 3) {
                Socket client = server.accept();
                Server.DynamicServer.ClientHandler clientHandler = new Server.DynamicServer.ClientHandler(client, array);
                clientHandlers.add(clientHandler);
                clientNumber++;
            }
            for(Server.DynamicServer.ClientHandler clientHandler: clientHandlers){
                Thread clientThread = new Thread(clientHandler);
                clientThreads.add(clientThread);
                clientThread.start();
            }

        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    @Override
    public int calculateTotal() {
        return 0;
    }

    @Override
    public int getTotalSum() {
        return 0;
    }

    @Override
    public long getElapsedTime() {
        return 0;
    }
}
