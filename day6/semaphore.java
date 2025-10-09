import java.util.concurrent.Semaphore;

public class semaphore{

    // ParkingLot class handles the parking logic
    static class ParkingLot {
        private final Semaphore spots; // Semaphore representing parking spots

        public ParkingLot(int numberOfSpots) {
            spots = new Semaphore(numberOfSpots); // Initialize permits = parking spots
        }

        // Method to park a car
        public void parkCar(String carName) {
            try {
                System.out.println(carName + " is trying to park.");

                // Acquire a permit before parking
                spots.acquire();
                System.out.println(carName + " has parked. Spots left: " + spots.availablePermits());

                // Simulate parking duration
                Thread.sleep((long) (Math.random() * 5000)); // Park for 0-5 seconds

                System.out.println(carName + " is leaving.");

                // Release the permit after leaving
                spots.release();
                System.out.println(carName + " left. Spots left: " + spots.availablePermits());

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Car class represents each car as a thread
    static class Car extends Thread {
        private ParkingLot parkingLot;

        public Car(ParkingLot parkingLot, String name) {
            super(name); // Thread name = car name
            this.parkingLot = parkingLot;
        }

        @Override
        public void run() {
            parkingLot.parkCar(getName()); // Each car tries to park
        }
    }

    public static void main(String[] args) {
        int totalSpots = 3; // Total parking spots
        ParkingLot parkingLot = new ParkingLot(totalSpots);

        // Simulate 6 cars trying to park
        for (int i = 1; i <= 6; i++) {
            Car car = new Car(parkingLot, "Car-" + i);
            car.start(); // Start the thread
        }
    }
}
