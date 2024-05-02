package Server.DynamicServer;

import Server.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DynamicServer implements ServerStrategy {
    private ServerSocket server;
    private static final int[] array = (new Task()).readTaskFromFile("src/Server/DynamicServer/input.txt");
    private int totalSum;
    private static final ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private final List<Thread> threads = new ArrayList<>();
    private final TimeMeasurement timer = new TimeMeasurement();
    private final ElapsedTimes elapsedTimes = new ElapsedTimes();

    private static int iteration = 0;
    private static int miniTaskSize;

    public DynamicServer(int miniTaskSize) {
        try {
            server = new ServerSocket(9999);
            DynamicServer.miniTaskSize = miniTaskSize;
        } catch (IOException e) {
            System.err.println("Could not start server.");
        }
    }

    @Override
    public void run() {
        System.out.println("Server is running...");
        try {
            while (clientHandlers.size() < 3) {
                Socket client = server.accept();
                ClientHandler clientHandler = new ClientHandler(client, elapsedTimes);
                clientHandlers.add(clientHandler);
            }
            timer.start();
            for (ClientHandler clientHandler : clientHandlers) {
                Thread thread = new Thread(clientHandler);
                thread.start();
                threads.add(thread);
            }

            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    System.err.println("Thread interrupted.");
                }
            }

            totalSum = calculateTotal();
            server.close();
            timer.stop();
            writeTimeToFile();
        } catch (IOException e) {
            System.err.println("Error occurred while running server.");
        }

    }
    public static String getMiniTask(){
        if (iteration == array.length) return "DONE";
        synchronized (clientHandlers){
            int[] miniTask = new int[miniTaskSize];
            for (int i = 0; i< miniTaskSize; i++) {
                if (iteration == array.length) {
                    break;
                }
                miniTask[i] = array[iteration++];
            }
            return Arrays.stream(miniTask)
                    .mapToObj(String::valueOf)
                    .collect(Collectors.joining(" "));
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
        return timer.getElapsedTime();
    }

    private void writeTimeToFile(){
        elapsedTimes.setTotalElapsedTime(timer.getElapsedTime());
        CSVWriter csvWriter = new CSVWriter(elapsedTimes.getClientElapsedTimes(), elapsedTimes.getTotalElapsedTime());
        csvWriter.writeToFile("dynamicElapsedTimes.csv");
    }
}
