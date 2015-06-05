package reteSensori.singleton;

/**
 * Created by Alessandra on 04/06/15.
 */
public class SingletonBattery {
    private int maxLevel;
    private int level;
    private static SingletonBattery singletonBattery = null;

    public synchronized static SingletonBattery getInstance() {
        if (singletonBattery == null) singletonBattery = new SingletonBattery();
        return singletonBattery;
    }

    private SingletonBattery() {
        maxLevel = 1000;
    }

    public void setLevel(int l) {
        level = l;
    }
    public int getLevel(){return level;}
    public int getMaxLevel(){return maxLevel;}
}
