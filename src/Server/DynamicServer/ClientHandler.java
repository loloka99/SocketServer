package Server.DynamicServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private  int result = 0;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
                ){
            while (true) {
                String miniTask = DynamicServer.getMiniTask();
                if (miniTask.equals("DONE")){
                    sendDone();
                    break;
                }
                System.out.println("Sending: " + miniTask);
                out.println(miniTask);
                String request = in.readLine();
                System.out.println("Received: " + request);
                int requestAsInt = Integer.parseInt(request);
                result += requestAsInt;
            }
        } catch (IOException e){
            System.err.println("Error occurred while handling client.");
        }
    }

    public int getResult() {
        return result;
    }

    public void sendDone() {
        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            out.println("DONE");
        } catch (IOException e) {
            System.err.println("Error occurred while sending DONE message.");
        }
    }
}
