package it.francescosantagati.jam;

/**
 * <p>A it.francescosantagati.jam.JAMAgent behaviour that is written inside {@link JAMBehaviour#action()} ()} method.</p>
 * <p>A behaviour can be added to an agent by {@link JAMAgent#addBehaviour(JAMBehaviour)} method
 * and is executed on {@link JAMAgent#start()} method.</p>
 *
 * @author Francesco Santagati
 */
public abstract class JAMBehaviour implements Runnable {

    private boolean done;
    private Thread myThread;
    public JAMAgent myAgent;

    public JAMBehaviour(JAMAgent agent) {
        myAgent = agent;
        done = false;
        myThread = null;
    }

    /**
     * Terminate this behaviour.
     */
    public void done() {
        done = true;
        myThread.interrupt();
    }

    /**
     * Check if this behaviour si terminated.
     *
     * @return True if is terminated. False otherwise.
     */
    public boolean isDone() {
        return done;
    }

    /**
     * Set a new {@link Thread} to run this behaviour..
     *
     * @param myThread A {@link Thread} instance.
     */
    public void setMyThread(Thread myThread) {
        this.myThread = myThread;
    }

    /**
     * Pause the behaviour thread.
     *
     * @param ms Duration
     */
    public void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Log.e(this, e.getMessage());
        }
    }

    public boolean hasNeverBeenStarted() {
        return myThread == null;
    }

    /**
     * Behaviour initialization method.
     *
     * @throws JAMBehaviourInterruptedException when is interrupted
     */
    public abstract void setup() throws JAMBehaviourInterruptedException;

    /**
     * Behaviour method that can be execute one or more times.
     *
     * @throws JAMBehaviourInterruptedException when is interrupted
     */
    public abstract void action() throws JAMBehaviourInterruptedException;

    /**
     * Behaviour method called after {@link JAMBehaviour#done()} is called.
     *
     * @throws JAMBehaviourInterruptedException when is interrupted
     */
    public abstract void dispose() throws JAMBehaviourInterruptedException;
}
