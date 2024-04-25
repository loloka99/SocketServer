package Server;

public interface ServerStrategy {
    public void run();
    public int calculateTotal();
    public int getTotalSum();
    public long getElapsedTime();
}
