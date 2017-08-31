package gd.java.concurrency.jobmanager.job;

import gd.java.concurrency.jobmanager.Job;
import gd.java.concurrency.jobmanager.Status;
import gd.java.concurrency.jobmanager.Type;

import java.util.Date;

import static java.util.concurrent.TimeUnit.*;

public class LongRunningJob extends Job {
    public LongRunningJob(int id) {
        super(id, Status.SCHEDULED, Type.LONG_RUNNING);
    }

    public void run() {
        changeStatus(Status.RUNNING);
        loop();

        if (!getStatus().equals(Status.STOPPED)) {
            changeStatus(Status.FINISHED);
        }
    }

    private void loop() {
        long duration = MILLISECONDS.convert(60, MINUTES);
        long start = new Date().getTime();

        while (new Date().getTime() - start < duration) {
            if (Thread.currentThread().isInterrupted()) {
                if (getStatus().equals(Status.RUNNING)) {
                    changeStatus(Status.STOPPED);
                }
                return;
            }
        }
    }
}