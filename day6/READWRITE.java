import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class READWRITE {

    // Shared configuration variable
    private int configValue = 1;

    // ReadWriteLock
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock readLock = rwLock.readLock();
    private final Lock writeLock = rwLock.writeLock();

    public static void main(String[] args) {
        READWRITE M1= new READWRITE();
        M1.run();
    }

    public void run() {
        // Start reader threads
        for (int i = 0; i < 5; i++) {
            final int readerId = i + 1;
            new Thread(() -> reader(readerId)).start();
        }

        // Start writer thread
        new Thread(this::writer).start();
    }

    // Reader logic
    private void reader(int id) {
        for (int i = 0; i < 4; i++) {
            readLock.lock();
            try {
                System.out.println("Reader " + id + " reads configValue = " + configValue);
                Thread.sleep(100); // simulate reading time
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                readLock.unlock();
            }

            try {
                Thread.sleep(50); // small pause between reads
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // Writer logic
    private void writer() {
        for (int i = 2; i <= 5; i++) {
            writeLock.lock();
            try {
                configValue = i; // update configuration
                System.out.println("Writer updates configValue to " + configValue);
                Thread.sleep(300); // simulate writing time
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                writeLock.unlock();
            }

            try {
                Thread.sleep(200); // wait before next update
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
