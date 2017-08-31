package gd.java.concurrency.jobmanager;

public abstract class Job implements Runnable {
    private int id;
    private volatile Status status;
    private Type type;

    public Job(int id, Status status, Type type) {
        this.id = id;
        this.status = status;
        this.type = type;
    }

    public abstract void run();

    protected void changeStatus(Status newStatus) {
        status = newStatus;
    }

    protected Status getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    Type getType() {
        return type;
    }
}
