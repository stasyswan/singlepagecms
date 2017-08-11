package gd.java.concurrency.pubsub;

public class Message {
    private String message;
    static final Message EXIT_MESSAGE = new Message("All done from Producer side.");

    public Message(String message) {
        this.message = message;
    }

    String getMsg() {
        return message;
    }
}
