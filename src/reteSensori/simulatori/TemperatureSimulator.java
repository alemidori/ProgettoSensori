package reteSensori.simulatori;

/**
 * Created by civi on 21/04/15.
 */
public class TemperatureSimulator extends Simulator implements Runnable {

    private Buffer<Misurazione> measurementsQueue;

    private final double A = 10;
    private final double W = 0.1;
    private final double PHI = rnd.nextDouble();

    public TemperatureSimulator(Buffer<Misurazione> meausrementsQueue){
        this.measurementsQueue = meausrementsQueue;
    }


    @Override
    public void run() {
        setStartTime();

        double i = 0.1;
        long waitingTime;

        while(!needToStop){

            double temperature = getTemperature(i);
            measurementsQueue.aggiungi(new Misurazione("Temperature", temperature + "", deltaTime()));

            try {
                waitingTime = 1000 + (int)(Math.random()*3000);
                Thread.sleep(waitingTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            i+=0.1;

        }

    }

    private double getTemperature(double t){
        return A * Math.sin(W*t+PHI)+20+rnd.nextGaussian()*0.2;

    }
}
