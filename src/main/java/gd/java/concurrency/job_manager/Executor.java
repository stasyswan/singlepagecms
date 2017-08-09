package gd.java.concurrency.job_manager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static gd.java.concurrency.job_manager.JobManager.maxThreads;

public class Executor {
    private Executor(){}

    private static class ExecutorHelper{
        private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(maxThreads);
    }

    static ExecutorService getInstance(){
        return ExecutorHelper.EXECUTOR;
    }
}
