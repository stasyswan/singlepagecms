package gd.java.concurrency.jobmanager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Scanner;

import org.apache.commons.cli.*;

class ConsoleInput {
    void printInfo() {
        System.out.println("Possible commands:");
        System.out.println("-c create -t <job_type> -n <jobs_number>");
        System.out.println("-c start -t <job_type>");
        System.out.println("-c run -t <job_type>");
        System.out.println("-c status -i <job_id>");
        System.out.println("-c stop -i <job_id>");
        System.out.println("-c stopall");
        System.out.println("-c startall");
        System.out.println();

        StringBuilder msg = new StringBuilder("Job Types: ");
        for (Type type : Type.values()) {
            msg.append(type.toString()).append(" ");
        }
        System.out.println(msg);
        System.out.println();

        System.out.println("For example:");
        System.out.println("-c create -t LONG_RUNNING -n 3");
        System.out.println("-c create -t WITH_EXCEPTION -n 1");
        System.out.println("-c create -t INFINITY -n 2");
        System.out.println("-c create -t INTERRUPTED -n 3");

        System.out.println();
        System.out.println("App started to listen user's input");
        System.out.println();
    }

    Options options() {
        Options posixOptions = new Options();

        Option option = new Option("m", "max-threads", true, "max threads");
        option.setValueSeparator('=');
        posixOptions.addOption(option);

        posixOptions.addOption(new Option("c", "command", true, "Command"));
        posixOptions.addOption(new Option("t", "type", true, "Command type"));
        posixOptions.addOption(new Option("n", "number", true, "Number of jobs"));
        posixOptions.addOption(new Option("i", "id", true, "Job ID"));

        return posixOptions;
    }

    void getCommand(int maxThreads) {
        boolean running = true;

        JobManager jm = new JobManager(maxThreads);
        Scanner sc = new Scanner(System.in);

        while (running) {
            CommandLineParser cmdLinePosixParser = new DefaultParser();

            try {
                String[] args = sc.nextLine().split(" ");
                CommandLine commandLine = cmdLinePosixParser.parse(options(), args);
                HashMap<String, String> params = new HashMap<>();

                for (Option o : options().getOptions()) {
                    if (commandLine.hasOption(o.getLongOpt()) &&
                            !o.getLongOpt().equals("max-threads")) {
                        params.put(o.getLongOpt(), commandLine.getOptionValue(o.getLongOpt()));
                    }
                }

                Method commandMethod = JobManager.class.getMethod(
                        params.get("command"),
                        HashMap.class
                );
                commandMethod.invoke(jm, params);

                if (params.get("command").equals("stopall")) running = false;
            } catch (IllegalArgumentException |
                    ParseException |
                    NoSuchMethodException |
                    IllegalAccessException |
                    InvocationTargetException |
                    NullPointerException e) {
                System.out.println("Wrong command");
            }
        }
    }
}
