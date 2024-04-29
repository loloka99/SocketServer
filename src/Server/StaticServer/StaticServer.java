package Server.StaticServer;

import Server.ServerStrategy;
import Server.Task;
import Server.TimeMeasurement;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StaticServer implements ServerStrategy {


    private ServerSocket server;
    private final int numOfEdgeNodes = 3;
    private int[] array = (new Task()).readTaskFromFile();
    private int chunkSize;
    private int totalSum = 0;
    private int [][]chunks;
    int respondedNodes = 0;
    private final ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    TimeMeasurement timer = new TimeMeasurement();

    public StaticServer() {
        try {
            server = new ServerSocket(9999);
            chunkSize = array.length / numOfEdgeNodes;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        chunkInitializer(array);
        int clientNumber=0;
        List<Thread> clientThreads = new ArrayList<>();
        System.out.println("Server is running...");
        try {
            while (clientNumber < numOfEdgeNodes) {
                Socket client = server.accept();
                ClientHandler clientHandler = new ClientHandler(client, chunks[clientNumber]);
                clientHandlers.add(clientHandler);
                clientNumber++;
            }
            // Start client handlers after all clients have connected
            timer.start();
            for(ClientHandler clientHandler: clientHandlers){
                Thread clientThread = new Thread(clientHandler);
                clientThreads.add(clientThread);
                clientThread.start();
            }
            //waiting for all client response
            for (Thread clientThread : clientThreads) {
                clientThread.join();
            }
            totalSum = calculateTotal();
            timer.stop();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    //dividing task
    public void chunkInitializer (int []array){
        chunks = new int[numOfEdgeNodes][chunkSize];
        int lastIndex=0;
        for (int i=0; i<numOfEdgeNodes ; i++){
            int endIndex = (chunkSize*i) + chunkSize;
            for (int j=0; j<chunkSize; j++){
                chunks[i][j] = array[lastIndex];
                lastIndex++;
            }
        }
        chunks[numOfEdgeNodes-1] =Arrays.copyOf(chunks[numOfEdgeNodes-1],chunkSize+(array.length-lastIndex));
        while (lastIndex < array.length){
            chunks[numOfEdgeNodes-1][chunkSize++] = array[lastIndex++];
        }
    }

    public int calculateTotal() {
        for (ClientHandler clientHandler : clientHandlers) {
            totalSum += clientHandler.getResult();
        }
        return totalSum;
    }

    public int getTotalSum() {
        return totalSum;
    }

    public long getElapsedTime() {
        return timer.getElapsedTime();
    }
}