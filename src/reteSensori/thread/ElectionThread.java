package reteSensori.thread;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by Alessandra on 04/06/15.
 */
public class ElectionThread implements Runnable {
    private int porta;
    private boolean stop = false;
    private Socket socket;
    private String richiesta;

    public ElectionThread(int p) {
        richiesta = "elezione";
        porta = p;
    }


    @Override
    public void run() {
        int[] porte = {1111, 2222, 3333, 4444};
        while (!stop) {
            for (int p : porte) {
                if (p != porta) {
                    try {
                        socket = new Socket("localhost", p);
                        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                        out.writeBytes(richiesta);
                        System.out.println("Ho indetto un'elezione");
                        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String res = br.readLine();
                        System.out.println(res + '\n');


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

        }
    }
}
