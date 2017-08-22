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
            this.changeStatus(Status.RUNNING);
            jobThreads.put(this.getId(), Thread.currentThread());
            try {
                for (int i = 0; i < 5; i++) {
                    TimeUnit.HOURS.sleep(1);
                }
            }
            catch (InterruptedException e) {
                synchronized (InterruptedJob.this) {
                    this.changeStatus(Status.STOPPED);
                    notifyAll();
                }
                Thread.currentThread().interrupt();
            }
            if(this.getStatus().equals(Status.RUNNING))this.changeStatus(Status.FINISHED);
        };
        return task;
    }
}
