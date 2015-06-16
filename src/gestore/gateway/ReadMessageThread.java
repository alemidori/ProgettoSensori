package gestore.gateway;

import javax.net.SocketFactory;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Alessandra on 08/06/15.
 */
public class ReadMessageThread implements Runnable {

    @Override
    public void run() {

        try {

            ServerSocket serverSocket = new ServerSocket(6666);

            while (true) {
                Socket socket = serverSocket.accept();
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String messaggio = br.readLine();
                System.out.println("NUOVO MESSAGGIO DALLA RETE: \n" + messaggio);
                System.out.println("***************************************");
                try {
                    Socket socketClient = SocketFactory.getDefault().createSocket("localhost", 8888);
                    if (socketClient.isConnected()) {
                        DataOutputStream out = new DataOutputStream(socketClient.getOutputStream());
                        out.writeBytes(messaggio + '\n');
                        socketClient.close();

                    }
                } catch (ConnectException e) {
                    System.out.println(" ");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
