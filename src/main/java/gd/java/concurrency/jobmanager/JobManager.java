package gd.java.concurrency.jobmanager;

import gd.java.concurrency.jobmanager.job.*;

import java.util.*;
import java.util.concurrent.*;

/**
 * {@link gd.java.concurrency.jobmanager.JobManager} provides API for managing
 * {@link gd.java.concurrency.jobmanager.Job} jobs.
 * Contains methods for running commands for creating, running, starting, stopping jobs
 * and getting their statuses.
 * All jobs run in parallel mode.

 * @author Anastasiia Lebedieva
 */
class JobManager {
    /**
     * Shared empty concurrent map instance used for managing job objects.
     */
    private ConcurrentMap<Integer, Job> allJobs = new ConcurrentHashMap<>();

    /**
     * Shared empty concurrent map instance used for managing job future objects.
     */
    private ConcurrentMap<Integer, Future> jobFutures = new ConcurrentHashMap<>();

    /**
     * Shared executor service for working with job threads.
     */
    private ExecutorService executor;

    /**
     * Constructs an empty job manager with the specified maximum threads count.
     *
     * @param  maxThreads Maximum number of available threads
     */
    JobManager(int maxThreads) {
        executor = Executors.newFixedThreadPool(maxThreads, new CaughtExceptionsThreadFactory());
    }

    /**
     * Creates given number of jobs of given type
     *
     * @param params should contain keys "number" - count of jobs and
     *               "type" - necessary job type
     */
    public void create(HashMap<String, String> params) {
        int count = Integer.parseInt(params.get("number"));
        for (int i = 0; i < count; i++) {
            int id = allJobs.size() + 1;
            Job job = createJob(Type.valueOf(params.get("type")), id);

            allJobs.put(id, job);
            System.out.printf("Job id: %d%n", id);
        }
    }

    /**
     * Runs all jobs of given type
     *
     * @param params should contain key "type" - necessary job type
     */
    public void run(HashMap<String, String> params) {
        String type = params.get("type");
        for (Map.Entry<Integer, Job> job : allJobs.entrySet()) {
            Job currentJob = job.getValue();

            if (currentJob.getType().toString().equals(type)
                    && currentJob.getStatus().equals(Status.SCHEDULED)) {
                if (currentJob.getType().equals(Type.WITH_EXCEPTION)) {
                    executor.execute(currentJob);
                } else {
                    jobFutures.put(currentJob.getId(), executor.submit(currentJob));
                }

                System.out.printf("Started Job with id: %d and type: %s%n",
                        currentJob.getId(),
                        currentJob.getType());
            }
        }
    }

    /**
     * Creates and starts a job of given type
     *
     * @param params should contain key "type" - necessary job type
     */
    public void start(HashMap<String, String> params) {
        String type = params.get("type");
        int id = allJobs.size() + 1;

        Job job = createJob(Type.valueOf(type), id);
        allJobs.put(id, job);

        if (job.getType().equals(Type.WITH_EXCEPTION)) {
            executor.execute(job);
        } else {
            jobFutures.put(job.getId(), executor.submit(job));
        }

        System.out.printf("Job id: %d%n", id);
    }

    /**
     * Runs all jobs
     *
     * @param params is needed for convenient method call
     */
    public void startall(HashMap<String, String> params) {
        startall();
    }

    /**
     * Runs all jobs
     */
    private void startall() {
        for (Type t : Type.values()) {
            HashMap<String, String> type = new HashMap<>();
            type.put("type", t.toString());
            run(type);
        }
    }

    /**
     * Stops a job of given id
     *
     * @param params should contain key "id" - necessary job type
     */
    public void stop(HashMap<String, String> params) {
        int id = Integer.parseInt(params.get("id"));
        Future future = jobFutures.get(id);

        if (future == null) {
            System.out.printf("There is no running Job with id: %d%n", id);
        } else {
            future.cancel(true);
            while (!future.isDone() && !future.isCancelled()) {
                Thread.yield();
            }
            System.out.printf("Cancelled Job with id: %d%n", id);
        }
        printAllJobStatuses();
    }

    /**
     * Prints status of job by given id
     *
     * @param params should contain key "id" - necessary job type
     */
    public void status(HashMap<String, String> params) {
        int id = Integer.parseInt(params.get("id"));
        Job job = allJobs.get(id);

        if (job == null) {
            System.out.printf("There is no Job with id: %d%n", id);
        } else {
            System.out.printf("Job id: %d Status: %s%n", id, job.getStatus());
        }
    }

    /**
     * Stops all jobs
     *
     * @param params is needed for convenient method call
     */
    public void stopall(HashMap<String, String> params) {
        stopall();
    }

    /**
     * Stops all jobs by shutting down executor and terminates application
     */
    private void stopall() {
        System.out.println("attempt to shutdown executor");

        executor.shutdown();
        executor.shutdownNow();

        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.err.printf("Error during shutting down an executor: %s%n", e.getMessage());
        }

        System.out.println("shutdown finished");
    }

    /**
     * Creates new Job object by given type
     *
     * @param type needed job type from enum {@code Type}
     * @param id   unique job identification
     * @return created Job object
     */
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

    /**
     * Prints out all Job status information for all existed jobs
     */
    private void printAllJobStatuses() {
        for (Job job : allJobs.values()) {
            System.out.printf("Job id: %d type: %s status: %s%n",
                    job.getId(),
                    job.getType(),
                    job.getStatus());
        }
    }
}
