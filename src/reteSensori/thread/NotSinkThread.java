package reteSensori.thread;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import com.google.gson.Gson;
import reteSensori.classi.Messaggio;
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

    public NotSinkThread(int p, Buffer b) {
        buffer = b;
        try {
            serverSocket = new ServerSocket(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (!stop) {
            try {
                System.out.println("In ascolto...");
                socket = serverSocket.accept();
                System.out.println("Connessione con il sink...");
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String req = br.readLine();
                System.out.println("Richiesta " + req);
                out = new DataOutputStream(socket.getOutputStream());

                if (Objects.equals(req, "misurazioni")) {
                    if (Nodo.getPercentBattery() < 25.0) {
                        Messaggio messaggio = new Messaggio("batteria");
                        Gson gsonMsg = new Gson();
                        String msg = gsonMsg.toJson(messaggio);
                        out.writeBytes(msg);

                    }
                    ArrayList<Misurazione> lista = (ArrayList<Misurazione>) buffer.leggi();
                    Gson gson = new Gson();
                    String gsonList = gson.toJson(lista);
                    out.writeBytes(gsonList + '\n');
                    Nodo.updateBattery("trasmissione");
                }

                if (Objects.equals(req, "elezione")) {
                    out = new DataOutputStream(socket.getOutputStream());
                    out.writeBytes("Nodo: " + buffer.leggi().get(0).getType() + "Livello batteria: " + SingletonBattery.getInstance().getLevel());
                    Nodo.updateBattery("trasmissione");
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void stopListening() {
        stop = true;
    }
}
