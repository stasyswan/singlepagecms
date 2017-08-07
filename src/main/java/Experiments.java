import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.function.LongBinaryOperator;
import java.util.stream.IntStream;

public class Experiments {

    private int countDown = 5;
    private Thread t;

    public Experiments(String name){
        t = new Thread(new Runnable() {
            @Override
            public void run() {
                if(Thread.currentThread().getName().equals("Thread0")){
                    Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
                }
                try {
                    while(true){
                        System.out.println(this);
                        if(--countDown == 0){
                            return;
                        }
                        TimeUnit.MILLISECONDS.sleep(10);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            public String toString(){
                return Thread.currentThread().getName() + ":" + countDown;
            }
        }, name);
        t.start();
    }

    public static void main(String[] args) throws Exception {
//        System.out.println();
//        Integer i1 = 1532;
//        Integer i2 = 1532;
//        System.out.println(i1==i2);
//
//        ArrayList<Integer> arr = new ArrayList<>();
//        for(int i = 0; i < 10; i++){
//            arr.add(i);
//        }
//        System.out.println(arr);
//
//        arr.removeIf(a -> a > 5);
//
//        System.out.println(arr);
//
//        for (Iterator<Integer> it = arr.iterator(); it.hasNext(); ) {
//            int a = it.next();
//            if(a > 3) it.remove();
//        }
//
//        System.out.println(arr);
//        System.out.println();
//
//        for (int i = 0; i < 3; i++) {
//            new Experiments("Thread" + i);
//        }


//        ArrayList<MapTable> arr = new ArrayList<>();
//        for(long i = 0; i < 1000000000; i++){
//            arr.add(new MapTable("number", "name", "price"));
//        }

        LongBinaryOperator op = (x, y) -> 2 * x + y;
        LongAccumulator accumulator = new LongAccumulator(op, 1L);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        IntStream.range(0, 10)
                .forEach(i -> executor.submit(() -> accumulator.accumulate(i)));

        stop(executor);

        System.out.println(accumulator.getThenReset());
    }

    public static void stop(ExecutorService executor) {
        try {
            executor.shutdown();
            executor.awaitTermination(60, TimeUnit.SECONDS);
        }
        catch (InterruptedException e) {
            System.err.println("termination interrupted");
        }
        finally {
            if (!executor.isTerminated()) {
                System.err.println("killing non-finished tasks");
            }
            executor.shutdownNow();
        }
    }

    public static void sleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
