package reteSensori.thread;

import com.google.gson.Gson;

import com.google.gson.reflect.TypeToken;
import reteSensori.classi.Messaggi;
import reteSensori.classi.Nodo;
import reteSensori.simulatori.Misurazione;


import javax.net.SocketFactory;

import java.io.*;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.*;

import reteSensori.singleton.SingletonBattery;

/**
 * Created by Alessandra on 03/06/15.
 */
public class SinkThread implements Runnable {
    private int porta;
    private int frequenza;
    private boolean stop = false;
    private Gson gson;
    private Socket socketNewSink;

    public SinkThread(int p, int f) {
        porta = p;
        frequenza = f;
        gson = new Gson();

    }

    @Override
    public void run() {

        ArrayList<Integer> porte = new ArrayList<>();
        porte.add(1111);
        porte.add(2222);
        porte.add(3333);
        porte.add(4444);

        while (!stop) {
            try {
                int freq = frequenza * 1000;
                Thread.sleep(freq);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            DataOutputStream outToMSGManager;
            Socket socketMSGManager;
            if (Nodo.getPercentBattery() > 25.0) {

                /*********************misurazioni***********************/
                Iterator<Integer> iter = porte.iterator();
                List<Misurazione> listeMisurazioni = new ArrayList<>();
                while (iter.hasNext()) {
                    int p = iter.next();
                    if (p != porta) {
                        try {
                            //creo una socket ancora disconnessa
                            Socket socket = SocketFactory.getDefault().createSocket("localhost", p);
                            if (socket.isConnected()) {
                                try {
                                    PrintWriter checkOut = new PrintWriter(socket.getOutputStream(), true);
                                    if (checkOut.checkError()) {
                                        System.out.println("Errore in checkError");
                                    } else {
                                        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                                        out.writeBytes(Messaggi.MISURAZIONI + '\n');
                                        Nodo.updateBattery(Messaggi.TRASMISSIONE_NODO);
                                        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                                        String res = br.readLine();
                                        Type listType = new TypeToken<ArrayList<Misurazione>>() {
                                        }.getType();
                                        ArrayList<Misurazione> lista = gson.fromJson(res, listType);
                                        System.out.println(res + '\n');
                                        for (int i = 0; i < lista.size() - 1; i++) {
                                            listeMisurazioni.add(lista.get(i));
                                        }

                                    }
                                } catch (ConnectException e) {
                                    System.out.println("ConnectException 1");

                                }

                            }
                        } catch (ConnectException e) {
                            /**lancio l'eccezione se il nodo ha chiuso la serversocket**/
                            System.out.println("ConnectException 2");


                            try {
                                socketMSGManager = new Socket("localhost", 5555);
                                String tipoClosed = null;
                                if (p == 1111) tipoClosed = "temperatura";
                                if (p == 2222) tipoClosed = "luminosita";
                                if (p == 3333) tipoClosed = "pir1";
                                if (p == 4444) tipoClosed = "pir2";
                                outToMSGManager = new DataOutputStream(socketMSGManager.getOutputStream());
                                outToMSGManager.writeBytes("Il nodo " + tipoClosed + Messaggi.NODO_STOPPATO);
                                Nodo.updateBattery(Messaggi.TRASMISSIONE_GESTORE);
                                iter.remove();
                                socketMSGManager.close();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

                ArrayList<Misurazione> listaSink = (ArrayList<Misurazione>) Nodo.getBuffer().leggi();
                //se la lista è vuota mi metto in attesa di misurazioni
                if (listaSink == null) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                for (int i = 0; i < listaSink.size() - 1; i++) {
                    listeMisurazioni.add(listaSink.get(i));
                }
                String all = gson.toJson(listeMisurazioni);

                try {
                    Socket socketMISManager = new Socket("localhost", 6666);
                    DataOutputStream outToMISManager = new DataOutputStream(socketMISManager.getOutputStream());
                    outToMISManager.writeBytes(all + '\n');
                    Nodo.updateBattery(Messaggi.TRASMISSIONE_GESTORE);
                    socketMISManager.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } /***********************************************************************/
            else {

                /**************elezione*******************/
                Iterator<Integer> iter = porte.iterator();
                System.out.println("Ho indetto un'elezione");
                Map<Integer, Integer> map = new HashMap<>();
                while (iter.hasNext()) {
                    int p = iter.next();
                    if (p != porta) {
                        try {
                            //creo una socket ancora disconnessa
                            Socket socket = SocketFactory.getDefault().createSocket("localhost", p);
                            if (socket.isConnected()) {
                                try {
                                    PrintWriter checkOut = new PrintWriter(socket.getOutputStream(), true);
                                    if (checkOut.checkError()) {
                                        System.out.println("Errore in checkError");
                                    } else {
                                        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                                        out.writeBytes(Messaggi.ELEZIONE + '\n');
                                        Nodo.updateBattery(Messaggi.TRASMISSIONE_NODO);
                                        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                                        int res = br.read();
                                        map.put(p, res);
                                    }
                                } catch (ConnectException e) {
                                    System.out.println("ConnectException 1");


                                }
                            }
                        } catch (ConnectException e) {
                            /**lancio l'eccezione se il nodo ha chiuso la serversocket**/
                            System.out.println("ConnectException 2");

                            try {
                                socketMSGManager = new Socket("localhost", 5555);
                                outToMSGManager = new DataOutputStream(socketMSGManager.getOutputStream());
                                String tipoClosed = null;
                                if (p == 1111) tipoClosed = "temperatura";
                                if (p == 2222) tipoClosed = "luminosita";
                                if (p == 3333) tipoClosed = "pir1";
                                if (p == 4444) tipoClosed = "pir2";
                                outToMSGManager.writeBytes("Il nodo " + tipoClosed + Messaggi.NODO_STOPPATO);
                                Nodo.updateBattery(Messaggi.TRASMISSIONE_GESTORE);
                                iter.remove();
                                socketMSGManager.close();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }

                int max = 0;
                int portToSendMessage = 0;
                for (int port : map.keySet()) {
                    int battery = map.get(port);
                    if (battery > max) {
                        max = battery;
                        portToSendMessage = port;
                    }
                }

                System.out.println("Livello batteria piu' carica': " + max);
                System.out.println("La porta corrispondente e': " + portToSendMessage);
                DataOutputStream outToMax;
                if (max > 25) {
                    try {
                        socketNewSink = new Socket("localhost", portToSendMessage);
                        outToMax = new DataOutputStream(socketNewSink.getOutputStream());
                        outToMax.writeBytes(Messaggi.ELETTO + '\n');
                        Nodo.updateBattery(Messaggi.TRASMISSIONE_NODO);
                        System.out.println("Ho eletto il nodo con la porta " + portToSendMessage + " come nuovo sink");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    stopRequest();
                    new Thread(new NotSinkThread(porta, frequenza)).start();

                } else {
                    System.out.println(Messaggi.ELEZIONE_FALLITA);

                    try {
                        if (max > SingletonBattery.getInstance().getLevel()) {
                            outToMax = new DataOutputStream(socketNewSink.getOutputStream());
                            outToMax.writeBytes(Messaggi.NOTIFICA_GESTORE);
                            Nodo.updateBattery(Messaggi.TRASMISSIONE_NODO);
                        } else {
                            socketMSGManager = new Socket("localhost", 5555);
                            outToMSGManager = new DataOutputStream(socketMSGManager.getOutputStream());
                            outToMSGManager.writeBytes(Messaggi.RETE_NON_DISPONIBILE);
                                    socketMSGManager.close();
                            Nodo.updateBattery(Messaggi.TRASMISSIONE_GESTORE);

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    stopRequest();
                }
            }
        }
    }


    public void stopRequest() {
        stop = true;
    }
}
