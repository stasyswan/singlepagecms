package gd.java.concurrency.jobmanager.job;

import gd.java.concurrency.jobmanager.Job;
import gd.java.concurrency.jobmanager.Status;
import gd.java.concurrency.jobmanager.Type;

import java.util.concurrent.TimeUnit;

import static gd.java.concurrency.jobmanager.JobManager.jobThreads;


public class InterruptedJob extends Job {

    public InterruptedJob(int id) {
        super(id, Status.SCHEDULED, Type.INTERRUPTED);
    }

    public Runnable getTask(){
        Runnable task = () -> {
            jobThreads.put(this.getId(), Thread.currentThread());
            try {
                for (int i = 0; i < 5; i++) {
                    TimeUnit.HOURS.sleep(1);
                }
            }
            catch (InterruptedException e) {
                System.out.println("Job with id: " + this.getId() + " was interrupted");
                Thread.currentThread().interrupt();
            }
        };
        return task;
    }
}
