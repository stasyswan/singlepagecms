package gd.java.concurrency.jobmanager;

import com.beust.jcommander.*;

@Parameters(separators = "=")
public class Main {

    @Parameter(names = { "--max-threads" }, required = true)
    public static int maxThreads;

    public static void main(String[] args) {
        Main main = new Main();
        new JCommander(main, args);

        ConsoleInput ci = new ConsoleInput();
        ci.printInfo();
        ci. getCommand(maxThreads);
    }
}
