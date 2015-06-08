package gestore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Alessandra on 07/06/15.
 */
public class ReadMisThread implements Runnable {

    @Override
    public void run() {

        try {
            ServerSocket serverSocket = new ServerSocket(5555);
            while(true) {
                Socket socket = serverSocket.accept();
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                System.out.println("Nuove misurazioni: \n"+ br.readLine());
                System.out.println("***************************************");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
