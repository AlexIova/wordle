import java.util.Timer;
import java.util.TimerTask;

public class ChangeThread implements Runnable{
    
    Timer timer;
    WordPicker wp;
    int time;

    public ChangeThread(int time, WordPicker wp) {
        this.wp = wp;
        this.time = time;
    }

    /**
     * This method runs the timer and schedules a task to change the word at a set interval.
     */
    @Override
    public void run() {
        timer = new Timer();
        // Schedule a task to change the word at a set interval. Transform time to milliseconds
        timer.schedule(new changeWord(wp), 0, time * 1000);
    }


    class changeWord extends TimerTask {

        WordPicker wp;

        public changeWord(WordPicker wp) {
            this.wp = wp;
        }

        /**
         * Runs the thread by changing the word using the WordProcessor instance.
         */
        @Override
        public void run() {
            wp.changeWord();
        }

    }

}
