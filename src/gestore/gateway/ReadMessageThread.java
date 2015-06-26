package gestore.gateway;

import com.google.gson.reflect.TypeToken;
import reteSensori.classi.Messaggi;
import reteSensori.simulatori.Misurazione;

import javax.net.SocketFactory;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;

import com.google.gson.Gson;

/**
 * Created by Alessandra on 08/06/15.
 */
public class ReadMessageThread implements Runnable {
    private Gson gson;


    public ReadMessageThread() {
        gson = new Gson();
    }

    @Override
    public void run() {

        try {

            ServerSocket serverSocket = new ServerSocket(5555);
            ArrayList<String[]> indirizziUtenti = new ArrayList<>();

            while (true) {
                Socket socket = serverSocket.accept();
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String incoming = br.readLine();

                if (incoming.startsWith(Messaggi.NEW_UTENTE)) {
                    String[] parts = incoming.split("-");
                    String[] address = {parts[1], parts[2]};
                    indirizziUtenti.add(address);
                } else if (incoming.startsWith(Messaggi.USER_REQUEST)) {
                    //splitto tutto secondo "-" e avrò che:
                    //il primo elemento[0] sarà il tipo di messaggio cioè user, il secondo[0] il tipo di richiesta
                    //a seconda se la richiesta è una media avrò anche il terzo[2] e quarto[3] elemento
                    //rispettivamente tempo inizio e tempo fine
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                    String[] parts = incoming.split("-");
                    String req = parts[1];
                    System.out.println("Richiesta per Server REST: " + req);
                    switch (req) {
                        case "recenteTemp":
                            String temp = ReadMisurazioniThread.getRecente("temperatura");
                            String toSendTemp = gson.toJson(temp);
                            out.writeBytes(toSendTemp + '\n');
                            socket.close();
                            break;
                        case "recenteLum":
                            String lum = ReadMisurazioniThread.getRecente("luminosita");
                            String toSendLum = gson.toJson(lum);
                            out.writeBytes(toSendLum + '\n');
                            socket.close();
                            break;
                        case "mediaTemp":
                            String mediaTemp = ReadMisurazioniThread.getMedia("temperatura", parts[2], parts[3]);
                            out.writeBytes(mediaTemp + '\n');
                            socket.close();
                            break;
                        case "mediaLum":
                            String mediaLum = ReadMisurazioniThread.getMedia("luminosita", parts[2], parts[3]);
                            out.writeBytes(mediaLum + '\n');
                            socket.close();
                            break;
                        case "minMaxTemp":
                            String minMaxTemp = ReadMisurazioniThread.getMinMax("temperatura", parts[2], parts[3]);
                            out.writeBytes(minMaxTemp + '\n');
                            socket.close();
                            break;
                        case "minMaxLum":
                            String minMaxLum = ReadMisurazioniThread.getMinMax("luminosita", parts[2], parts[3]);
                            out.writeBytes(minMaxLum + '\n');
                            socket.close();
                            break;
                        case "presPir1":
                            String presPir1 = ReadMisurazioniThread.getPresenza("pir1", parts[2], parts[3]);
                            out.writeBytes(presPir1 + '\n');
                            socket.close();
                            break;
                        case "presPir2":
                            String presPir2 = ReadMisurazioniThread.getPresenza("pir2", parts[2], parts[3]);
                            out.writeBytes(presPir2 + '\n');
                            socket.close();
                            break;
                        case "mediaPres":
                            String mediaPres = ReadMisurazioniThread.getMediaPres(parts[2], parts[3]);
                            out.writeBytes(mediaPres + '\n');
                            socket.close();
                            break;
                        default:
                            out.writeBytes("Tipo sconosciuto.\n");
                            socket.close();
                            break;
                    }
                } else {

                    System.out.println("NUOVO MESSAGGIO DALLA RETE: \n" + incoming);
                    System.out.println("***************************************");
                    if (!indirizziUtenti.isEmpty()) {
                        for (String[] i : indirizziUtenti) {
                            try {
                                Socket newUser = new Socket(i[0], Integer.parseInt(i[1]));
                                DataOutputStream out = new DataOutputStream(newUser.getOutputStream());
                                out.writeBytes(incoming + '\n');
                                newUser.close();
                            } catch (ConnectException e) {
                                System.out.println(" ");
                            }
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
