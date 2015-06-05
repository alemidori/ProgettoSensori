package reteSensori.simulatori;

/**
 * Created by civi on 21/04/15.
 */
public class Misurazione implements Comparable<Misurazione> {

    private String type;
    private String value;
    private long timestamp;

    public Misurazione(String type, String value, long timestamp) {
        this.value = value;
        this.timestamp = timestamp;
        this.type=type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int compareTo(Misurazione m) {
        Long thisTimestamp = timestamp;
        Long otherTimestamp = m.getTimestamp();
        return thisTimestamp.compareTo(otherTimestamp);
    }
}
