package gestore.gateway;

import com.google.gson.Gson;
import reteSensori.classi.Messaggi;
import reteSensori.simulatori.Misurazione;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Objects;
import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;

/**
 * Created by Alessandra on 07/06/15.
 */
public class ReadMisurazioniThread implements Runnable {
    private ArrayList<Misurazione> listaAll = new ArrayList<>();
    private static ArrayList<Misurazione> listaTemp = new ArrayList<>();
    private static ArrayList<Misurazione> listaLum = new ArrayList<>();
    private static ArrayList<Misurazione> listaPIR1 = new ArrayList<>();
    private static ArrayList<Misurazione> listaPIR2 = new ArrayList<>();

    @Override
    public void run() {
        Gson gson = new Gson();
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

        if (Objects.equals(tipo, "temperatura")) {
            long rec = 0;
            if (!listaTemp.isEmpty()) {
                for (Misurazione m : listaTemp) {
                    long time = m.getTimestamp();
                    if (time > rec) {
                        rec = time;
                        recente = m;
                        response = gson.toJson(recente);
                    }
                }
            } else {
                return Messaggi.MISURAZIONE_NON_DISPONIBILE;
            }
        }
        if (Objects.equals(tipo, "luminosita")) {
            long rec = 0;
            if (!listaLum.isEmpty()) {
                for (Misurazione m : listaLum) {
                    long time = m.getTimestamp();
                    if (time > rec) {
                        rec = time;
                        recente = m;
                        response = gson.toJson(recente);
                    }
                }
            } else {
                return Messaggi.MISURAZIONE_NON_DISPONIBILE;
            }
        }

        return response;
    }

    public static String getMedia(String tipo, String tempoInizio, String tempoFine) {
        String toReturn = null;
        ArrayList<Double> valori = new ArrayList<>();
        int inizio = Integer.parseInt(tempoInizio) * 1000;
        int fine = Integer.parseInt(tempoFine) * 1000;

        if (Objects.equals(tipo, "temperatura")) {
            double somma = 0;

            for (Misurazione m : listaTemp) {
                if (m.getTimestamp() >= inizio && m.getTimestamp() <= fine) {
                    valori.add(Double.parseDouble(m.getValue()));
                }

            }
            if (!valori.isEmpty()) {
                for (int i = 0; i < valori.size() - 1; i++) {
                    somma = somma + valori.get(i);
                }
                double med = somma / valori.size();
                toReturn = String.valueOf(med);
            } else {
                toReturn = Messaggi.MEDIA_NON_DISPONIBILE;
            }
        }
        if (Objects.equals(tipo, "luminosita")) {
            double somma = 0;
            for (Misurazione m : listaLum) {
                if (m.getTimestamp() >= inizio && m.getTimestamp() <= fine) {
                    valori.add(Double.parseDouble(m.getValue()));
                }

            }
            if (!valori.isEmpty()) {
                for (int i = 0; i < valori.size() - 1; i++) {
                    somma = somma + valori.get(i);
                }
                double med = somma / valori.size();
                toReturn = String.valueOf(med);
            } else {
                toReturn = Messaggi.MEDIA_NON_DISPONIBILE;
            }
        }
        return toReturn;
    }

    public static String getMinMax(String tipo, String tempoInizio, String tempoFine) {
        String toReturn = null;
        Gson gson = new Gson();
        int inizio = Integer.parseInt(tempoInizio) * 1000;
        int fine = Integer.parseInt(tempoFine) * 1000;

        ArrayList<String> minMax = new ArrayList<>();
        if (Objects.equals(tipo, "temperatura")) {
            ArrayList<Double> valori = new ArrayList<>();
            for (Misurazione m : listaTemp) {
                if (m.getTimestamp() >= inizio && m.getTimestamp() <= fine) {
                    valori.add(Double.parseDouble(m.getValue()));
                }
            }
            if (!valori.isEmpty()) {
                double max = 0;
                for (double v : valori) {
                    if (v > max) {
                        max = v;
                    }
                }
                minMax.add(String.valueOf(max));

                double min = valori.get(0);

                for (double v : valori) {
                    if (v < min) {
                        min = v;
                    }
                }

                minMax.add(String.valueOf(min));
                toReturn = gson.toJson(minMax);
            } else {
                toReturn = Messaggi.MISURAZIONE_NON_DISPONIBILE;
            }
        }

        if (Objects.equals(tipo, "luminosita")) {
            ArrayList<Double> valori = new ArrayList<>();
            for (Misurazione m : listaLum) {
                if (m.getTimestamp() >= inizio && m.getTimestamp() <= fine) {
                    valori.add(Double.parseDouble(m.getValue()));
                }
            }
            if (!valori.isEmpty()) {
                double max = 0;
                for (double v : valori) {
                    if (v > max) {
                        max = v;
                    }
                }
                minMax.add(String.valueOf(max));

                double min = valori.get(0);

                for (double v : valori) {
                    if (v < min) {
                        min = v;
                    }
                }
                minMax.add(String.valueOf(min));
                toReturn = gson.toJson(minMax);
            } else {
                toReturn = Messaggi.MISURAZIONE_NON_DISPONIBILE;
            }
        }
        return toReturn;
    }

    public static String getPresenza(String tipo, String tempoInizio, String tempoFine) {
        String toReturn = null;
        int inizio = Integer.parseInt(tempoInizio) * 1000;
        int fine = Integer.parseInt(tempoFine) * 1000;
        if (Objects.equals(tipo, "pir1")) {
            ArrayList<Misurazione> valori = new ArrayList<>();
            for (Misurazione m : listaPIR1) {
                if (m.getTimestamp() >= inizio && m.getTimestamp() <= fine) {
                    valori.add(m);
                }
            }
            if (!valori.isEmpty()) {
                toReturn = String.valueOf(valori.size());
            } else {
                toReturn = Messaggi.MISURAZIONE_NON_DISPONIBILE;
            }
        }
        if (Objects.equals(tipo, "pir2")) {
            ArrayList<Misurazione> valori = new ArrayList<>();
            for (Misurazione m : listaPIR2) {
                if (m.getTimestamp() >= inizio && m.getTimestamp() <= fine) {
                    valori.add(m);
                }
            }
            if (!valori.isEmpty()) {
                toReturn = String.valueOf(valori.size());
            } else {
                toReturn = Messaggi.MISURAZIONE_NON_DISPONIBILE;
            }
        }
        return toReturn;
    }

    public static String getMediaPres(String tempoInizio, String tempoFine) {
        String toReturn;
        int inizio = Integer.parseInt(tempoInizio) * 1000;
        int fine = Integer.parseInt(tempoFine) * 1000;
        ArrayList<Misurazione> valori = new ArrayList<>();
        for (Misurazione m : listaPIR1) {
            if (m.getTimestamp() >= inizio && m.getTimestamp() <= fine) {
                valori.add(m);
            }
        }
        int control = 0;
        if(!valori.isEmpty()) {
            control = valori.size();
        }
        for (Misurazione m : listaPIR2) {
            if (m.getTimestamp() >= inizio && m.getTimestamp() <= fine) {
                valori.add(m);
            }
        }
        if (!valori.isEmpty() && valori.size() != control) {
            toReturn = String.valueOf(valori.size() / 2);
        } else {
            toReturn = Messaggi.MEDIA_NON_DISPONIBILE;
        }

        return toReturn;
    }
}
