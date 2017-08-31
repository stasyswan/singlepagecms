package gd.java.concurrency.jobmanager;

import java.util.concurrent.ThreadFactory;

public class CaughtExceptionsThreadFactory implements ThreadFactory {
    private JobUncaughtExceptionHandler handler = new JobUncaughtExceptionHandler();

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r);
        t.setUncaughtExceptionHandler(handler);
        return t;
    }
}
