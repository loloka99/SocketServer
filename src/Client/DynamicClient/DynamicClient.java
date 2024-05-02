package Client.DynamicClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.stream.IntStream;

public class DynamicClient implements Runnable {
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;

    @Override
    public void run() {
        try {
            client = new Socket("127.0.0.1", 9999);
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            while (true) {
                String input = in.readLine();
                if (input.equals("DONE")) {
                    break;
                }
                // Calculate sum of divisors
                int sum = Arrays.stream(input.split(" "))
                        .mapToInt(Integer::parseInt)
                        .filter(number -> number != 0)
                        .map(number -> IntStream.rangeClosed(1, number)
                                .filter(i -> number % i == 0)
                                .sum())
                        .sum();
                out.println(sum);
            }
            out.println("DONE");
            shutDown();
        } catch (IOException e) {
            System.err.println("Error occurred while running client.");
        }
    }

    public void shutDown() {
        try {
            in.close();
            out.close();
            if (!client.isClosed()) {
                client.close();
            }
        } catch (IOException e) {
            System.err.println("Error occurred while shutting down client.");
        }
    }

    public static void main(String[] args) {
        DynamicClient client = new DynamicClient();
        client.run();
    }
}
