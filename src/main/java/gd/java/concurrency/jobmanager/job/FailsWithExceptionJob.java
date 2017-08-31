package gd.java.concurrency.jobmanager.job;

import gd.java.concurrency.jobmanager.Job;
import gd.java.concurrency.jobmanager.Status;
import gd.java.concurrency.jobmanager.Type;

public class FailsWithExceptionJob extends Job {

    public FailsWithExceptionJob(int id) {
        super(id, Status.SCHEDULED, Type.WITH_EXCEPTION);
    }

    public void run() {
        setStatus(Status.RUNNING);

        try {
            throw new RuntimeException();
        } finally {
            setStatus(Status.FAILED);
        }
    }
}