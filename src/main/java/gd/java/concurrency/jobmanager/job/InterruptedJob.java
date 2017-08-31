package gd.java.concurrency.jobmanager.job;

import gd.java.concurrency.jobmanager.Job;
import gd.java.concurrency.jobmanager.Status;
import gd.java.concurrency.jobmanager.Type;

import java.util.concurrent.TimeUnit;


public class InterruptedJob extends Job {

    public InterruptedJob(int id) {
        super(id, Status.SCHEDULED, Type.INTERRUPTED);
    }

    public void run() {
        this.changeStatus(Status.RUNNING);

        try {
            for (int i = 0; i < 5; i++) TimeUnit.HOURS.sleep(1);
        } catch (InterruptedException e) {
            changeStatus(Status.STOPPED);
            Thread.currentThread().interrupt();
        }

        if (this.getStatus().equals(Status.RUNNING)) changeStatus(Status.FINISHED);
    }
}
