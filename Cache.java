package cache;

public interface Cache {
    int get(int key);
    void put(int key, int value);
    void put(int key, int value, long ttlMillis);
}

