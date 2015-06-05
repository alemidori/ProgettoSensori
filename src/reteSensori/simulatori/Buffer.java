package reteSensori.simulatori;

import java.util.List;

/**
 * Created by civi on 21/04/15.
 */
public interface Buffer<T> {
    public void aggiungi(T t);
    public List<T> leggi();
}
