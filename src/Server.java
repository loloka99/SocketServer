import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Server {

    private ServerSocket server;
    String fileName = "src/input.txt";
    int[] array = readTaskFromFile(fileName);
    private final int numOfEdgeNodes = 3;
    private int chunkSize;
    private int totalSum = 0;
    private int [][]chunks;
    int respondedNodes = 0;
    private final ArrayList<ClientHandler> clientHandlers = new ArrayList<>();

    public Server() {
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
                Thread clientThread = new Thread(clientHandler);
                clientThreads.add(clientThread);
                clientThread.start();
                clientNumber++;
            }
            //waiting for all client response
            for (Thread clientThread : clientThreads) {
                clientThread.join();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

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

    public int calculateSumOfAllResponse() {
        for (ClientHandler clientHandler : clientHandlers) {
            totalSum += clientHandler.getResult();
        }
        return totalSum;
    }

    public int[] readTaskFromFile(String fileName){
        try {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);
            //reading text
            String line = scanner.nextLine();
            String[] numbersAsString = line.split("\\s+");
            int[] numbers = new int[numbersAsString.length];
            //converting text to numbers
            for (int i = 0; i < numbersAsString.length; i++) {
                numbers[i] = Integer.parseInt(numbersAsString[i]);
            }
            scanner.close();
            return numbers;
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + fileName);
            e.printStackTrace();
            return new int[0];
        } catch (NumberFormatException e) {
            System.err.println("The file contains not number characters");
            e.printStackTrace();
            return new int[0];
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.run();
        System.out.println(server.calculateSumOfAllResponse());
    }

}

/*
            982473,2349587,356,34567,9678123,6789,345623,67845,8,23,9,3456,234,67,237698,2346,32548,
            789,15467,7890,23462,9234,8564,3,90,6,67,8975645,667,7823,48,9,1,8,89,45346,788,45687,9,
            2789,34567845,257,987,4,768,43,455,234,4567,87
            */