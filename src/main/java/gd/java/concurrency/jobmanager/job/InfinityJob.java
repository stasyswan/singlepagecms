package gd.java.concurrency.jobmanager.job;

import gd.java.concurrency.jobmanager.Job;
import gd.java.concurrency.jobmanager.Status;
import gd.java.concurrency.jobmanager.Type;

import static gd.java.concurrency.jobmanager.JobManager.jobThreads;

public class InfinityJob extends Job {

    public InfinityJob(int id) {
        super(id, Status.SCHEDULED, Type.INFINITY);
    }

    public Runnable getTask(){
        Runnable task = () -> {
            this.changeStatus(Status.RUNNING);
            jobThreads.put(this.getId(), Thread.currentThread());
            while(!Thread.currentThread().isInterrupted()){
                Thread.yield();
            }
            if(Thread.currentThread().isInterrupted()) {
                this.changeStatus(Status.STOPPED);
            }
            else
            {
                this.changeStatus(Status.FINISHED);
            }
            return;
        };
        return task;
    }
}
