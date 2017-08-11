package gd.java.concurrency.pubsub;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {

        // Creating BlockingQueue of size 10
        // BlockingQueue supports operations that wait for the queue to become non-empty when retrieving an element,
        // and wait for space to become available in the queue when storing an element.
        BlockingQueue<Message> queue = new ArrayBlockingQueue<>(10);

        Producer producer = new Producer(queue);
        Consumer consumerOne = new Consumer(queue, "One");
        Consumer consumerTwo = new Consumer(queue, "Two");

        System.out.println("Let's get started. Producer / Consumer Test Started.\n");

        ExecutorService executor = Executors.newFixedThreadPool(3);
        executor.submit(producer::run);
        executor.submit(consumerOne::run);
        executor.submit(consumerTwo::run);

        try {
            System.out.println("attempt to shutdown executor");
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        }
        catch (InterruptedException e) {
            System.err.println("tasks interrupted");
        }
        finally {
            if (!executor.isTerminated()) {
                System.err.println("cancel non-finished tasks");
            }
            executor.shutdownNow();
            System.out.println("shutdown finished");
        }



    }
}
