package cache;

import java.util.PriorityQueue;

class TTLManager {
    private final PriorityQueue<Node> pq = new PriorityQueue<>(
            (a, b) -> Long.compare(a.expiry, b.expiry)
    );

    void add(Node node) {
        if (node.expiry > 0) pq.offer(node);
    }

    Node peek() {
        return pq.peek();
    }

    Node poll() {
        return pq.poll();
    }
}

