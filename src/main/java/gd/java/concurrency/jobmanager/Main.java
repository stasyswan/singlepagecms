package gd.java.concurrency.jobmanager;

import org.apache.commons.cli.*;

public class Main {
    public static void main(String[] args) {
        CommandLineParser cmdLinePosixParser = new DefaultParser();

        try {
            ConsoleInput ci = new ConsoleInput();
            CommandLine commandLine = cmdLinePosixParser.parse(ci.options(), args);
            int maxThreads = Integer.parseInt(commandLine.getOptionValue("max-threads"));

            ci.printInfo();
            ci.getCommand(maxThreads);
        } catch (NumberFormatException | ParseException e) {
            System.out.println("Problem with program argument 'max-threads'");
        }
    }
}