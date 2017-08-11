package gd.java.concurrency.factory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServiceFactoryImpl {

    public Service getService(String name) {
        return createService(name);
    }

    public Service run() {
        return createService("vfdf");
    }

    private Service createService(String name) {
        return new Service(name);
    }

    public static void main(String[] args) {
        ServiceFactoryImpl sfi = new ServiceFactoryImpl();
        ExecutorService executor = Executors.newFixedThreadPool(3);
        executor.submit(sfi::run);

    }
}
