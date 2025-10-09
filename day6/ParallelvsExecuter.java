import java.util.concurrent.*;
import java.util.stream.IntStream;

class ParallelvsExecuter {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        int N = 100_000_000; // Large-scale computation
        int numThreads = 4;   // For ExecutorService

        // Sequential execution
        long start = System.nanoTime();
        long seqSum = IntStream.rangeClosed(1, N).asLongStream().map(i -> i * i).sum();
        long end = System.nanoTime();
        System.out.println("Sequential sum: " + seqSum + " | Time: " + (end - start) / 1_000_000 + " ms");

        // Parallel Stream execution
        start = System.nanoTime();
        long parallelSum = IntStream.rangeClosed(1, N).parallel().asLongStream().map(i -> i * i).sum();
        end = System.nanoTime();
        System.out.println("Parallel Stream sum: " + parallelSum + " | Time: " + (end - start) / 1_000_000 + " ms");

        // ExecutorService execution
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        int chunkSize = N / numThreads;
        Callable<Long>[] tasks = new Callable[numThreads];

        for (int i = 0; i < numThreads; i++) {
            final int startRange = i * chunkSize + 1;
            final int endRange = (i == numThreads - 1) ? N : (i + 1) * chunkSize;
            tasks[i] = () -> {
                long sum = 0;
                for (int j = startRange; j <= endRange; j++) sum += (long) j * j;
                return sum;
            };
        }

        start = System.nanoTime();
        long executorSum = 0;
        for (Future<Long> f : executor.invokeAll(java.util.Arrays.asList(tasks))) {
            executorSum += f.get();
        }
        end = System.nanoTime();
        System.out.println("ExecutorService sum: " + executorSum + " | Time: " + (end - start) / 1_000_000 + " ms");

        executor.shutdown();
    }
}
