package gd.java.concurrency.singleton;

import com.google.common.base.Stopwatch;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

class Singleton {
    private Singleton(){}

    private static class SingletonHelper{
        private static final Singleton INSTANCE = new Singleton();
    }

    static Singleton getInstance(){
        return SingletonHelper.INSTANCE;
    }
}

class MainSingleton {
    public static void main(String[] args) {
        Stopwatch stopwatch = Stopwatch.createStarted();

        Singleton sOne = Singleton.getInstance();
        Singleton sTwo = Singleton.getInstance();

        System.out.println("Comparing sOne and sTwo: equals = " + sOne.equals(sTwo));

        // Trying threads

        Callable task = () -> {
            try {
                TimeUnit.MILLISECONDS.sleep(15);
                return Singleton.getInstance();
            }
            catch (InterruptedException e) {
                throw new IllegalStateException("taskSOne interrupted", e);
            }
        };

        ExecutorService executor = Executors.newFixedThreadPool(3);

        boolean result = false;
        boolean result1 = false;
        boolean result2 = false;

        Future<Singleton> future = null;
        Future<Singleton> future1 = null;
        Future<Singleton> future2 = null;

        try {
            future = executor.submit(task);

            TimeUnit.MILLISECONDS.sleep(10);

            future1 = executor.submit(task);

            TimeUnit.MILLISECONDS.sleep(20);

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