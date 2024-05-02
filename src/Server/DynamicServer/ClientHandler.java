package Server.DynamicServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private long finishTime;
    private boolean finished = false;
    private  int result = 0;

    public ClientHandler(Socket clientSocket, int[] array) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                ){
            String request  = in.readLine();
            System.out.println("Received: " + request);
            while (!request.equals("DONE")) {
                System.out.println("Received: " + request);
                int requestAsInt = Integer.parseInt(request);
                result += requestAsInt;
                synchronized (this) {
                    finished = true;
                    finishTime = System.currentTimeMillis();
                    this.notify();
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public int getResult() {
        return result;
    }

    public boolean isFinished() {
        return finished;
    }

    public long getFinishTime() {
        return finishTime;
    }

    public void sendTask(int[] miniTask) {
        //System.out.println("MiniTask:  " + Arrays.toString(miniTask));
        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            ){
            String miniTaskAsString = Arrays.stream(miniTask)
                    .mapToObj(String::valueOf)
                    .collect(Collectors.joining(" "));
            out.println(miniTaskAsString);
            String response = in.readLine();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void waitForCompletion() throws InterruptedException {
        synchronized (this) {
            while (!finished) {
                this.wait();
            }
        }
    }

    public void sendDone() {
        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            out.println("DONE");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
