package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Alessandra on 16/06/15.
 */
public class ClientThreadForMessage implements Runnable {
    private ServerSocket serverSocket;

    public ClientThreadForMessage(ServerSocket ss) {
       serverSocket = ss;
    }

    @Override
    public void run() {

        while (true) {
            try {
                Socket socket = serverSocket.accept();
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String messaggio = br.readLine();
                System.out.println("********************************");
                System.out.println("NUOVO MESSAGGIO DAL GESTORE:\n"+messaggio);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
