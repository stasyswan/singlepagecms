package gd.java.concurrency.job_manager;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Job {
    private int id;
    private Statuses status;
    private Types type;
    private Future currentFuture;

    public Job(int id, Statuses status, Types type) {
        this.id = id;
        this.status = status;
        this.type = type;
    }

    public void changeStatus(Statuses newStatus){
        status = newStatus;
    }

    public void start(Callable task){
        Future future = Executor.getInstance().submit(task);
        System.out.println("Started Job with id: " + this.id);
        this.status = Statuses.RUNNING;
        currentFuture = future;
        if(future.isDone()){
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                this.status = Statuses.FAILED;
                e.printStackTrace();
            }
        }
    }

    void stop(){
        if(currentFuture != null) {
            currentFuture.cancel(true);
            this.status = Statuses.FINISHED;
            System.out.println("Cancelled Job with id: " + this.id);
        }
    }

    public Callable getTask(){
        return () -> 0;
    }

    public Statuses getStatus(){
        return status;
    }

    public int getId() {
        return id;
    }

    public Types getType() {
        return type;
    }
}
