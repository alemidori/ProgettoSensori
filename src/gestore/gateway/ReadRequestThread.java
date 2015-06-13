package gestore.gateway;

import reteSensori.simulatori.Misurazione;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Alessandra on 11/06/15.
 */
public class ReadRequestThread implements Runnable {
    private Gson gson;

    public ReadRequestThread(){
        gson = new Gson();
    }
    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(7777);
            while (true){
                Socket socket= serverSocket.accept();
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                String req = br.readLine();

                System.out.println("Richiesta per Server REST: "+req);
                switch(req){
                    case "recenteTemp":
                        Misurazione temp = ReadMisurazioniThread.getRecente("temperatura");
                        String toSendTemp = gson.toJson(temp);
                        out.writeBytes(toSendTemp+'\n');
                        break;
                    case "recenteLum":
                        Misurazione lum = ReadMisurazioniThread.getRecente("luminosita");
                        String toSendLum = gson.toJson(lum);
                        out.writeBytes(toSendLum + '\n');
                        break;
                    default: System.out.println("tipo non specificato");
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
