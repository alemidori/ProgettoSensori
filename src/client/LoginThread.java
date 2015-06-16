package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.URL;
import java.util.Objects;
import java.util.Scanner;

/**
 * Created by Alessandra on 16/06/15.
 */
public class LoginThread implements Runnable {
    private ServerSocket serverSocket;
    private boolean flagOK;

    public LoginThread(ServerSocket ss){
        serverSocket = ss;
    }

    @Override
    public void run() {
        do {
            try {
                System.out.println("Inserisci nome utente:");
                Scanner scannerTipo = new Scanner(System.in);
                String utente = scannerTipo.nextLine();
                URL url = new URL("http://localhost:8080/login/"+ utente);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                System.out.println(conn.getResponseCode());
                if (conn.getResponseCode() == 409) {
                    System.out.println("Errore: nome utente non disponibile.");
                    flagOK = false;
                } else if(conn.getResponseCode()== 201) {
                    flagOK = true;
                    System.out.println("Login effettuato con successo.");
                    new Thread(new ClientThreadForMessage(serverSocket)).start();
                    new Thread(new OperazioniClient(serverSocket)).start();
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
