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

            // Fogadja az adatot a szervertol
            String input = in.readLine();

            int sum = Arrays.stream(input.split(" "))         // Szóköz mentén felbontjuk a sztringet és egy Streammé alakítjuk az elemeket
                    .mapToInt(Integer::parseInt)                    // Az elemeket átalakítjuk egész számokká
                    .map(number -> IntStream.rangeClosed(1, number) // Minden számra alkalmazzuk az osztók összegzését
                            .filter(i -> number % i == 0)
                            .sum())
                    .sum();

            out.println(sum);
            System.out.println(sum);
            shutDown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int sumCalculator(int number){
        int sum=0;
        for(int i=1; i<=number; i++){
            if (number%i==0){
                sum+=i;
            }
        }
        return sum;
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
