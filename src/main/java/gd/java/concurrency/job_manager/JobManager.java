package gd.java.concurrency.job_manager;


import com.beust.jcommander.*;

import gd.java.concurrency.job_manager.job.FailsWithExceptionJob;
import gd.java.concurrency.job_manager.job.InfinityJob;
import gd.java.concurrency.job_manager.job.LongRunningJob;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

@Parameters(separators = "=")
public class JobManager {
    Map<Integer, Job> allJobs = new HashMap<>();

    public JobManager() {
        allJobs.put(1, new LongRunningJob(1));
        allJobs.put(2, new LongRunningJob(2));
        allJobs.put(3, new InfinityJob(3));
        allJobs.put(4, new InfinityJob(4));
        allJobs.put(5, new FailsWithExceptionJob(5));
    }

    public void runJob(String type){
        for(Map.Entry<Integer, Job> job : allJobs.entrySet()){
            if(job.getValue().getType().toString().equals(type)){
                job.getValue().start(job.getValue().getTask());
            }
        }
    }

    public void stopJob(int id){
        allJobs.get(id).stop();
        for(Job job : allJobs.values()){
            System.out.println("Job id: " + job.getId() + " type: " + job.getType() + " status: " + job.getStatus());
        }
    }

    public void printJobStatus(int id){
        System.out.println("Status: " + allJobs.get(id).getStatus());
    }

    public void stopAllJobs(){
        for(Job job : allJobs.values()){
            if(job.getStatus().equals(Statuses.RUNNING)) {
                job.stop();
            }
            System.out.println("Job id: " + job.getId() + " type: " + job.getType() + " status: " + job.getStatus());
        }
    }

    public void exit(){
        try {
            for(Job job : allJobs.values()){
                if(job.getStatus().equals(Statuses.RUNNING)) {
                    job.changeStatus(Statuses.FINISHED);
                }
            }
            System.out.println("attempt to shutdown executor");
            Executor.getInstance().shutdown();
            Executor.getInstance().awaitTermination(5, TimeUnit.SECONDS);
        }
        catch (InterruptedException e) {
            System.err.println("tasks interrupted");
        }
        finally {
            if (!Executor.getInstance().isTerminated()) {
                System.err.println("cancel non-finished tasks");
            }
            Executor.getInstance().shutdownNow();
            System.out.println("shutdown finished");
        }
    }

    @Parameter(names = { "--max-threads" }, required = true)
    public static int maxThreads;

    public static void main(String[] args) {
        JobManager jm = new JobManager();

        new JCommander(jm, args);

        System.out.println("Possible commands:");
        System.out.println("start <job_type>");
        System.out.println("status <job_id>");
        System.out.println("stop <job_id>");
        System.out.println("stop-all");
        System.out.println();

        String msg = "Job Types: ";
        for (Types type : Types.values()) {
            msg += type.toString() + " ";
        }
        System.out.println(msg);

        System.out.println();
        System.out.println("App started to listen user's input");
        System.out.println();

        Scanner sc = new Scanner(System.in);
        boolean running = true;

        while(running){
            String command = sc.next();
            String parameter = "";
            if(!command.equals("stop-all")) {
                parameter = sc.next();
            }

            switch (command){
                case "start" :
                    jm.runJob(parameter);
                    break;
                case "status" :
                    jm.printJobStatus(Integer.parseInt(parameter));
                    break;
                case "stop" :
                    jm.stopJob(Integer.parseInt(parameter));
                    break;
                case "stop-all" :
                    jm.exit();
                    running = false;
                    break;
            }
        }
    }
}
