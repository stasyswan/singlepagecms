package gd.java.concurrency.jobmanager;

import com.beust.jcommander.*;

import gd.java.concurrency.jobmanager.job.FailsWithExceptionJob;
import gd.java.concurrency.jobmanager.job.InfinityJob;
import gd.java.concurrency.jobmanager.job.InterruptedJob;
import gd.java.concurrency.jobmanager.job.LongRunningJob;

import java.util.*;
import java.util.concurrent.*;

import static gd.java.concurrency.jobmanager.Command.*;
import static gd.java.concurrency.jobmanager.Status.*;

@Parameters(separators = "=")
public class JobManager {
    Map<Integer, Job> allJobs = new HashMap<>();
    Map<Integer, Future> jobFuTures = new HashMap<>();
    public volatile static Map<Integer, Thread> jobThreads = new HashMap<>();

    public void createJob(String type, int count){
        for (int i = 0; i <count ; i++) {
            int id = allJobs.size() + 1;

            Job job = null;
            switch (Type.valueOf(type)){
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

            allJobs.put(id, job);
            System.out.println("Job id: " + id);
        }
    }

    public void runJob(ExecutorService executor, String type){
        for(Map.Entry<Integer, Job> job : allJobs.entrySet()){
            Job currentJob = job.getValue();
            if(currentJob.getType().toString().equals(type)){
                Future future = executor.submit(currentJob.getTask());
                jobFuTures.put(currentJob.getId(), future);
                currentJob.changeStatus(Status.RUNNING);
                System.out.println("Started Job with id: " + currentJob.getId() + " and type: " + currentJob.getType());
            }
        }
    }

    public void stopJob(int id){
        Future future = jobFuTures.get(id);
        if(future == null) {
            System.out.println("There is no running Job with id: " + id);
        }
        else {
            future.cancel(true);
            allJobs.get(id).changeStatus(Status.FINISHED);
            System.out.println("Cancelled Job with id: " + id);
        }
        for(Job job : allJobs.values()){
            System.out.println("Job id: " + job.getId() + " type: " + job.getType() + " status: " + job.getStatus());
        }
    }

    public void interruptJob(int id){
        Future future = jobFuTures.get(id);
        Job toInterruptJob = allJobs.get(id);
        if(future == null) {
            System.out.println("There is no running Job with id: " + id);
        }
        else {
            jobThreads.get(id).interrupt();
            toInterruptJob.changeStatus(Status.INTERRUPTED);
        }
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
            for(Job job : allJobs.values()){
                jobFuTures.get(job.getId()).cancel(true);
                if(job.getStatus().equals(RUNNING)) {
                    job.changeStatus(FINISHED);
                }
            }
            for(Job job : allJobs.values()){
                System.out.println("Job id: " + job.getId() + " type: " + job.getType() + " status: " + job.getStatus());
            }
            System.out.println("attempt to shutdown executor");
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
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

    static void printInfo(){
        System.out.println("Possible commands:");
        System.out.println("create <job_type> <jobs_count>");
        System.out.println("start <job_type>");
        System.out.println("status <job_id>");
        System.out.println("stop <job_id>");
        System.out.println("interrupt <job_id>");
        System.out.println("stop-all");
        System.out.println();

        String msg = "Job Type: ";
        for (Type type : Type.values()) {
            msg += type.toString() + " ";
        }
        System.out.println(msg);

        System.out.println();
        System.out.println("App started to listen user's input");
        System.out.println();
    }

    static boolean isCommandWrong(String command){
        boolean isWrong = false;
        try{
            Command.valueOf(command.toUpperCase());
        }
        catch (IllegalArgumentException e){
            isWrong = true;
        }
        return  isWrong;
    }

    static boolean isTypeWrong(String type){
        boolean isWrong = false;
        try{
            Type.valueOf(type);
        }
        catch (IllegalArgumentException e){
            isWrong = true;
        }
        return  isWrong;
    }

    @Parameter(names = { "--max-threads" }, required = true)
    public static int maxThreads;

    public static boolean running = true;

    public static void main(String[] args) {
        JobManager jm = new JobManager();
        new JCommander(jm, args);
        ExecutorService executor = Executors.newFixedThreadPool(maxThreads);

        printInfo();

        Scanner sc = new Scanner(System.in);

        while(running){
            String[] arr = sc.nextLine().split(" ");
            String command = arr[0].toUpperCase();

            if(command.equals("")){
                continue;
            }

            if(isCommandWrong(command)) {
                System.out.println("Wrong command");
                continue;
            }

            String parameter = "";
            int count = 0;

            switch (arr.length) {
                case 1:
                    if (!command.isEmpty() && !command.equals(STOPALL.toString()) && !command.equals(STARTALL.toString())) {
                        System.out.println("Need to set job type");
                    }
                    break;
                case 2:
                    if (command.equals(CREATE.toString())) {
                        System.out.println("Need to set job type and count of jobs");

                    }
                    else {
                        if(command.equals(START.toString()) && isTypeWrong(arr[1])){
                            System.out.println("Wrong job type");
                            break;
                        }
                        parameter = arr[1];
                    }
                    break;
                case 3:
                    if(isTypeWrong(arr[1])){
                        System.out.println("Wrong job type");
                        break;
                    }
                    parameter = arr[1];
                    try {
                        count = Integer.parseInt(arr[2]);
                    } catch (NumberFormatException e) {
                        System.out.println("Jobs count should be integer");
                    }
                    break;
            }

            switch (Command.valueOf(command)) {
                case CREATE:
                    jm.createJob(parameter, count);
                    break;
                case START:
                    jm.runJob(executor, parameter);
                    break;
                case STARTALL:
                    for(Type type : Type.values()){
                        jm.runJob(executor, type.toString());
                    }
                    break;
                case STATUS:
                    jm.printJobStatus(Integer.parseInt(parameter));
                    break;
                case STOP:
                    jm.stopJob(Integer.parseInt(parameter));
                    break;
                case STOPALL:
                    running = false;
                    jm.exit(executor);
                    break;
                case INTERRUPT:
                    jm.interruptJob(Integer.parseInt(parameter));
            }
        }
    }
}
