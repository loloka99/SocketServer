package Server;


import java.util.ArrayList;

public class ElapsedTimes {
    private ArrayList<Long> clientElapsedTimes = new ArrayList<>();
    private long totalElapsedTime;

    public void addClientElapsedTime(long elapsedTime){
        clientElapsedTimes.add(elapsedTime);
    }
    public void setTotalElapsedTime(long totalElapsedTime) {
        this.totalElapsedTime = totalElapsedTime;
    }

    public long getTotalElapsedTime() {
        return totalElapsedTime;
    }
    public ArrayList<Long> getClientElapsedTimes() {
        return clientElapsedTimes;
    }
}
