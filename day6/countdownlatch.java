import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

public class countdownlatch{

    public static void main(String[] args) throws InterruptedException {
        int numberOfWorkers = 3;
        CountDownLatch latch = new CountDownLatch(numberOfWorkers);

        System.out.println("Main thread: Starting initialization process.");

        // Create and start worker threads
        for (int i = 0; i < numberOfWorkers; i++) {
            Thread worker = new WorkerThread("Worker-" + (i + 1), latch);
            worker.start();
        }

        // Main thread waits for all worker threads to complete their initialization
        latch.await(); // This call blocks until the latch count reaches zero

        System.out.println("Main thread: All worker threads have completed initialization. Continuing with main application logic.");
        // Further main application logic can proceed here
    }

    static class WorkerThread extends Thread {
        private final String name;
        private final CountDownLatch latch;

        public WorkerThread(String name, CountDownLatch latch) {
            this.name = name;
            this.latch = latch;
        }

        @Override
        public void run() {
            try {
                System.out.println(name + ": Starting initialization tasks.");
                // Simulate initialization tasks
                Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 3000)); // Simulate work time
                System.out.println(name + ": Initialization tasks completed.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println(name + ": Interrupted during initialization.");
            } finally {
                latch.countDown(); // Decrement the latch count
            }
        }
    }
}