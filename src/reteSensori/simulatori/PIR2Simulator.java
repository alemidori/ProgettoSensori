package reteSensori.simulatori;

/**
 * Created by civi on 06/05/15.
 */
public class PIR2Simulator extends Simulator implements Runnable {

    private Buffer measurementsQueue;
    private final double H = 4;
    private final double L = 1;
    private final double ALPHA = 0.8;

    public PIR2Simulator(Buffer measurementsQueue){
        this.measurementsQueue =measurementsQueue;
    }

    @Override
    public void run() {

        setStartTime();

        long waitingTime;

        while(!needToStop()) {
            waitingTime = generateWaitingTime();

            try {
                Thread.sleep(waitingTime);
                measurementsQueue.aggiungi(new Misurazione("PIR2", "ON", deltaTime()));
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private long generateWaitingTime(){
        double U = Math.random();
        return (long) (Math.pow(-(Math.pow(U * H, ALPHA) - Math.pow(U * L, ALPHA) - Math.pow(H, ALPHA)) / (Math.pow(H, ALPHA) * Math.pow(L, ALPHA)), -1 / ALPHA) * 1000);
    }
}
