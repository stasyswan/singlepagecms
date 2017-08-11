package gd.java.concurrency.jobmanager;

import java.util.concurrent.Future;

public abstract class Job {
    private int id;
    private Status status;
    private Type type;
    private Future currentFuture;

    public Job(int id, Status status, Type type) {
        this.id = id;
        this.status = status;
        this.type = type;
    }

    public void changeStatus(Status newStatus){
        status = newStatus;
    }

    public abstract Runnable getTask();

    public Status getStatus(){
        return status;
    }

    public int getId() {
        return id;
    }

    public Type getType() {
        return type;
    }
}
