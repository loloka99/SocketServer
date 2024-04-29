package Server;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Task {
    public int[] readTaskFromFile(String fileName) {

        try {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);
            //reading text
            String line = scanner.nextLine();
            String[] numbersAsString = line.split("\\s+");
            int[] numbers = new int[numbersAsString.length];
            //converting text to numbers
            for (int i = 0; i < numbersAsString.length; i++) {
                numbers[i] = Integer.parseInt(numbersAsString[i]);
            }
            scanner.close();
            return numbers;
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + fileName);
            e.printStackTrace();
            return new int[0];
        } catch (NumberFormatException e) {
            System.err.println("The file contains not number characters");
            e.printStackTrace();
            return new int[0];
        }
    }
}
