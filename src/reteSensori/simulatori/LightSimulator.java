package reteSensori.simulatori;

/**
 * Created by civi on 04/05/15.
 */
public class LightSimulator extends Simulator implements Runnable {

    private Buffer<Misurazione> measurementsQueue;

    private final double A = 185;
    private final double W = 0.15;
    private final double PHI = rnd.nextDouble();

    public LightSimulator(Buffer<Misurazione> meausrementsQueue){
        this.measurementsQueue = meausrementsQueue;
    }

    @Override
    public void run() {
        setStartTime();

        double i = 0.1;
        long waitingTime;

        while(!needToStop()){

            double light = getLight(i);
            measurementsQueue.aggiungi(new Misurazione("Light", light + "", deltaTime()));

            try {
                waitingTime = 1000 + (int)(Math.random()*3000);
                Thread.sleep(waitingTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            i+=0.2;

        }

    }

    private double getLight(double t){
        return A * Math.sin(W*t+PHI)+210+rnd.nextGaussian()*5;

    }
}
