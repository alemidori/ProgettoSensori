package reteSensori.classi;

/**
 * Created by Alessandra on 04/06/15.
 */
public class Messaggio {
    private String tipoMessaggio;
    private String messaggio;

    public Messaggio(String tipomsg) {
        tipoMessaggio = tipomsg;
    }

    public String getTipoMessaggio() {
        return tipoMessaggio;
    }

    public void setMessaggio(String m) {
        messaggio = m;
    }

    public String getMessaggio() {
        return messaggio;
    }

}
