package gd.java.concurrency.jobmanager;

public class JobUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        String message = String.format("Thread %s hit by exception %s.",
                t.getName(), e.toString());
        System.out.println(message);
    }
}