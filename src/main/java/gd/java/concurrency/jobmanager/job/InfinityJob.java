package gd.java.concurrency.jobmanager.job;

import gd.java.concurrency.jobmanager.Job;
import gd.java.concurrency.jobmanager.Status;
import gd.java.concurrency.jobmanager.Type;

import static gd.java.concurrency.jobmanager.JobManager.running;

public class InfinityJob extends Job {

    public InfinityJob(int id) {
        super(id, Status.SCHEDULED, Type.INFINITY);
    }

    public Runnable getTask(){
        Runnable task = () -> {
            while(running){
                Thread.yield();
            }
        };
        return task;
    }
}
