// ProducerConsumerDemo.java
import java.util.LinkedList;
import java.util.Queue;

class SharedBuffer {
    private Queue<Integer> queue = new LinkedList<>();
    private int capacity; // maximum buffer size

    public SharedBuffer(int capacity) {
        this.capacity = capacity;
    }

    // Producer adds item
    public synchronized void produce(int value) throws InterruptedException {
        // If buffer is full, producer waits
        while (queue.size() == capacity) {
            System.out.println("Buffer is full → Producer waiting...");
            wait(); // release lock and wait
        }

        queue.add(value);
        System.out.println("Produced: " + value + " | Buffer size: " + queue.size());

        // Notify consumer that new item is available
        notifyAll();
    }

    // Consumer removes item
    public synchronized int consume() throws InterruptedException {
        // If buffer is empty, consumer waits
        while (queue.isEmpty()) {
            System.out.println("Buffer is empty → Consumer waiting...");
            wait(); // release lock and wait
        }

        int value = queue.remove();
        System.out.println("Consumed: " + value + " | Buffer size: " + queue.size());

        // Notify producer that space is now available
        notifyAll();
        return value;
    }
}

// Producer Thread
class Producer extends Thread {
    private SharedBuffer buffer;
    private int itemsToProduce;

    public Producer(SharedBuffer buffer, int itemsToProduce) {
        this.buffer = buffer;
        this.itemsToProduce = itemsToProduce;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= itemsToProduce; i++) {
                buffer.produce(i);
                Thread.sleep(500); // simulate production time
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("✅ Producer finished producing " + itemsToProduce + " items.");
    }
}

// Consumer Thread
class Consumer extends Thread {
    private SharedBuffer buffer;
    private int itemsToConsume;

    public Consumer(SharedBuffer buffer, int itemsToConsume) {
        this.buffer = buffer;
        this.itemsToConsume = itemsToConsume;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= itemsToConsume; i++) {
                buffer.consume();
                Thread.sleep(1000); // simulate consumption time
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("✅ Consumer finished consuming " + itemsToConsume + " items.");
    }
}

// Main Class
class ProducerConsumerDemo {
    public static void main(String[] args) {
        SharedBuffer buffer = new SharedBuffer(3); // capacity = 3

        Producer producer = new Producer(buffer, 7);
        Consumer consumer = new Consumer(buffer, 7);

        producer.start();
        consumer.start();
    }
}
