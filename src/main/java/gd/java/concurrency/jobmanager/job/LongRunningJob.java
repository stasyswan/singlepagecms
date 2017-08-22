package gd.java.concurrency.jobmanager.job;

import gd.java.concurrency.jobmanager.Job;
import gd.java.concurrency.jobmanager.Status;
import gd.java.concurrency.jobmanager.Type;

import static gd.java.concurrency.jobmanager.JobManager.jobThreads;

public class LongRunningJob extends Job {

    public LongRunningJob(int id) {
        super(id, Status.SCHEDULED, Type.LONG_RUNNING);
    }

    public Runnable getTask(){
        Runnable task = () -> {
            this.changeStatus(Status.RUNNING);
            jobThreads.put(this.getId(), Thread.currentThread());
            loop();
            if(!this.getStatus().equals(Status.STOPPED)) {
                this.changeStatus(Status.FINISHED);
            }
        };
        return task;
    }

    private void loop() {
        for(int i = 0; i < 260000; i++) {
            if(Thread.currentThread().isInterrupted()) {
                if(this.getStatus().equals(Status.RUNNING)) {
                    this.changeStatus(Status.STOPPED);
                }
                return;
            }
            for (int j = 260000; j > 0; j--) {
                if(Thread.currentThread().isInterrupted()){
                    if(this.getStatus().equals(Status.RUNNING)) {
                        this.changeStatus(Status.STOPPED);
                    }
                    return;
                }
                int s = i + j;
                for(int e = 0; e < 260000; e++) {
                    if(Thread.currentThread().isInterrupted()) {
                        if(this.getStatus().equals(Status.RUNNING)) {
                            this.changeStatus(Status.STOPPED);
                        }
                        return;
                    }
                    for (int a = 260000; j > 0; j--) {
                        if(Thread.currentThread().isInterrupted()) {
                            if(this.getStatus().equals(Status.RUNNING)) {
                                this.changeStatus(Status.STOPPED);
                            }
                            return;
                        }
                        int d = i + j;
                        for(int w = 0; w < 260000; w++) {
                            if(Thread.currentThread().isInterrupted()) {
                                if(this.getStatus().equals(Status.RUNNING)) {
                                    this.changeStatus(Status.STOPPED);
                                }
                                return;
                            }
                            for (int f = 260000; f > 0; f--) {
                                if(Thread.currentThread().isInterrupted()) {
                                    if(this.getStatus().equals(Status.RUNNING)) {
                                        this.changeStatus(Status.STOPPED);
                                    }
                                    return;
                                }
                                int h = i + j;
                            }
                        }
                    }
                }
            }
        }
    }

}
