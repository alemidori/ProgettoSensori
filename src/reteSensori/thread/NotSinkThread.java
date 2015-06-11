package reteSensori.thread;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import com.google.gson.Gson;
import reteSensori.classi.Nodo;
import reteSensori.simulatori.Buffer;
import reteSensori.simulatori.Misurazione;
import reteSensori.singleton.SingletonBattery;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Alessandra on 03/06/15.
 */
public class NotSinkThread implements Runnable {
    private Buffer<Misurazione> buffer;
    private ServerSocket serverSocket;
    private boolean stop = false;
    private Socket socket;
    private DataOutputStream out;
    private int porta, frequenza;

    public NotSinkThread(int p, int f) {
        porta = p;
        frequenza = f;
        buffer = Nodo.getBuffer();
        try {
            serverSocket = new ServerSocket(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (!stop) {
            if (SingletonBattery.getInstance().getLevel() > 0) {
                try {
                    System.out.println("In ascolto...");
                    socket = serverSocket.accept();
                    System.out.println("Connessione con il sink...");
                    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String req = br.readLine();
                    out = new DataOutputStream(socket.getOutputStream());

                    if (Objects.equals(req, "misurazioni")) {
                        System.out.println("Richiesta: " + req);
                        ArrayList<Misurazione> lista = (ArrayList<Misurazione>) buffer.leggi();
                        Gson gson = new Gson();
                        String gsonList = gson.toJson(lista);
                        out.writeBytes(gsonList + '\n');
                        Nodo.updateBattery("trasmissione");
                    }

                    if (Objects.equals(req, "elezione")) {
                        System.out.println("Richiesta: " + req);
                        out = new DataOutputStream(socket.getOutputStream());
                        int perc = (int) (Nodo.getPercentBattery());
                        out.writeByte(perc);
                        Nodo.updateBattery("trasmissione");
                    }
                    if (Objects.equals(req, "eletto")) {
                        System.out.println("Sono stato eletto sink");
                        stopListening();
                        new Thread(new SinkThread(porta, frequenza)).start();
                    }
                    if (Objects.equals(req, "msgManager")) {
                        Socket socketManager = new Socket("localhost",5555);
                        DataOutputStream outToManager = new DataOutputStream(socketManager.getOutputStream());
                        outToManager.writeBytes("La rete di sensori non e' piu' disponibile");
                        socketManager.close();
                        Nodo.updateBattery("trasmissioneGestore");
                        System.out.println("Ho notificato il gestore di rete non disponibile");
                        stopListening();
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                stopListening();

            }
        }
            try {

                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

    }

    public void stopListening() {
        stop = true;
    }
}
