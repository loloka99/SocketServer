package Client.StaticClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.stream.IntStream;

public class Client implements Runnable {
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private boolean done;

    @Override
    public void run() {
        try {
            client = new Socket("127.0.0.1", 9999);
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            String input = in.readLine();

            //calculate sum of divisors
            int sum = Arrays.stream(input.split(" "))
                    .mapToInt(Integer::parseInt)
                    .map(number -> IntStream.rangeClosed(1, number)
                            .filter(i -> number % i == 0)
                            .sum())
                    .sum();

            out.println(sum);

            shutDown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shutDown() {
        done = true;
        try {
            in.close();
            out.close();
            if (!client.isClosed()) {
                client.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }
}
