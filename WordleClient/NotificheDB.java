import java.util.*;

public class NotificheDB {
    
    private Queue<StatisticMsg> queue;

    public NotificheDB() {
        queue = new LinkedList<>();
    }

    /**
     * Adds a StatisticMsg to the queue in a thread-safe manner.
     *
     * @param msg the StatisticMsg to add to the queue
     */
    public void add(StatisticMsg msg) {
        synchronized (this) {
            queue.add(msg);
        }
    }


    /**
     * Removes and returns the head of the queue, or returns null if the queue is empty.
     * This method is synchronized to ensure thread safety.
     * 
     * @return the head of the queue, or null if the queue is empty
     */
    public StatisticMsg pop() {
        synchronized(this){
            return queue.poll();
        }
    }


    /**
     * Checks if the queue is empty. Method syncronized on this onject.
     * 
     * @return true if the queue is empty, false otherwise
     */
    public boolean isEmpty() {
        synchronized(this){
            return queue.isEmpty();
        }
    }



}
