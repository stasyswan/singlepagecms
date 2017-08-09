package gd.java.concurrency.job_manager.job;

import gd.java.concurrency.job_manager.Job;
import gd.java.concurrency.job_manager.Statuses;
import gd.java.concurrency.job_manager.Types;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class LongRunningJob extends Job {

    public LongRunningJob(int id) {
        super(id, Statuses.SCHEDULED, Types.LONG_RUNNING);
    }

    public Callable getTask(){
        Callable task = () -> {
            try {
                int i;
                for(i = 0; i < 26; i++){
                    TimeUnit.SECONDS.sleep(15);
                    System.out.println("LongRunningJob" + this.getId() + ": i = " + i);
                }
                return i;
            }
            catch (InterruptedException e) {
                throw new IllegalStateException("LongRunningJob interrupted", e);
            }
        };
        return task;
    }
}
