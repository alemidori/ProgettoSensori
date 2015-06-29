package client.thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Alessandra on 16/06/15.
 */
public class ClientThreadForMessage implements Runnable {
    private ServerSocket serverSocket;
    private int flag;
    private static boolean stop;
    ArrayList<String> messaggi = new ArrayList<>();

    public ClientThreadForMessage(int p) {
        stop = false;
        try {
            serverSocket = new ServerSocket(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
        flag = 0;
    }

    @Override
    public void run() {

        while (!stop) {
            try {
                Socket socket = serverSocket.accept();
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String messaggio = br.readLine();
                if(Objects.equals(messaggio, "logout")){
                    System.out.println("Logout eseguito con successo.");
                    stopListening();
                }
                else {
                    flag = 0;
                    if (messaggi.isEmpty()) {
                        System.out.println("NUOVO MESSAGGIO DAL GESTORE:\n" + messaggio);
                        System.out.println("********************************");
                        messaggi.add(messaggio);
                    } else {
                        for (String s : messaggi) {
                            if (Objects.equals(s, messaggio)) {
                                flag = 1;
                            }
                        }
                        if (flag == 0) {
                            System.out.println("NUOVO MESSAGGIO DAL GESTORE:\n" + messaggio);
                            System.out.println("********************************");
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            serverSocket.close();
            System.out.println("Connessioni chiuse");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void stopListening() {
        stop = true;
    }

}
