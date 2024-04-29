package Server.StaticServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private int[] array ;
    private  int result;

    public ClientHandler(Socket clientSocket, int[] array) {
        this.clientSocket = clientSocket;
        this.array = array;
    }

    @Override
    public void run() {
        try (
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                ){
            //Converting to string
            String arrayAsString = Arrays.stream(array)
                    .mapToObj(String::valueOf)
                    .collect(Collectors.joining(" "));
            out.println(arrayAsString);
            String response = in.readLine();
            result = Integer.parseInt(response);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public int getResult() {
        return result;
    }
}
