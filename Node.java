package cache;

class Node {
    int key;
    int value;
    long expiry;
    Node prev;
    Node next;

    Node(int key, int value, long expiry) {
        this.key = key;
        this.value = value;
        this.expiry = expiry;
    }
}

