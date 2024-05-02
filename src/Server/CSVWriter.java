package Server;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CSVWriter {
    private final ArrayList<Long> clientElapsedTimes;
    private final long totalElapsedTime;

    public CSVWriter(ArrayList<Long> clientElapsedTimes, long totalElapsedTime) {
        this.clientElapsedTimes = clientElapsedTimes;
        this.totalElapsedTime = totalElapsedTime;
    }

    public void writeToFile(String filePath) {
        FileWriter csvWriter = null;
        try{
            csvWriter = new FileWriter(filePath, true);

            // Write elapsed times to CSV file
            for (Long clientElapsedTime : clientElapsedTimes) {
                csvWriter.append(String.valueOf(clientElapsedTime));
                csvWriter.append(',');
            }
            // Write total elapsed time to CSV file
            csvWriter.append(String.valueOf(totalElapsedTime));
            csvWriter.append('\n');
        }catch (Exception e){
            System.err.println("Error occurred while writing to CSV file.");
        }finally {
            if (csvWriter != null) {
                try {
                    csvWriter.flush();
                    csvWriter.close();
                } catch (IOException e) {
                    System.err.println("Error occurred while closing CSV writer.");
                }
            }
        }
    }
}
