package reteSensori.simulatori;

/**
 * Created by civi on 21/04/15.
 */
public class PIR1Simulator extends Simulator implements Runnable {

    private Buffer measurementsQueue;
    private final double H = 15;
    private final double L = 1;
    private final double ALPHA = 0.9;

    public PIR1Simulator(Buffer measurementsQueue){
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
                measurementsQueue.aggiungi(new Misurazione("PIR1", "ON", deltaTime()));
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
