package reteSensori.simulatori;

import java.util.Random;

/**
 * Created by civi on 22/04/15.
 */
public abstract class Simulator implements Runnable {
    protected long startTime;
    protected boolean needToStop=false;
    protected Random rnd = new Random();


    public void setStartTime(){
        startTime=System.currentTimeMillis();
    }

    public long deltaTime(){
        return System.currentTimeMillis()-startTime;
    }

    public void stopMeGently(){
        needToStop=true;
    }
}
