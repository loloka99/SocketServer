package Server.DynamicServer;

import Server.ElapsedTimes;
import Server.TimeMeasurement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private  int result = 0;
    private final TimeMeasurement timer = new TimeMeasurement();
    private final ElapsedTimes elapsedTimes;
    private int numOfResolvedTasks = 0;

    public ClientHandler(Socket clientSocket, ElapsedTimes elapsedTimes) {
        this.clientSocket = clientSocket;
        this.elapsedTimes = elapsedTimes;
    }

    @Override
    public void run() {
        try (
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
                ){
            timer.start();
            while (true) {
                String miniTask = DynamicServer.getMiniTask();
                if (miniTask.equals("DONE")){
                    sendDone();
                    break;
                }
                out.println(miniTask);
                String request = in.readLine();
                int requestAsInt = Integer.parseInt(request);
                result += requestAsInt;
                numOfResolvedTasks++;
            }
            timer.stop();
            System.out.println("Client " + clientSocket.getPort() + " sent " + numOfResolvedTasks + " tasks.");
            elapsedTimes.addClientElapsedTime(timer.getElapsedTime());
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
