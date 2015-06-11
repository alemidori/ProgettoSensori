package gestore.serverREST;

public class User {

    private String ip;
    private String name;

    public User(String ip, String name) {
        this.ip = ip;
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public String getName() {
        return name;
    }
}