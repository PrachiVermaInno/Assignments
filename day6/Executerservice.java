import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

public class Executerservice {

    // Callable task to count lines in a single file
    static class LineCountTask implements Callable<Integer> {
        private final Path filePath;

        public LineCountTask(Path filePath) {
            this.filePath = filePath;
        }

        @Override
        public Integer call() throws Exception {
            int lines = 0;
            try (BufferedReader reader = Files.newBufferedReader(filePath)) {
                while (reader.readLine() != null) {
                    lines++;
                }
            } catch (IOException e) {
                System.err.println("Error reading file: " + filePath + " -> " + e.getMessage());
            }
            return lines;
        }
    }

    public static void main(String[] args) {
        // Directory containing text files
        String dirPath = "C:\\Users\\Dell\\Documents\\Custom Office Templates"; // replace with your path
        Path directory = Paths.get(dirPath);

        // Create a fixed thread pool
        ExecutorService executor = Executors.newFixedThreadPool(4); // you can adjust pool size

        try {
            // List all text files in the directory
            List<Path> textFiles = Files.list(directory)
                    .filter(path -> path.toString().endsWith(".txt"))
                    .toList();

            List<Future<Integer>> futures = new ArrayList<>();

            // Submit a LineCountTask for each file
            for (Path file : textFiles) {
                futures.add(executor.submit(new LineCountTask(file)));
            }

            // Collect results
            int totalLines = 0;
            for (Future<Integer> future : futures) {
                try {
                    totalLines += future.get(); // get() waits for the task to complete
                } catch (InterruptedException | ExecutionException e) {
                    System.err.println("Error getting result: " + e.getMessage());
                }
            }

            System.out.println("Total number of lines across all files: " + totalLines);

        } catch (IOException e) {
            System.err.println("Error accessing directory: " + e.getMessage());
        } finally {
            // Shutdown the thread pool
            executor.shutdown();
        }
    }
}
