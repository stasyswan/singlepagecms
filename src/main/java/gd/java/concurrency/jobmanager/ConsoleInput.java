package gd.java.concurrency.jobmanager;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static gd.java.concurrency.jobmanager.Command.*;

class ConsoleInput {

     void printInfo(){
        System.out.println("Possible commands:");
        System.out.println("create <job_type> <jobs_count>");
        System.out.println("start <job_type>");
        System.out.println("status <job_id>");
        System.out.println("stop <job_id>");
        System.out.println("stopall");
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

     boolean isCommandWrong(String command){
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

    void oneArgument(String command){
        if (!command.isEmpty() &&
            (command.equals(STATUS.toString()) ||
            command.equals(STOP.toString()))){
            System.out.println("Need to set job id");
        }
        else if (!command.isEmpty() &&
            !command.equals(STOPALL.toString()) &&
            !command.equals(STARTALL.toString())) {
            System.out.println("Need to set job type");
        }
    }

    String twoArguments(String command, String parameter){
        if(isTypeWrong(parameter) && !command.equals(STOP.toString()) && !command.equals(STATUS.toString())){
            System.out.println("Wrong job type");
        }

        else if (command.equals(CREATE.toString())) {
            System.out.println("Need to set job type and count of jobs");
        }
        else {
            return parameter;
        }
        return "";
    }

    String threeArgumentsParameter(String parameter){
        if(isTypeWrong(parameter)){
            System.out.println("Wrong job type");
            return "";
        }
        return parameter;
    }

    int threeArgumentsCount(String count){
        int c = 0;
        try {
            c = Integer.parseInt(count);
        } catch (NumberFormatException e) {
            System.out.println("Jobs count should be integer");
        }
        return c;
    }

    void getCommand(int maxThreads){
        JobManager jm = new JobManager();
        boolean running = true;
        Scanner sc = new Scanner(System.in);
        ExecutorService executor = Executors.newFixedThreadPool(maxThreads);

         while(running){
            String[] arr = sc.nextLine().split(" ");
            String command = arr[0].toUpperCase();
             String parameter = "";
             int count = 0;

            if(command.equals("")){
                continue;
            }

            if(isCommandWrong(command)) {
                System.out.println("Wrong command");
                continue;
            }

            switch (arr.length) {
                case 1:
                    oneArgument(command);
                    break;
                case 2:
                    parameter = twoArguments(command, arr[1]);
                    break;
                case 3:
                    parameter = threeArgumentsParameter(arr[1]);
                    count = threeArgumentsCount(arr[2]);
                    break;
            }

             int id = 0;

            switch (Command.valueOf(command)) {
                case CREATE:
                    if(!parameter.isEmpty() && count != 0) {
                        jm.createJob(parameter, count);
                    }
                    break;
                case RUN:
                    if(!parameter.isEmpty()) {
                        jm.runJob(executor, parameter);
                    }
                    break;
                case STARTALL:
                    for(Type type : Type.values()){
                        jm.runJob(executor, type.toString());
                    }
                    break;
                case START:
                    if(!parameter.isEmpty()) {
                        jm.startJob(executor, parameter);
                    }
                    break;
                case STATUS:
                    id = 0;
                    try{
                        if (!parameter.equals("")){
                            id = Integer.parseInt(parameter);
                        }
                    }
                    catch (NumberFormatException e) {
                        System.out.println("Job id count should be integer");
                    }
                    if(id > 0) {
                        jm.printJobStatus(id);
                    }
                    break;
                case STOP:
                    id = 0;
                    try{
                        if (!parameter.equals("")){
                            id = Integer.parseInt(parameter);
                        }
                    }
                    catch (NumberFormatException e) {
                        System.out.println("Jobs count should be integer");
                        break;
                    }
                    if(id > 0) {
                        jm.stopJob(id);
                    }
                    break;
                case STOPALL:
                    running = false;
                    jm.exit(executor);
                    break;
            }
        }
    }
}
