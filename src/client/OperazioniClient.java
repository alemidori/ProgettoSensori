package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Scanner;

/**
 * Created by Alessandra on 16/06/15.
 */
public class OperazioniClient implements Runnable {

    public OperazioniClient() {
    }

    @Override
    public void run() {

        System.out.println("OPERAZIONI:\n1. Ottieni la misurazione piu' recente.\n" +
                "2. Ottieni media misurazioni dal tempo t1 al tempo t2.\n" +
                "3. Ottieni minimo e massimo dal tempo t1 al tempo t2.\n" +
                "4. Effettua il logout.\n");
        System.out.println("********************************");
        Scanner oper = new Scanner(System.in);
        int numOper = oper.nextInt();
        if (numOper == 1) {
            try {

                System.out.println("Inserisci tipo per la misurazione piu' recente:");
                Scanner scannerTipo = new Scanner(System.in);
                String tipo = scannerTipo.nextLine();
                URL url = new URL("http://localhost:8080/misurazione/recente/" + tipo);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                if (conn.getResponseCode() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : "
                            + conn.getResponseCode());
                }

                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

                String output;
                while ((output = br.readLine()) != null) {
                    System.out.println(output + '\n');
                }

                conn.disconnect();
                //alla fine dell'operazione faccio una chiamata ricorsiva a run per ridare il controllo all'utente
                this.run();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        //........

        if (numOper == 4) {
            //logout
            try {
                System.out.println("Logout dell'utente: ");
                Scanner scanUtente = new Scanner(System.in);
                String utente = scanUtente.nextLine();
                URL url = new URL("http://localhost:8080/logout/"+utente);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("DELETE");
                System.out.println(conn.getResponseCode());
                new Thread(new LoginThread()).start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }
}
