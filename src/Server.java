import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

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



    public Server() {
        try {
            server = new ServerSocket(9999);
            chunkSize = array.length % numOfEdgeNodes;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        int respondedNodes = 0;
        try {
            while (respondedNodes < numOfEdgeNodes) {
                Socket client = server.accept();
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                // Generál egy véletlen számot
                int randomNumber = (int) (Math.random() * 100);

                // Elküldi a véletlen számot a kliensnek
                out.println(randomNumber);

                // Fogadja az eredményt a klienstől
                String result = in.readLine();
                System.out.println("Kapott eredmény: " + result);

                

                // Bezárja az adatfolyamokat és a socketet
                in.close();
                out.close();
                client.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }

}