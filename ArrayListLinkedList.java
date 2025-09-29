import java.util.*;

public class ListPerformanceComparison{
    
    public static void main(String[] args) {
        int[] sizes = {10_000, 50_000, 100_000};

        for (int size : sizes) {
            System.out.println("\n--- Size: " + size + " ---");

            // ArrayList test
            List<Integer> arrayList = new ArrayList<>();
            long arrayInsertTime = testInsertion(arrayList, size);
            long arrayDeleteTime = testDeletion(arrayList, size);

            System.out.println("ArrayList - Insertion: " + arrayInsertTime + " ms");
            System.out.println("ArrayList - Deletion: " + arrayDeleteTime + " ms");

            // LinkedList test
            List<Integer> linkedList = new LinkedList<>();
            long linkedInsertTime = testInsertion(linkedList, size);
            long linkedDeleteTime = testDeletion(linkedList, size);

            System.out.println("LinkedList - Insertion: " + linkedInsertTime + " ms");
            System.out.println("LinkedList - Deletion: " + linkedDeleteTime + " ms");
        }
    }

    // Method to test insertion at the end
    private static long testInsertion(List<Integer> list, int size) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            list.add(i);  // insertion at the end
        }
        long end = System.currentTimeMillis();
        return end - start;
    }

    // Method to test deletion from the beginning
    private static long testDeletion(List<Integer> list, int size) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            list.remove(0);  // deletion from front
        }
        long end = System.currentTimeMillis();
        return end - start;
    }
}
