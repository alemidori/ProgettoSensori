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
        new Thread(new ReadMisThread()).start();
        new Thread(new ReadMsgThread()).start();
    }

}
