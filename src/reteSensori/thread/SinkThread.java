package reteSensori.thread;

import com.google.gson.Gson;

import reteSensori.classi.Nodo;
import reteSensori.simulatori.Misurazione;


import javax.net.SocketFactory;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.*;

import reteSensori.singleton.SingletonBattery;

/**
 * Created by Alessandra on 03/06/15.
 */
public class SinkThread implements Runnable {
    private int porta;
    private int frequenza;
    private boolean stop = false;
    private Socket socketMSGManager;
    private Socket socketMISManager;
    private Gson gson;
    private String richiesta, elezione;
    private Socket socketNewSink;

    public SinkThread(int p, int f) {
        richiesta = "misurazioni";
        elezione = "eletto";
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

            DataOutputStream outToMSGManager;
            if (Nodo.getPercentBattery() > 25.0) {
                /*********************chiedo le misurazioni***********************/
                List<String> listeMisurazioni = new ArrayList<>();
                for (int p : porte) {
                    if (p != porta) {
                        try {
                            //creo una socket ancora disconnessa
                            Socket socket = SocketFactory.getDefault().createSocket("localhost",p);
                            if (socket.isConnected()) {
                                try {
                                    PrintWriter checkOut = new PrintWriter(socket.getOutputStream(), true);
                                    if (checkOut.checkError()) {
                                        System.out.println("Errore in checkError");
                                    } else {

                                        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                                        out.writeBytes(richiesta + '\n');
                                        Nodo.updateBattery("trasmissione");
                                        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                                        System.out.println("Leggo le misurazioni");
                                        String res = br.readLine();
                                        System.out.println(res + '\n');
                                        listeMisurazioni.add(res);


                                    }
                                } catch (ConnectException e) {
                                    System.out.println("ConnectException 1");

                                }

                            }
                        }catch (ConnectException e) {
                            /**lancio l'eccezione se il nodo ha chiuso la serversocket**/
                            System.out.println("ConnectException 2");

                            try {
                                socketMSGManager = new Socket("localhost", 6666);
                                outToMSGManager = new DataOutputStream(socketMSGManager.getOutputStream());
                                outToMSGManager.writeBytes("Il nodo con porta:" + p + " ha chiuso le connessioni");
                                socketMSGManager.close();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

                /*genero la stringa con le mie misurazioni*/
                ArrayList<Misurazione> lista = (ArrayList<Misurazione>) Nodo.getBuffer().leggi();
                String gsonList = gson.toJson(lista);
                listeMisurazioni.add(gsonList);
                String all = gson.toJson(listeMisurazioni);

                try {
                    socketMISManager = new Socket("localhost", 5555);
                    DataOutputStream outToMISManager = new DataOutputStream(socketMISManager.getOutputStream());
                    outToMISManager.writeBytes(all + '\n');
                    socketMISManager.close();
                    Nodo.updateBattery("trasmissioneGestore");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } /***********************************************************************/
            else {

                /**************elezione*******************/
                System.out.println("Ho indetto un'elezione");
                richiesta = "elezione";
                Map<Integer, Integer> map = new HashMap<>();
                for (int p : porte) {
                    if (p != porta) {
                        try {
                            //creo una socket ancora disconnessa
                            Socket socket = SocketFactory.getDefault().createSocket("localhost",p);
                            if (socket.isConnected()) {
                                try {
                                    PrintWriter checkOut = new PrintWriter(socket.getOutputStream(), true);
                                    if (checkOut.checkError()) {
                                        System.out.println("Errore in checkError");
                                    } else {
                                        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                                        out.writeBytes(richiesta + '\n');
                                        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                                        int res = br.read();
                                        map.put(p, res);
                                    }
                                }
                                catch (ConnectException e) {
                                    System.out.println("ConnectException 1");

                                }
                            }
                        }
                        catch (ConnectException e) {
                            /**lancio l'eccezione se il nodo ha chiuso la serversocket**/
                            System.out.println("ConnectException 2");
                            try {
                                socketMSGManager = new Socket("localhost", 6666);
                                outToMSGManager = new DataOutputStream(socketMSGManager.getOutputStream());
                                outToMSGManager.writeBytes("Il nodo con porta:" + p + " ha chiuso le connessioni");
                                socketMSGManager.close();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }

                        }
                        catch (IOException e) {
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
                        outToMax.writeBytes(elezione + '\n');
                        System.out.println("Ho eletto il nodo con la porta " + portToSendMessage + " come nuovo sink");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    stopRequest();
                    new Thread(new NotSinkThread(porta, frequenza)).start();
                } else {
                    System.out.println("Nessun nodo ha la batteria >25%");

                    try {
                        if (max > SingletonBattery.getInstance().getLevel()) {
                            outToMax = new DataOutputStream(socketNewSink.getOutputStream());
                            outToMax.writeBytes("msgManager");
                        } else {
                            socketMSGManager = new Socket("localhost", 6666);
                            outToMSGManager = new DataOutputStream(socketMSGManager.getOutputStream());
                            outToMSGManager.writeBytes("La rete sensori non e' piu' disponibile");
                            socketMSGManager.close();
                            Nodo.updateBattery("trasmissioneGestore");
                            System.out.println("Ho notificato il gestore di rete non disponibile");
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
