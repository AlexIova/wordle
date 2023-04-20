import java.util.*;

public class NotificheDB {
    
    private Queue<DataGame> queue;

    public NotificheDB() {
        queue = new LinkedList<>();
    }

    public void add(DataGame data) {
        synchronized(this){
            queue.add(data);
        }
    }

    public DataGame get() {
        synchronized(this){
            return queue.poll();
        }
    }

    public boolean isEmpty() {
        synchronized(this){
            return queue.isEmpty();
        }
    }


}
