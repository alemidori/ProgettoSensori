package reteSensori.simulatori;
import java.util.Random;

/**
 * Created by civi on 22/04/15.
 */
public abstract class Simulator implements Runnable {
    protected long startTime;
    private boolean stopCondition =false;
    protected Random rnd = new Random();

    public void setStartTime(){
        startTime=System.currentTimeMillis();
    }

    public long deltaTime(){
        return System.currentTimeMillis()-startTime;
    }

    public synchronized void stopMeGently() {
        stopCondition = true;
    }

    protected synchronized boolean needToStop() {
        return stopCondition;
    }
}

