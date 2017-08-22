package gd.java.concurrency.jobmanager;

import gd.java.concurrency.jobmanager.job.FailsWithExceptionJob;
import gd.java.concurrency.jobmanager.job.InfinityJob;
import gd.java.concurrency.jobmanager.job.InterruptedJob;
import gd.java.concurrency.jobmanager.job.LongRunningJob;

import java.util.*;
import java.util.concurrent.*;

public class JobManager {
    private ConcurrentMap<Integer, Job> allJobs = new ConcurrentHashMap<>();
    private ConcurrentMap<Integer, Future> jobFuTures = new ConcurrentHashMap<>();
    public static ConcurrentMap<Integer, Thread> jobThreads = new ConcurrentHashMap<>();

    Job jobFactory(Type type, int id){
        Job job = null;
        switch (type){
            case LONG_RUNNING :
                job = new LongRunningJob(id);
                break;
            case INFINITY :
                job = new InfinityJob(id);
                break;
            case WITH_EXCEPTION :
                job = new FailsWithExceptionJob(id);
                break;
            case INTERRUPTED :
                job = new InterruptedJob(id);
                break;
        }
        return job;
    }

    public void createJob(String type, int count){
        for (int i = 0; i <count ; i++) {
            int id = allJobs.size() + 1;
            Job job = jobFactory(Type.valueOf(type), id);

            allJobs.put(id, job);
            System.out.println("Job id: " + id);
        }
    }

    public void runJob(ExecutorService executor, String type){
        for(Map.Entry<Integer, Job> job : allJobs.entrySet()){
            Job currentJob = job.getValue();
            if(currentJob.getType().toString().equals(type) && currentJob.getStatus().equals(Status.SCHEDULED)){
                Future future = executor.submit(currentJob.getTask());
                jobFuTures.put(currentJob.getId(), future);
                System.out.println("Started Job with id: " + currentJob.getId() + " and type: " + currentJob.getType());
            }
        }
    }

    public void startJob(ExecutorService executor, String type){
        int id = allJobs.size() + 1;
        Job job = jobFactory(Type.valueOf(type), id);

        allJobs.put(id, job);
        Future future = executor.submit(job.getTask());
        jobFuTures.put(job.getId(), future);
        System.out.println("Job id: " + id);
    }


    public void stopJob(int id) {
        Future future = jobFuTures.get(id);
        if(future == null) {
            System.out.println("There is no running Job with id: " + id);
        }
        else {

            Job job = allJobs.get(id);
            synchronized (job){
                jobThreads.get(id).interrupt();
                System.out.println("Cancelled Job with id: " + id);
                try {
                    job.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        printAllJobStatuses();

    }

    public void printAllJobStatuses(){
        for(Job job : allJobs.values()){
            System.out.println("Job id: " + job.getId() + " type: " + job.getType() + " status: " + job.getStatus());
        }
    }

    public void printJobStatus(int id){
        Job job = allJobs.get(id);
        if(job == null){
            System.out.println("There is no Job with id: " + id);
        }
        else{
            System.out.println("Status: " + job.getStatus());
        }
    }

    public void exit(ExecutorService executor){
        try {
            for(Thread thread : jobThreads.values()) thread.interrupt();
            System.out.println("attempt to shutdown executor");
            executor.awaitTermination(5, TimeUnit.SECONDS);
            executor.shutdown();

            for(Job job : allJobs.values()){
                System.out.println("Job id: " + job.getId() + " type: " + job.getType() + " status: " + job.getStatus());
            }
        }
        catch (InterruptedException e) {
            System.err.println("tasks interrupted");
        }
        finally {
            if (!executor.isTerminated()) {
                System.err.println("cancel non-finished tasks");
                executor.shutdownNow();
            }
            System.out.println("shutdown finished");
        }
    }
}
