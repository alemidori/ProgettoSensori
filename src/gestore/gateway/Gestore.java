package gestore.gateway;


/**
 * Created by Alessandra on 04/06/15.
 */
public class Gestore {

    public static void main(String args[]) throws Exception {
        new Thread(new ReadMisurazioniThread()).start();
        new Thread(new ReadMessageThread()).start();
    }

}
