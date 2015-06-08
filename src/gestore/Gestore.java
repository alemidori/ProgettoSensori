package gestore;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


/**
 * Created by Alessandra on 04/06/15.
 */
public class Gestore {
    public static void main(String args[]) throws Exception {
        ServerSocket serverSocket = new ServerSocket(5555);


        while (true) {
            Socket socket = serverSocket.accept();
            new Thread(new InputSwitcherThread(socket)).start();
//            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            String res = br.readLine();
//            Object what = gson.fromJson(res, Object.class);
//            if(what instanceof ArrayList){
//                new Thread(new ReadMisThread(socket)).start();
//            }
//            else{
//                new Thread(new ReadMsgThread(socket)).start();
//            }


        }

    }

}
