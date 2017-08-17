//4. Thread-safe Service Factory.
// Factory below is responsible for initializing services, by their name.
// Initialization of single service takes considerable amount of time (1 minute).
// Number of services is limited with approximately 10 services,
// but factory will be called often (100 requests per second).
// Factory is working in multithreaded environment.

package gd.java.concurrency.factory;

import java.util.concurrent.*;

public class ServiceFactoryImpl {
    ConcurrentMap<String, Service> services = new ConcurrentHashMap<>();

    // should be thread-safe
    // miminal blocking (for example, getService("a") shouldn't block getService("b))
    // lazy
    public Service getService(String name) {
       if(createService(name) == null){
           System.out.println("Created service " + name);
       }
       else{
           System.out.println("Got service " + name);
       }
        return services.get(name);
    }

    private Service createService(String name) {
        return services.putIfAbsent(name, new Service(name));
    }

    public static void main(String[] args) {
        ServiceFactoryImpl sfi = new ServiceFactoryImpl();

        sfi.createService("name1");
        sfi.createService("name2");

        ExecutorService executor = Executors.newFixedThreadPool(3);

        executor.execute(() -> {
            sfi.getService("name1");
        });
        executor.execute(() -> {
            sfi.getService("name3");
        });
        executor.execute(() -> {
            sfi.getService("name4");
        });

        try {
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
}
