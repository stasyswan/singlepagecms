//4. Thread-safe Service Factory.
// Factory below is responsible for initializing services, by their name.
// Initialization of single service takes considerable amount of time (1 minute).
// Number of services is limited with approximately 10 services,
// but factory will be called often (100 requests per second).
// Factory is working in multithreaded environment.

package gd.java.concurrency.factory;

import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class ServiceFactoryImpl {
    private ConcurrentMap<String, Service> services = new ConcurrentHashMap<>();
    private ReentrantLock locker = new ReentrantLock();
    // should be thread-safe
    // miminal blocking (for example, getService("a") shouldn't block getService("b))
    // lazy
    private Service getService(String name) {

        if (!services.containsKey(name))  {
            createService(name);
            System.out.println("Created service " + name);
        } else {
            System.out.println("Got service " + name);
        }
        return services.get(name);
    }

    private void createService(String name) {
        locker.lock();
        if (!services.containsKey(name)) {
            services.put(name, new Service(name));
        }
        locker.unlock();
    }


    public static void main(String[] args) {
        ServiceFactoryImpl sfi = new ServiceFactoryImpl();

        sfi.createService("name1");
        sfi.createService("name2");

        ExecutorService executor = Executors.newFixedThreadPool(30);

        for (int i = 0; i < 10; i++) {
            executor.execute(() -> {
                for (int j = 0; j < 10; j++) {
                    sfi.getService("name" + j);
                }

            });
        }
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
        }
    }
}
