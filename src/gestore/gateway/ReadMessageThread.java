package gestore.gateway;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Alessandra on 08/06/15.
 */
public class ReadMessageThread implements Runnable {

    @Override
    public void run() {

        try {
            ServerSocket serverSocket = new ServerSocket(6666);
            while(true) {
                Socket socket = serverSocket.accept();
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                System.out.println("NUOVO MESSAGGIO DALLA RETE: \n" + br.readLine());
                System.out.println("***************************************");


            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
