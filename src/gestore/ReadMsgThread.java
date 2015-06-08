package gestore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by Alessandra on 08/06/15.
 */
public class ReadMsgThread implements Runnable {
    private BufferedReader br;

    public ReadMsgThread(Socket s){

        try {
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        try {
            System.out.println(br.readLine());
            System.out.println("***************************************");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
