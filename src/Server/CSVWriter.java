package Server;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CSVWriter {
    private final String csvFile = "elapsedTimes.csv";
    private ArrayList<Long> clientElapsedTimes = new ArrayList<>();
    private long totalElapsedTime;

    public CSVWriter(ArrayList<Long> clientElapsedTimes, long totalElapsedTime) {
        this.clientElapsedTimes = clientElapsedTimes;
        this.totalElapsedTime = totalElapsedTime;
    }

    public void writeToFile(String filePath) {
        FileWriter csvWriter = null;
        try{
            csvWriter = new FileWriter(filePath, true);

            // Új sor hozzáadása a CSV fájlhoz
            for (int i = 0; i < clientElapsedTimes.size(); i++) {
                csvWriter.append(String.valueOf(clientElapsedTimes.get(i)));
                if (i != clientElapsedTimes.size() - 1) {
                    csvWriter.append(',');
                }
            }
            csvWriter.append(String.valueOf(totalElapsedTime));
            csvWriter.append('\n');
        }catch (Exception e){
            e.printStackTrace();
    }finally {
            if (csvWriter != null) {
                try {
                    csvWriter.flush();
                    csvWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        }
}
