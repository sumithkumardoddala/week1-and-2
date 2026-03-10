import java.util.*;

public class ParkingLot {

    class ParkingSpot {
        String licensePlate;
        long entryTime;
        boolean occupied;

        ParkingSpot() {
            occupied = false;
        }
    }

    ParkingSpot[] table;
    int capacity = 500;
    int occupiedSpots = 0;
    int totalProbes = 0;
    int parkOperations = 0;

    public ParkingLot() {
        table = new ParkingSpot[capacity];
        for (int i = 0; i < capacity; i++) {
            table[i] = new ParkingSpot();
        }
    }

    // Hash function
    private int hash(String licensePlate) {
        return Math.abs(licensePlate.hashCode()) % capacity;
    }

    // Park vehicle using linear probing
    public void parkVehicle(String licensePlate) {

        int index = hash(licensePlate);
        int probes = 0;

        while (table[index].occupied) {
            index = (index + 1) % capacity;
            probes++;
        }

        table[index].licensePlate = licensePlate;
        table[index].entryTime = System.currentTimeMillis();
        table[index].occupied = true;

        occupiedSpots++;
        totalProbes += probes;
        parkOperations++;

        System.out.println("Vehicle " + licensePlate +
                " parked at spot #" + index +
                " (" + probes + " probes)");
    }

    // Exit vehicle
    public void exitVehicle(String licensePlate) {

        int index = hash(licensePlate);

        while (table[index].occupied) {

            if (table[index].licensePlate.equals(licensePlate)) {

                long duration = System.currentTimeMillis() - table[index].entryTime;

                double hours = duration / (1000.0 * 60 * 60);

                double fee = hours * 5; // $5 per hour

                table[index].occupied = false;
                occupiedSpots--;

                System.out.printf("Spot #%d freed, Duration: %.2f hours, Fee: $%.2f\n",
                        index, hours, fee);

                return;
            }

            index = (index + 1) % capacity;
        }

        System.out.println("Vehicle not found.");
    }

    // Find nearest available spot
    public void findNearestSpot() {

        for (int i = 0; i < capacity; i++) {
            if (!table[i].occupied) {
                System.out.println("Nearest available spot: #" + i);
                return;
            }
        }

        System.out.println("Parking full.");
    }

    // Parking statistics
    public void getStatistics() {

        double occupancy = (occupiedSpots * 100.0) / capacity;
        double avgProbes = parkOperations == 0 ? 0 : (double) totalProbes / parkOperations;

        System.out.println("Occupancy: " + occupancy + "%");
        System.out.println("Average Probes: " + avgProbes);
        System.out.println("Peak Hour: 2-3 PM (example)");
    }


    public static void main(String[] args) {

        ParkingLot lot = new ParkingLot();

        lot.parkVehicle("ABC-1234");
        lot.parkVehicle("ABC-1235");
        lot.parkVehicle("XYZ-9999");

        lot.findNearestSpot();

        lot.exitVehicle("ABC-1234");

        lot.getStatistics();
    }
}
