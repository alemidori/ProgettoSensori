package reteSensori.thread;

import com.google.gson.Gson;
import reteSensori.classi.Messaggio;
import reteSensori.classi.Nodo;
import reteSensori.simulatori.Misurazione;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Alessandra on 03/06/15.
 */
public class SinkThread implements Runnable {
    private int porta;
    private int frequenza;
    private boolean stop = false;
    private Socket socket;
    private Gson gson;
    private String richiesta;
    private DataOutputStream outToManager;

    public SinkThread(int p, int f) {
        richiesta = "misurazioni";
        porta = p;
        frequenza = f;
        gson = new Gson();
    }

    @Override
    public void run() {
        int[] porte = {1111, 2222, 3333, 4444};
        while (!stop) {
            try {
                int freq = frequenza * 1000;
                Thread.sleep(freq);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int p : porte) {
                if (p != porta) {
                    try {
                        socket = new Socket("localhost", p);
                        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                        out.writeBytes(richiesta + '\n');
                        Nodo.updateBattery("trasmissione");
                        System.out.println("Ho richiesto le misurazioni");
                        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String res = br.readLine();
                        System.out.println(res + '\n');
                        Object what = gson.fromJson(res, Object.class);
                        //se ciò che ricevo è un messaggio
                        if (what instanceof Messaggio) {
                            String content = ((Messaggio) what).getTipoMessaggio();
                            if (Objects.equals(content, "batteria")) {
                                System.out.println("Un nodo e' scarico");
                                Messaggio msgToManager = new Messaggio("batteria");
                                Socket socketManager = new Socket("localhost", 5555);
                                outToManager = new DataOutputStream(socketManager.getOutputStream());
                                String msg = gson.toJson(msgToManager);
                                outToManager.writeBytes(msg);

                                /*TODO
                                mi faccio restituire la porta del nodo con la batteria scarica
                                quindi chiudo le comunicazioni con lui*/

                            }

                            
                        }
                        //se ciò che ricevo è una lista di misurazioni
                        if (what instanceof ArrayList) {
                            System.out.println(res);
                            /*genero la stringa con le mie misurazioni*/
                            ArrayList<Misurazione> lista = (ArrayList<Misurazione>) Nodo.getBuffer().leggi();
                            String gsonList = gson.toJson(lista);
                            String liste = res.concat(gsonList); //concateno la mia alle liste che ho ricevuto
                            outToManager.writeBytes(liste);
                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopRequest() {
        stop = true;
    }
}
