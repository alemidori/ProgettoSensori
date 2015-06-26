package reteSensori.classi;


import reteSensori.simulatori.*;
import reteSensori.singleton.SingletonBattery;
import reteSensori.thread.NotSinkThread;
import reteSensori.thread.SinkThread;

/**
 * Created by Alessandra on 04/06/15.
 */
public class Nodo {
    private String tipo;
    private static int frequenza, port;
    private static boolean sink;
    private static Simulator simulator;
    private static BufferMisurazioni<Misurazione> buffer = new BufferMisurazioni<>();
    private static SinkThread sinkThread;
    private static NotSinkThread notSinkThread;
    private static float percentBattery;

    public Nodo(String t, int b, boolean s, int f) {
        tipo = t;
        SingletonBattery.getInstance().setLevel(b);
        sink = s;
        frequenza = f;
    }

    public void startNodo() {
        switch (tipo) {
            case "temperatura":
                port = 1111;
                simulator = new TemperatureSimulator(buffer);
                new Thread(simulator).start();
                break;

            case "luminosita":
                port = 2222;
                simulator = new LightSimulator(buffer);
                new Thread(simulator).start();
                break;

            case "PIR1":
                port = 3333;
                simulator = new PIR1Simulator(buffer);
                new Thread(simulator).start();
                break;

            case "PIR2":
                port = 4444;
                simulator = new PIR2Simulator(buffer);
                new Thread(simulator).start();
                break;

            default:
                System.out.println("Errore: tipo non specificato.");
                break;
        }


        System.out.println("PROCESSO NODO AVVIATO" + '\n' + "********************************");

        if (sink) {
            //se sono sink invio le richieste e aspetto le risposte
            sinkThread = new SinkThread(port, frequenza);
            new Thread(sinkThread).start();


        } else {
            //se non sono sink mi metto in attesa di una richiesta dal sink
            notSinkThread = new NotSinkThread(port,frequenza);
            new Thread(notSinkThread).start();
        }
    }

    public static Buffer<Misurazione> getBuffer() {
        return buffer;
    }

    public static float getPercentBattery() {
        return percentBattery;
    }

    public synchronized static void updateBattery(String s) {
        int level = SingletonBattery.getInstance().getLevel();
        int newLevel;
        int readingCost = 2;
        int trasmissionCost = 5;
        int trasmissionToManager = trasmissionCost * 4;
        if (level > 0) {
            switch (s) {
                case Messaggi.LETTURA:
                    newLevel = level - readingCost;
                    SingletonBattery.getInstance().setLevel(newLevel);
                    break;
                case Messaggi.TRASMISSIONE_NODO:
                    newLevel = level - trasmissionCost;
                    SingletonBattery.getInstance().setLevel(newLevel);
                    break;
                case Messaggi.TRASMISSIONE_GESTORE:
                    newLevel = level - trasmissionToManager;
                    SingletonBattery.getInstance().setLevel(newLevel);
                    break;
                default:
                    System.out.println("attivita' non specificata");
            }

            percentBattery = ((level * 100) / SingletonBattery.getInstance().getMaxLevel());
            System.out.println("Livello batteria: " + level + " Percentuale: " + percentBattery + "%");

        } else {
            simulator.stopMeGently();
        System.out.println("Sensore stoppato");
        }

    }


}
