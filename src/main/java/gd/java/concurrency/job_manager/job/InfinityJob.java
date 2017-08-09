package gd.java.concurrency.job_manager.job;

import gd.java.concurrency.job_manager.Job;
import gd.java.concurrency.job_manager.Statuses;
import gd.java.concurrency.job_manager.Types;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class InfinityJob extends Job {

    public InfinityJob(int id) {
        super(id, Statuses.SCHEDULED, Types.INFINITY);
    }

    public Callable getTask(){
        Callable task = () -> {
            try {
                while(true){
                    TimeUnit.SECONDS.sleep(25);
                    System.out.println("InfinityJob with id: " + this.getId() + " is still running");
                }
            }
            catch (InterruptedException e) {
                throw new IllegalStateException("InfinityJob interrupted", e);
            }
        };
        return task;
    }
}
