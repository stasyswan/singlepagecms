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

                String[] commands = {"command", "type", "number", "id"};
                HashMap params = new HashMap();

                for (int i = 0; i < commands.length; i++) {
                    if (commandLine.hasOption(commands[i])) {
                        params.put(commands[i], commandLine.getOptionValue(commands[i]));
                    }
                }

                try {
                    Method commandMethod = JobManager.class.getMethod(
                            params.get("command").toString(),
                            HashMap.class
                    );
                    commandMethod.invoke(jm, params);
                } catch (NoSuchMethodException |
                        IllegalAccessException |
                        InvocationTargetException e) {
                    e.printStackTrace();
                }
            } catch (IllegalArgumentException | ParseException e) {
                System.out.println("Wrong command");
            }
        }
    }
}
