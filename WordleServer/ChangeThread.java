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

    @Override
    public void run() {
        timer = new Timer();
        timer.schedule(new changeWord(wp), 0, time * 1000);
    }

    class changeWord extends TimerTask {

        WordPicker wp;

        public changeWord(WordPicker wp) {
            this.wp = wp;
        }

        @Override
        public void run() {
            wp.changeWord();
        }
    }

}
