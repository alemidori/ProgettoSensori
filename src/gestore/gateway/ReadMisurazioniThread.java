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
            ServerSocket serverSocket = new ServerSocket(6666);
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

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getRecente(String tipo) {
        String response = null;
        Gson gson = new Gson();
        Misurazione recente;

        if(Objects.equals(tipo, "temperatura")) {
            long rec = 0;
            if(!listaTemp.isEmpty()) {
                for (Misurazione m : listaTemp) {
                    long time = m.getTimestamp();
                    if (time > rec) {
                        rec = time;
                        recente = m;
                        response = gson.toJson(recente);
                    }
                }
            }
            else{
                return "Misurazione non disponibile";
            }
        }
        if(Objects.equals(tipo, "luminosita")) {
            long rec = 0;
            if(!listaLum.isEmpty()) {
                for (Misurazione m : listaLum) {
                    long time = m.getTimestamp();
                    if (time > rec) {
                        rec = time;
                        recente = m;
                        response = gson.toJson(recente);
                    }
                }
            }
            else{
                return "Misurazione non disponibile";
            }
        }

        return response;
    }

    public static String getMedia(String tipo, String tempoInizio, String tempoFine){
        String media = null;
        ArrayList<Double> valori = new ArrayList<>();
        int inizio = Integer.parseInt(tempoInizio)*1000;
        int fine = Integer.parseInt(tempoFine)*1000;

        if(Objects.equals(tipo, "temperatura")) {
            double somma=0;
            for (Misurazione m : listaTemp) {
                if(m.getTimestamp()>=inizio && m.getTimestamp()<=fine){
                    valori.add(Double.parseDouble(m.getValue()));
                }
            }
            for(int i=0; i<valori.size()-1;i++){
                somma = somma +valori.get(i);
            }
            double med = somma/valori.size();
            media = String.valueOf(med);

        }
        if(Objects.equals(tipo, "luminosita")) {
            double somma=0;
            for (Misurazione m : listaLum) {
                if(m.getTimestamp()>=inizio && m.getTimestamp()<=fine){
                    valori.add(Double.parseDouble(m.getValue()));
                }
            }
            for(int i=0; i<valori.size()-1;i++){
                somma = somma +valori.get(i);
            }
            double med = somma/valori.size();
            media = String.valueOf(med);

        }
        return media;
    }
}
