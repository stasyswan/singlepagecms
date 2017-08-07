package gd.java.concurrency.pub_sub_example;
import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable {
    private BlockingQueue<Message> queue;

    Producer(BlockingQueue<Message> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        // producing Message messages
        for (int i = 0; i < 5; i++) {
            Message msg = new Message("i'm msg " + (i + 1));
            try {
                Thread.sleep(10);
                queue.put(msg);
                System.out.println("Producer: Message - " + msg.getMsg() + " produced.");
            } catch (Exception e) {
                System.out.println("Exception:" + e);
            }
        }
        // adding exit message
        try {
            queue.put(Message.EXIT_MESSAGE);
            System.out.println("Producer: Exit Message - " + Message.EXIT_MESSAGE.getMsg());
        } catch (Exception e) {
            System.out.println("Exception:" + e);
        }
    }
}
