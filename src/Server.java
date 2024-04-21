import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class Server {

    private ServerSocket server;
    private int[] array = {
            982473,2349587,356,34567,9678123,6789,345623,67845,8,23,9,3456,234,67,237698,2346,32548,
            789,15467,7890,23462,9234,8564,3,90,6,67,8975645,667,7823,48,9,1,8,89,45346,788,45687,9,
            2789,34567845,257,987,4,768,43,455,234,4567,87
    };
    private int numOfEdgeNodes = 3;
    private int chunkSize;
    private int totalSum = 0;
    private int [][]chunks;
    int respondedNodes = 0;



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
        try {
            while (true) {
                Socket client = server.accept();

                Thread clientThread = new Thread(new ClientHandler(client, chunks[clientNumber]));
                clientNumber++;
                clientThread.start();

            }
        } catch (IOException e) {
            e.printStackTrace();
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

    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }

}