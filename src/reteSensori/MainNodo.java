package reteSensori;

import reteSensori.classi.*;

/**
 * Created by Alessandra on 29/05/15.
 */
public class MainNodo {

    public static void main(String args[]) throws Exception {

        Nodo nodo = new Nodo(args[0], Integer.parseInt(args[1]), Boolean.parseBoolean(args[2]), Integer.parseInt(args[3]));
        nodo.startNodo();

    }


}
