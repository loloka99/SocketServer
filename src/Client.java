import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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
            System.out.println("Kapott szam: " + input);

            int number = Integer.parseInt(input); // szam atalakitasa inte
            System.out.println(number);
            int sum = sumCalculator(number);
            System.out.println(sum);

            // VÃ¡lasz elkÃ¼ldÃ©se a szervernek
            out.println(sum);

            // BezÃ¡rÃ¡si folyamat
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
