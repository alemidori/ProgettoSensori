package client;


import client.thread.LoginThread;

/**
 * Created by Alessandra on 16/06/15.
 */
public class Client {
    public static void main(String args[]) {
        new Thread(new LoginThread()).start();
    }
}
