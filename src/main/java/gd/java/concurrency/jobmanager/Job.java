package gd.java.concurrency.jobmanager;

/**
 * {@link gd.java.concurrency.jobmanager.Job} represents runnable task
 * which has specific state and type.
 *
 * @author Anastasiia Lebedieva
 */
public abstract class Job implements Runnable {
    private int id;
    private volatile Status status;
    private Type type;
  /**
   * Constructs an empty job with the specified id, status and type.
   *
   * @param id unique job identification
   * @param status specific job state from enum {@code Status}
   * @param type needed job type from enum {@code Type}

   * */
    public Job(int id, Status status, Type type) {
        this.id = id;
        this.status = status;
        this.type = type;
    }

    /**
     * Implementation of interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code Runnable} method to be called in that separately executing
     * thread.
     *
     * @see     java.lang.Runnable#run()
     */
    public abstract void run();

    /**
     * Setter for attribute status
     *
     * @param newStatus new status value
     */
    protected void setStatus(Status newStatus) {
        status = newStatus;
    }

    /**
     * Getter for attribute status
     */
    protected Status getStatus() {
        return status;
    }

    /**
     * Getter for attribute id
     */
    public int getId() {
        return id;
    }


    /**
     * Getter for attribute type
     */
    Type getType() {
        return type;
    }
}
