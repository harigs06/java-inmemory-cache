package cache;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Cache cache = new LRUCache(2);

        cache.put(1, 100);
        cache.put(2, 200);
        System.out.println(cache.get(1));

        cache.put(3, 300);
        System.out.println(cache.get(2));

        cache.put(4, 400, 1000);
        Thread.sleep(1100);
        System.out.println(cache.get(4));
    }
}

