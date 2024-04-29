package Server.StaticServer;

import Server.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StaticServer implements ServerStrategy {


    private ServerSocket server;
    private final int numOfEdgeNodes = 3;
    private final int[] array = (new Task()).readTaskFromFile("src/Server/StaticServer/input.txt");
    private int chunkSize;
    private int totalSum = 0;
    private int [][]chunks;
    private final ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    TimeMeasurement timer = new TimeMeasurement();
    ElapsedTimes elapsedTimes = new ElapsedTimes();

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
            // Accept clients
            while (clientNumber < numOfEdgeNodes) {
                Socket client = server.accept();
                ClientHandler clientHandler = new ClientHandler(client, chunks[clientNumber], elapsedTimes);
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

            writeTimeToFile();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    //Write elapsed times to CSV file
    private void writeTimeToFile(){
        elapsedTimes.setTotalElapsedTime(timer.getElapsedTime());
        CSVWriter csvWriter = new CSVWriter(elapsedTimes.getClientElapsedTimes(), elapsedTimes.getTotalElapsedTime());
        csvWriter.writeToFile("elapsedTimes.csv");
    }

    //Chunk initializer
    public void chunkInitializer (int []array){
        chunks = new int[numOfEdgeNodes][chunkSize];
        int lastIndex=0;
        for (int i=0; i<numOfEdgeNodes ; i++){
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
    //Calculate total sum
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