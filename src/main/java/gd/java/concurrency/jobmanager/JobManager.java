package gd.java.concurrency.jobmanager;

import gd.java.concurrency.jobmanager.job.*;

import java.util.*;
import java.util.concurrent.*;

class JobManager {
    private ConcurrentMap<Integer, Job> allJobs = new ConcurrentHashMap<>();
    private ConcurrentMap<Integer, Future> jobFutures = new ConcurrentHashMap<>();
    private ExecutorService executor;

    JobManager(int maxThreads) {
        executor = Executors.newFixedThreadPool(maxThreads, new CaughtExceptionsThreadFactory());
    }

    private Job createJob(Type type, int id) {
        Job job = null;
        switch (type) {
            case LONG_RUNNING:
                job = new LongRunningJob(id);
                break;
            case INFINITY:
                job = new InfinityJob(id);
                break;
            case WITH_EXCEPTION:
                job = new FailsWithExceptionJob(id);
                break;
            case INTERRUPTED:
                job = new InterruptedJob(id);
                break;
        }
        return job;
    }

    public void create(HashMap<String, String> params) {
        int count = Integer.parseInt(params.get("number"));
        for (int i = 0; i < count; i++) {
            int id = allJobs.size() + 1;
            Job job = createJob(Type.valueOf(params.get("type")), id);

            allJobs.put(id, job);
            System.out.println("Job id: " + id);
        }
    }

    public void run(HashMap<String, String> param) {
        String type = param.get("type");
        for (Map.Entry<Integer, Job> job : allJobs.entrySet()) {
            Job currentJob = job.getValue();

            if (currentJob.getType().toString().equals(type) && currentJob.getStatus().equals(Status.SCHEDULED)) {
                if (currentJob.getType().equals(Type.WITH_EXCEPTION)) {
                    executor.execute(currentJob);
                } else {
                    jobFutures.put(currentJob.getId(), executor.submit(currentJob));
                }

                System.out.println("Started Job with id: " + currentJob.getId() + " and type: " + currentJob.getType());
            }
        }
    }

    public void start(HashMap<String, String> param) {
        String type = param.get("type");
        int id = allJobs.size() + 1;

        Job job = createJob(Type.valueOf(type), id);
        allJobs.put(id, job);

        if (job.getType().equals(Type.WITH_EXCEPTION)) {
            executor.execute(job);
        } else {
            jobFutures.put(job.getId(), executor.submit(job));
        }

        System.out.println("Job id: " + id);
    }

    public void startall(HashMap<String, String> param) {
        for (Type t : Type.values()) {
            HashMap type = new HashMap();
            type.put("type", t.toString());
            run(type);
        }
    }

    public void stop(HashMap<String, String> params) {
        int id = Integer.parseInt(params.get("id"));
        Future future = jobFutures.get(id);

        if (future == null) {
            System.out.println("There is no running Job with id: " + id);
        } else {
            future.cancel(true);
            while (!future.isDone() && !future.isCancelled()) {
                Thread.yield();
            }
            System.out.println("Cancelled Job with id: " + id);
        }
        printAllJobStatuses();
    }

    public void status(HashMap<String, String> params) {
        int id = Integer.parseInt(params.get("id"));
        Job job = allJobs.get(id);

        if (job == null) {
            System.out.println("There is no Job with id: " + id);
        } else {
            System.out.println("Job id: " + id + " Status: " + job.getStatus());
        }
    }

    public void stopall(HashMap<String, String> param) {
        exit();
    }

    private void printAllJobStatuses() {
        for (Job job : allJobs.values()) {
            System.out.println("Job id: " + job.getId() + " type: " + job.getType() + " status: " + job.getStatus());
        }
    }

    private void exit() {
        System.out.println("attempt to shutdown executor");

        executor.shutdown();
        executor.shutdownNow();

        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.err.println("Error during shutting down an executor: " + e.getMessage());
        }

        System.out.println("shutdown finished");
    }
}
