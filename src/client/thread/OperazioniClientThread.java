package client.thread;

import reteSensori.classi.Messaggi;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Scanner;

/**
 * Created by Alessandra on 16/06/15.
 */
public class OperazioniClientThread implements Runnable {
    private ClientThreadForMessage listener;
private int porta;
    public OperazioniClientThread(int p) {
        listener = new ClientThreadForMessage(p);
        porta=p;
    }

    @Override
    public void run() {

        new Thread(listener).start();
        System.out.println("OPERAZIONI:\n1. Ottieni la misurazione piu' recente.\n" +
                "2. Ottieni media misurazioni in un intervallo di tempo.\n" +
                "3. Ottieni minimo e massimo in un intervallo di tempo.\n" +
                "4. Attivazione sensore presenza in un intervallo di tempo.\n" +
                "5. Media attivazione sensori di presenza in un intervallo di tempo.\n" +
                "6. Effettua il logout.\n");
        System.out.println("********************************");
        Scanner oper = new Scanner(System.in);
        int numOper = oper.nextInt();
        if (numOper == 1) {
            try {

                System.out.println("Misurazione piu' recente.\nInserisci tipo misurazione(temperatura - luminosita):");
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

        if (numOper == 2) {
            System.out.println("Media misurazioni.\nInserisci tipo misurazione(temperatura - luminosita):");
            Scanner scannerTipo = new Scanner(System.in);
            String tipo = scannerTipo.nextLine();
            System.out.println("Inserisci tempo inizio in secondi:");
            Scanner scanTempoInizio = new Scanner(System.in);
            String tempoInizio = scanTempoInizio.nextLine();
            System.out.println("Inserisci tempo fine in secondi:");
            Scanner scanTempoFine = new Scanner(System.in);
            String tempoFine = scanTempoFine.nextLine();
            try {
                URL url = new URL("http://localhost:8080/misurazione/media/" + tipo + "/" + tempoInizio + "/" + tempoFine);
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

        if (numOper == 3) {
            System.out.println("Minimo e massimo.\nInserisci tipo misurazione(temperatura - luminosita):");
            Scanner scannerTipo = new Scanner(System.in);
            String tipo = scannerTipo.nextLine();
            System.out.println("Inserisci tempo inizio in secondi:");
            Scanner scanTempoInizio = new Scanner(System.in);
            String tempoInizio = scanTempoInizio.nextLine();
            System.out.println("Inserisci tempo fine in secondi:");
            Scanner scanTempoFine = new Scanner(System.in);
            String tempoFine = scanTempoFine.nextLine();
            try {
                URL url = new URL("http://localhost:8080/misurazione/min_max/" + tipo + "/" + tempoInizio + "/" + tempoFine);
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

        if (numOper == 4) {
            System.out.println("Rilevazione presenza.\nInserisci zona(per ovest:pir1, per est:pir2):");
            Scanner scannerTipo = new Scanner(System.in);
            String tipo = scannerTipo.nextLine();
            System.out.println("Inserisci tempo inizio in secondi:");
            Scanner scanTempoInizio = new Scanner(System.in);
            String tempoInizio = scanTempoInizio.nextLine();
            System.out.println("Inserisci tempo fine in secondi:");
            Scanner scanTempoFine = new Scanner(System.in);
            String tempoFine = scanTempoFine.nextLine();
            try {
                URL url = new URL("http://localhost:8080/misurazione/presenza/" + tipo + "/" + tempoInizio + "/" + tempoFine);
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

        if (numOper == 5) {
            System.out.println("Media rilevazioni presenza.\nInserisci tempo inizio in secondi:");
            Scanner scanTempoInizio = new Scanner(System.in);
            String tempoInizio = scanTempoInizio.nextLine();
            System.out.println("Inserisci tempo fine in secondi:");
            Scanner scanTempoFine = new Scanner(System.in);
            String tempoFine = scanTempoFine.nextLine();
            try {
                URL url = new URL("http://localhost:8080/misurazione/media_presenza/" + tempoInizio + "/" + tempoFine);
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
        if (numOper == 6) {
            //logout
            try {
                System.out.println("Logout dell'utente: ");
                Scanner scanUtente = new Scanner(System.in);
                String utente = scanUtente.nextLine();
                URL url = new URL("http://localhost:8080/logout/" + utente);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("DELETE");

                if (conn.getResponseCode() == 202) {
                    //listener.stopListening(); //setta stop=true prima che si metta in ascolto

                    Socket toGateway = new Socket("localhost", 5555);
                    DataOutputStream outManager = new DataOutputStream(toGateway.getOutputStream());
                    outManager.writeBytes(Messaggi.USER_REQUEST + "-" + "logout" +"-"+ porta +'\n');

                    System.out.println("Richiesta logout..."+'\n');

                } else {
                    System.out.println("Utente " + utente + " non trovato.");
                    this.run();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }

}
