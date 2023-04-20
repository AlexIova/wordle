import java.util.*;

public class NotificheDB {
    
    private Queue<StatisticMsg> queue;

    public NotificheDB() {
        queue = new LinkedList<>();
    }

    public void add(StatisticMsg msg) {
        synchronized(this){
            queue.add(msg);
        }
    }

    public StatisticMsg pop() {
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
