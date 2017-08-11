package gd.java.concurrency.pubsub;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class Consumer implements Runnable {

    private BlockingQueue<Message> queue;
    private String name;

    Consumer(BlockingQueue<Message> queue, String name) {
        this.queue = queue;
        this.name = name;
    }

    @Override
    public void run() {
        try {
            Message msg;
            // consuming messages until exit message is received
            while (!(msg = queue.poll(12, TimeUnit.MILLISECONDS)).equals(Message.EXIT_MESSAGE)) {
                Thread.sleep(15);
                System.out.println("Consumer " + this.name + ": Message - " + msg.getMsg() + " consumed.");
                System.out.println("Queue size: " + queue.size());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
