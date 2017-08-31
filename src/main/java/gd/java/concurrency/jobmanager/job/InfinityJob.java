package gd.java.concurrency.jobmanager.job;

import gd.java.concurrency.jobmanager.Job;
import gd.java.concurrency.jobmanager.Status;
import gd.java.concurrency.jobmanager.Type;

public class InfinityJob extends Job {

    public InfinityJob(int id) {
        super(id, Status.SCHEDULED, Type.INFINITY);
    }

    public void run() {
        changeStatus(Status.RUNNING);

        while (!Thread.currentThread().isInterrupted()) {
            Thread.yield();
        }

        if (Thread.currentThread().isInterrupted()) {
            changeStatus(Status.STOPPED);
        } else {
            changeStatus(Status.FINISHED);
        }
    }
}
