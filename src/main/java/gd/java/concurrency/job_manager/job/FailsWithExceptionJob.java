package gd.java.concurrency.job_manager.job;

import gd.java.concurrency.job_manager.Job;
import gd.java.concurrency.job_manager.Statuses;
import gd.java.concurrency.job_manager.Types;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class FailsWithExceptionJob extends Job {

    public FailsWithExceptionJob(int id) {
        super(id, Statuses.SCHEDULED, Types.WITH_EXCEPTION);
    }

    public Callable getTask(){
        Callable task = () -> {
            try {
                while(true){
                    TimeUnit.SECONDS.sleep(5);
                    System.out.println("FailsWithExceptionJob with id: " + this.getId() + " is running");
                    Thread.currentThread().interrupt();
//                    throw new InterruptedException("FailsWithExceptionJob is interrupted");
                }
            }
            catch (InterruptedException e) {
                System.out.println(e);
                throw new IllegalStateException("InfinityJob interrupted", e);
            }
        };
        return task;
    }
}
