package reteSensori;

import reteSensori.classi.*;

/**
 * Created by Alessandra on 29/05/15.
 */
public class MainNodo {

    public static void main(String args[]) throws Exception {

        Nodo nodo = new Nodo(args[0], Integer.parseInt(args[1]), Boolean.parseBoolean(args[2]), Integer.parseInt(args[3]));
        nodo.startNodo();

        //************************************************************




/*TODO:
 Chiamare all'interno dei case, dopo i simulatori, il thread(o classe?) createList passandogli come parametro il buffer,la frequenza del sink,
 introdurre nel thread uno sleep pari alla frequenza. chiamare la funzione per la codifica Json.
 implementare JsonCoding in modo da trasformare la lista di misurazioni ottenuta in una stringa(vedi slide su Json)
Richieste del sink: misurazioni, elezione.
Se il nodo è sink lancia un thread(Request) in cui vengono passate le socket con le porte di tutti gli altri nodi(switch)
e la richiesta(misurazioni o elezione), la prima viene lanciata in questo punto del codice.
nel thread se come parametro di richiesta viene mandato misurazioni i nodi chiamano la classe createList che scrive le misur.
e le restituisce tramite socket. N.B. prova a fondere i thread Request e CreateList perché da un thread non puoi chiamare un altro
 */


    }

    /**
     * ************************ fine main **************************
     */


}
