package reteSensori.simulatori;

import reteSensori.classi.Nodo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alessandra on 17/05/15.
 */
public class BufferMisurazioni<M> implements Buffer {

    private List<Misurazione> misurazioni;
    private List<Misurazione> list;
    int i;

    public BufferMisurazioni() {
        misurazioni = new ArrayList<>(10);
        list = new ArrayList<>();
        i = 0;
    }

    @Override
    public synchronized void aggiungi(Object o) {

        Nodo.updateBattery("lettura");

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

    }


    @Override
    public synchronized List leggi() {
        List <?> toReturn = new ArrayList<>(misurazioni);

        misurazioni.clear();

        i = 0;

        return toReturn;

    }
}
