package client;

import java.io.IOException;
import java.net.ServerSocket;


/**
 * Created by Alessandra on 16/06/15.
 */
public class Client {
    public static void main(String args[]) {

        try {
            ServerSocket serverSocket = new ServerSocket(8888);
            new Thread(new LoginThread(serverSocket)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
