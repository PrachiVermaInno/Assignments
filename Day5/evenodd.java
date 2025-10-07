import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
class OddEvenPrintMain {
    // Shared state for synchronization
    boolean oddTurn;
    AtomicInteger count = new AtomicInteger(1); // Using AtomicInteger for thread-safe counter
    int MAX;

    // Constructor to initialize the shared state
    public OddEvenPrintMain(boolean startWithOdd, int maxLimit) {
        this.oddTurn = startWithOdd;
        this.MAX = maxLimit;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the maximum number to print: ");
        int maxLimit = scanner.nextInt();
        scanner.close();

        OddEvenPrintMain oep = new OddEvenPrintMain(true, maxLimit); // Start with odd number
        Thread t1 = new Thread(new EvenThread(oep));
        Thread t2 = new Thread(new OddThread(oep));
        t1.start();
        t2.start();
    }
}

class OddThread implements Runnable {
    private OddEvenPrintMain oep;

    public OddThread(OddEvenPrintMain oep) {
        this.oep = oep;
    }

    @Override
    public void run() {
        synchronized (oep) { // Synchronize on the shared OddEvenPrintMain instance
            while (oep.count.get() <= oep.MAX) {
                while (!oep.oddTurn) {
                    try {
                        oep.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // Restore interrupt status
                        e.printStackTrace();
                    }
                }
                if (oep.count.get() <= oep.MAX) { // Check again after waiting
                    System.out.println("Odd Thread :" + oep.count.getAndIncrement());
                    oep.oddTurn = false;
                    oep.notify();
                }
            }
        }
    }
}

class EvenThread implements Runnable {
    private OddEvenPrintMain oep;

    public EvenThread(OddEvenPrintMain oep) {
        this.oep = oep;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(50); // Give OddThread a chance to start first
        } catch (InterruptedException e1) {
            Thread.currentThread().interrupt(); // Restore interrupt status
            e1.printStackTrace();
        }
        synchronized (oep) { // Synchronize on the shared OddEvenPrintMain instance
            while (oep.count.get() <= oep.MAX) {
                while (oep.oddTurn) {
                    try {
                        oep.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // Restore interrupt status
                        e.printStackTrace();
                    }
                }
                if (oep.count.get() <= oep.MAX) { // Check again after waiting
                    System.out.println("Even thread :" + oep.count.getAndIncrement());
                    oep.oddTurn = true;
                    oep.notify();
                }
            }
        }
    }
}
