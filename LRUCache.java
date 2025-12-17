package cache;

import java.util.HashMap;
import java.util.Map;

public class LRUCache implements cache.Cache {

    private final int capacity;
    private int size;
    private final Map<Integer, cache.Node> map;
    private final Node head;
    private final Node tail;
    private final TTLManager ttlManager;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.map = new HashMap<>();
        this.ttlManager = new TTLManager();
        this.head = new Node(-1, -1, -1);
        this.tail = new Node(-1, -1, -1);
        head.next = tail;
        tail.prev = head;
    }

    @Override
    public int get(int key) {
        cleanupExpired();
        Node node = map.get(key);
        if (node == null) return -1;
        moveToFront(node);
        return node.value;
    }

    @Override
    public void put(int key, int value) {
        put(key, value, -1);
    }

    @Override
    public void put(int key, int value, long ttlMillis) {
        cleanupExpired();

        if (map.containsKey(key)) {
            Node node = map.get(key);
            node.value = value;
            node.expiry = ttlMillis > 0 ? System.currentTimeMillis() + ttlMillis : -1;
            moveToFront(node);
            ttlManager.add(node);
            return;
        }

        if (size == capacity) {
            Node lru = tail.prev;
            removeNode(lru);
            map.remove(lru.key);
            size--;
        }

        long expiry = ttlMillis > 0 ? System.currentTimeMillis() + ttlMillis : -1;
        Node node = new Node(key, value, expiry);
        map.put(key, node);
        addAfterHead(node);
        ttlManager.add(node);
        size++;
    }

    private void cleanupExpired() {
        long now = System.currentTimeMillis();
        while (ttlManager.peek() != null && ttlManager.peek().expiry <= now) {
            Node node = ttlManager.poll();
            if (map.containsKey(node.key)) {
                removeNode(node);
                map.remove(node.key);
                size--;
            }
        }
    }

    private void moveToFront(Node node) {
        removeNode(node);
        addAfterHead(node);
    }

    private void addAfterHead(Node node) {
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }

    private void removeNode(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }
}

