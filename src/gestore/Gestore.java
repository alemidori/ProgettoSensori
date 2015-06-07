package gestore;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Alessandra on 04/06/15.
 */
public class Gestore {
    public static void main(String args[]) throws Exception {
        ServerSocket serverSocket = new ServerSocket(5555);
        while (true) {
            System.out.println("In ascolto...");
            Socket socket = serverSocket.accept();
            new Thread(new GestoreThread(socket)).start();

        }

    }
}
