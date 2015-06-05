package reteSensori.thread;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Alessandra on 04/06/15.
 */
public class SendToManager implements Runnable {
    private  Socket socket;
    private  String liste;
    private DataOutputStream outToManager;
    public SendToManager(Socket s,String l){
        socket = s;
        liste = l;
        try {
            outToManager = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            outToManager.writeBytes(liste);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
