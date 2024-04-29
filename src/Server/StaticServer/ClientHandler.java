package Server.StaticServer;

import Server.CSVWriter;
import Server.ElapsedTimes;
import Server.TimeMeasurement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final int[] array ;
    private  int result;
    private final TimeMeasurement timer = new TimeMeasurement();
    private final ElapsedTimes elapsedTimes;


    public ClientHandler(Socket clientSocket, int[] array, ElapsedTimes elapsedTimes) {
        this.clientSocket = clientSocket;
        this.array = array;
        this.elapsedTimes = elapsedTimes;
    }

    @Override
    public void run() {
        try  {
            timer.start();
            try (
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                ) {
                //Converting to string
                String arrayAsString = Arrays.stream(array)
                        .mapToObj(String::valueOf)
                        .collect(Collectors.joining(" "));
                out.println(arrayAsString);
                String response = in.readLine();
                result = Integer.parseInt(response);
                timer.stop();
                // Add elapsed time to the list
                elapsedTimes.addClientElapsedTime(timer.getElapsedTime());
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public int getResult() {
        return result;
    }

    public ElapsedTimes getElapsedTimes() {
        return elapsedTimes;
    }
}
