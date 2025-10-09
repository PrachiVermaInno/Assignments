import java.io.*;
import java.util.*;
import java.util.concurrent.*;
class Task4 {

    public static void main(String[] args) {
        // Step 1: Directory containing the files
        String directoryPath = System.getProperty("user.home") + File.separator + "Documents";
        File folder = new File(directoryPath);

        // Step 2: Filter only text files (.txt)
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));
        if (files == null || files.length == 0) {
            System.out.println("No text files found in the directory.");
            return;
        }

        // Step 3: Create a fixed thread pool
        ExecutorService executor = Executors.newFixedThreadPool(4); // 4 threads

        // Step 4: Prepare Callable tasks
        List<Callable<Integer>> tasks = new ArrayList<>();
        for (File file : files) {
            tasks.add(() -> {
                int lines = 0;
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    while (br.readLine() != null) {
                        lines++;
                    }
                } catch (IOException e) {
                    System.err.println("Error reading file: " + file.getName());
                }
                System.out.println(file.getName() + " has " + lines + " lines.");
                return lines;
            });
        }

        // Step 5: Submit tasks and collect results
        try {
            List<Future<Integer>> results = executor.invokeAll(tasks);
            int totalLines = 0;
            for (Future<Integer> future : results) {
                totalLines += future.get(); // get() waits for task completion
            }
            System.out.println("Total lines across all files: " + totalLines);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            // Step 6: Shutdown the executor
            executor.shutdown();
        }
    }
}
