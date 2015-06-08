package gestore;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Alessandra on 08/06/15.
 */
public class InputSwitcherThread implements Runnable {
    private BufferedReader br;
    private Socket socket;

    public InputSwitcherThread(Socket s) {
        socket = s;
        try {
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String res = null;
        try {
            res = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //devo intercettare e tradurre ciò che mi arriva ma non posso usare Object perché troppo generico
//        Object what = gson.fromJson(res, Object.class);
//        if (what instanceof ArrayList) {
//            new Thread(new ReadMisThread(socket)).start();
//        }
    }
}
