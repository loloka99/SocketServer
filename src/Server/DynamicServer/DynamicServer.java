package Server.DynamicServer;

import Server.ServerStrategy;
import Server.Task;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class DynamicServer implements ServerStrategy {
    private ServerSocket server;
    private final int[] array = (new Task()).readTaskFromFile("src/Server/DynamicServer/input.txt");
    private int totalSum;
    private final ArrayList<ClientHandler> clientHandlers = new ArrayList<>();

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
        try {
            int iteration = 0;
            int miniTaskSize = 3;
            while (clientHandlers.size() < 3) {
                Socket client = server.accept();
                ClientHandler clientHandler = new ClientHandler(client, array);
                clientHandlers.add(clientHandler);
            }
            for (ClientHandler clientHandler : clientHandlers) {
                new Thread(clientHandler).start();
            }

            for (ClientHandler clientHandler : clientHandlers) {
                int[] miniTask = new int[miniTaskSize];
                for (int i = 0; i < miniTaskSize; i++) {
                    if (iteration == array.length) break;
                    miniTask[i] = array[iteration++];
                }
                clientHandler.sendTask(miniTask);

            }
            System.out.println("Tasks sent");

            while (iteration < array.length){

                waitForAnyClientToFinish();
                ClientHandler fastestClient = findFastestClient();

                if (fastestClient != null) {
                    int[] miniTask = new int[miniTaskSize];
                    for (int i = 0; i< miniTaskSize; i++) {
                        if (iteration == array.length) break;
                        miniTask[i] = array[iteration++];
                    }
                    fastestClient.sendTask(miniTask);
                }
            }

            for (ClientHandler clientHandler : clientHandlers) {
                clientHandler.sendDone();
            }

            for (ClientHandler clientHandler : clientHandlers) {
                clientHandler.waitForCompletion();
            }
            totalSum = calculateTotal();
            server.close();
        } catch (IOException e) {
            e.printStackTrace();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private ClientHandler findFastestClient() {
        ClientHandler fastestClient = null;
        long minFinishTime = Long.MAX_VALUE;
        for (ClientHandler clientHandler : clientHandlers) {
            System.out.println("Client finish time: " + clientHandler.getFinishTime());
            if (clientHandler.getFinishTime() < minFinishTime) {
                minFinishTime = clientHandler.getFinishTime();
                fastestClient = clientHandler;
            }
        }
        return fastestClient;
    }

    private void waitForAnyClientToFinish() throws InterruptedException {
        for (ClientHandler clientHandler : clientHandlers) {
            synchronized (clientHandlers) {
                while (!clientHandler.isFinished()) {
                    clientHandler.wait();
                }
            }
        }
    }

    @Override
    public int calculateTotal() {
        return clientHandlers.stream()
                .mapToInt(ClientHandler::getResult)
                .sum();
    }

    @Override
    public int getTotalSum() {
        return totalSum;
    }

    @Override
    public long getElapsedTime() {
        return 0;
    }
}
