package client;

import reteSensori.classi.Messaggi;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.URL;
import java.util.Scanner;
import java.net.Socket;

/**
 * Created by Alessandra on 16/06/15.
 */
public class LoginThread implements Runnable {
    private boolean flagOK;

    public LoginThread(){
    }

    @Override
    public void run() {
        do {
            try {
                System.out.println("Inserisci nome utente:");
                Scanner scanUtente = new Scanner(System.in);
                String utente = scanUtente.nextLine();
                System.out.println("Inserisci indirizzo IP:");
                Scanner scanIP = new Scanner(System.in);
                String ip = scanIP.nextLine();
                System.out.println("Inserisci porta:");
                Scanner scanPorta = new Scanner(System.in);
                String porta = scanPorta.nextLine();

                URL url = new URL("http://localhost:8080/login/"+utente+"/"+ip+"/"+porta);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                System.out.println(conn.getResponseCode());
                if (conn.getResponseCode() == 409) {
                    System.out.println("Errore: nome utente non disponibile.");
                    flagOK = false;
                } else if(conn.getResponseCode()== 201) {
                    flagOK = true;
                    System.out.println("Login effettuato con successo.");
                    DataOutputStream outManager = new DataOutputStream(new Socket("localhost", 5555).getOutputStream());
                    outManager.writeBytes(Messaggi.NEW_UTENTE + "-" +ip+"-"+ porta+'\n');
                    ServerSocket serverSocket = new ServerSocket(Integer.parseInt(porta));
                    new Thread(new ClientThreadForMessage(serverSocket)).start();
                    new Thread(new OperazioniClient()).start();
                }
                else{
                    System.out.println("Non ottengo risposta dal server.");
                }

                conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (!flagOK);

    }
}
