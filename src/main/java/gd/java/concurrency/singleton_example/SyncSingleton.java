package gd.java.concurrency.singleton_example;

import com.google.common.base.Stopwatch;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

class SyncSingleton {
    private static SyncSingleton instance;

    private SyncSingleton(){}

    static synchronized SyncSingleton getInstance(){
        if(instance == null){
            synchronized (SyncSingleton.class) {
                if(instance == null){
                    instance = new SyncSingleton();
                }
            }
        }
        return instance;
    }
}

class MainSyncSingleton {
    public static void main(String[] args) {
        Stopwatch stopwatch = Stopwatch.createStarted();

        SyncSingleton sOne = SyncSingleton.getInstance();
        SyncSingleton sTwo = SyncSingleton.getInstance();

        System.out.println("Comparing sOne and sTwo: equals = " + sOne.equals(sTwo));

        // Trying threads

        Callable task = () -> {
            try {
                MILLISECONDS.sleep(15);
                return SyncSingleton.getInstance();
            }
            catch (InterruptedException e) {
                throw new IllegalStateException("taskSOne interrupted", e);
            }
        };

        ExecutorService executor = Executors.newFixedThreadPool(3);

        boolean result = false;
        boolean result1 = false;
        boolean result2 = false;

        Future<SyncSingleton> future = null;
        Future<SyncSingleton> future1 = null;
        Future<SyncSingleton> future2 = null;

        try {
            future = executor.submit(task);

            MILLISECONDS.sleep(10);

            future1 = executor.submit(task);

            MILLISECONDS.sleep(20);

            future2 = executor.submit(task);

            System.out.println("futures done? " + (future.isDone() && future1.isDone() && future2.isDone()));


            result = future.get().getInstance().equals(future1.get().getInstance());
            result1 = future.get().getInstance().equals(future2.get().getInstance());
            result2 = future1.get().getInstance().equals(future2.get().getInstance());

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("futures done? " + (future.isDone() && future1.isDone() && future2.isDone()));
        System.out.println("result: " + (result && result1 && result2));

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

        stopwatch.stop();
        System.out.println("Time elapsed is "+ stopwatch.elapsed(MILLISECONDS));
    }
}