package gd.java.concurrency.jobmanager.job;

import gd.java.concurrency.jobmanager.Job;
import gd.java.concurrency.jobmanager.Status;
import gd.java.concurrency.jobmanager.Type;

public class LongRunningJob extends Job {

    public LongRunningJob(int id) {
        super(id, Status.SCHEDULED, Type.LONG_RUNNING);
    }

    public Runnable getTask(){
        Runnable task = () -> {
            for(int i = 0; i < 26; i++){
                System.out.println("LongRunningJob" + this.getId() + ": i = " + i);
            }
            this.changeStatus(Status.FINISHED);
        };
        return task;
    }
}
