package gestore.gateway;

import com.google.gson.Gson;
import reteSensori.simulatori.Misurazione;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;
import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;

/**
 * Created by Alessandra on 07/06/15.
 */
public class ReadMisurazioniThread implements Runnable {
    private Gson gson = new Gson();
    private ArrayList<Misurazione> listaAll = new ArrayList<>();
    private static ArrayList<Misurazione> listaTemp = new ArrayList<>();
    private static ArrayList<Misurazione> listaLum = new ArrayList<>();
    private static ArrayList<Misurazione> listaPIR1 = new ArrayList<>();
    private static ArrayList<Misurazione> listaPIR2 = new ArrayList<>();

    @Override
    public void run() {

        try {
            ServerSocket serverSocket = new ServerSocket(5555);
            while (true) {
                Socket socket = serverSocket.accept();
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String res = br.readLine();

                Type listType = new TypeToken<ArrayList<Misurazione>>() {
                }.getType();

                ArrayList<Misurazione> nuoveMis = gson.fromJson(res, listType);
                listaAll.addAll(nuoveMis);

                System.out.println("Lista completa: \n" + gson.toJson(listaAll));
                System.out.println("***************************************");


                for (int i = 0; i < listaAll.size() - 1; i++) {

                    if (Objects.equals(listaAll.get(i).getType(), "Temperature")) {
                        listaTemp.add(listaAll.get(i));
                    }
                    if (Objects.equals(listaAll.get(i).getType(), "Light")) {
                        listaLum.add(listaAll.get(i));
                    }
                    if (Objects.equals(listaAll.get(i).getType(), "PIR1")) {
                        listaPIR1.add(listaAll.get(i));
                    }
                    if (Objects.equals(listaAll.get(i).getType(), "PIR2")) {
                        listaPIR2.add(listaAll.get(i));

                    }
                }

                System.out.println("Prendo l'ultimo elemento di ogni lista(che non è il più recente)");

                if (!listaTemp.isEmpty()) {
                    System.out.println("Tipo: " + listaTemp.get(listaTemp.size() - 1).getType()
                            + " Valore: " + listaTemp.get(listaTemp.size() - 1).getValue()
                            + " Timestamp " + listaTemp.get(listaTemp.size() - 1).getTimestamp());
                }
                if (!listaLum.isEmpty()) {
                    System.out.println("Tipo: " + listaLum.get(listaLum.size() - 1).getType()
                            + " Valore: " + listaLum.get(listaLum.size() - 1).getValue()
                            + " Timestamp " + listaLum.get(listaLum.size() - 1).getTimestamp());
                }
                if (!listaPIR1.isEmpty()) {
                    System.out.println("Tipo: " + listaPIR1.get(listaPIR1.size() - 1).getType()
                            + " Valore: " + listaPIR1.get(listaPIR1.size() - 1).getValue()
                            + " Timestamp " + listaPIR1.get(listaPIR1.size() - 1).getTimestamp());
                }

                if (!listaPIR2.isEmpty()) {
                    System.out.println("Tipo: " + listaPIR2.get(listaPIR2.size() - 1).getType()
                            + " Valore: " + listaPIR2.get(listaPIR2.size() - 1).getValue()
                            + " Timestamp " + listaPIR2.get(listaPIR2.size() - 1).getTimestamp());
                }
                System.out.println("***************************************");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Misurazione getRecente(String tipo) {
        Misurazione recente = null;

        if(Objects.equals(tipo, "temperatura")) {
            long rec = 0;
            for (Misurazione m : listaTemp) {
                long time = m.getTimestamp();
                if (time > rec) {
                    rec = time;
                    recente = m;
                }
            }
        }
        if(Objects.equals(tipo, "luminosita")) {
            long rec = 0;
            for (Misurazione m : listaLum) {
                long time = m.getTimestamp();
                if (time > rec) {
                    rec = time;
                    recente = m;
                }
            }
        }
        if(Objects.equals(tipo, "pir1")) {
            long rec = 0;
            for (Misurazione m : listaPIR1) {
                long time = m.getTimestamp();
                if (time > rec) {
                    rec = time;
                    recente = m;
                }
            }
        }
        if(Objects.equals(tipo, "pir2")) {
            long rec = 0;
            for (Misurazione m : listaPIR2) {
                long time = m.getTimestamp();
                if (time > rec) {
                    rec = time;
                    recente = m;
                }
            }
        }
        return recente;
    }
}