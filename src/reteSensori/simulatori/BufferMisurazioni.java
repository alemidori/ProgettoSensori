package reteSensori.simulatori;

import reteSensori.classi.Messaggi;
import reteSensori.classi.Nodo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alessandra on 17/05/15.
 */
public class BufferMisurazioni<M> implements Buffer {

    private List<Misurazione> misurazioni;
    int i;

    public BufferMisurazioni() {
        misurazioni = new ArrayList<>(10);
        i = 0;
    }

    @Override
    public synchronized void aggiungi(Object o) {
        notify();
        if (i < 10) {
            misurazioni.add(i, (Misurazione) o);
            System.out.println("Tipo: " + misurazioni.get(i).getType() + " " + i + " Valore: " + misurazioni.get(i).getValue());

        }
        if (i == 10) {
            i = 0;
            misurazioni.add(i, (Misurazione) o);
            System.out.println("Tipo: " + misurazioni.get(i).getType() + " " + i + " Valore: " + misurazioni.get(i).getValue());
        }
        i++;

        Nodo.updateBattery(Messaggi.LETTURA);
    }


    @Override
    public synchronized List leggi() {
        List <?> toReturn = new ArrayList<>(misurazioni);
        misurazioni.clear();
        i = 0;
        Nodo.updateBattery(Messaggi.LETTURA);
        return toReturn;
    }
}
