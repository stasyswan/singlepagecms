package gd.java.concurrency.jobmanager.job;

import gd.java.concurrency.jobmanager.Job;
import gd.java.concurrency.jobmanager.Status;
import gd.java.concurrency.jobmanager.Type;

import static gd.java.concurrency.jobmanager.JobManager.jobThreads;

public class FailsWithExceptionJob extends Job {

    public FailsWithExceptionJob(int id) {
        super(id, Status.SCHEDULED, Type.WITH_EXCEPTION);
    }

    public Runnable getTask(){
        Runnable task = () -> {
            this.changeStatus(Status.RUNNING);
            jobThreads.put(this.getId(), Thread.currentThread());
            try {
                throw new RuntimeException();
            }
            catch (RuntimeException e) {
                System.out.println("Job with id: " + this.getId() + " was finished with exception");
            }
            finally {
                this.changeStatus(Status.FINISHED);
            }
        };
        return task;
    }
}