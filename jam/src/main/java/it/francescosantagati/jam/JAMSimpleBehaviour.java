package it.francescosantagati.jam;

/**
 * A behaviour that is executed just once.
 *
 * @author Francesco Santagati
 */
public abstract class JAMSimpleBehaviour extends JAMBehaviour {

    public JAMSimpleBehaviour(JAMAgent myAgent) {
        super(myAgent);
    }

    /**
     * A simple behaviour have three steps:
     * <ul>
     * <li>setup: initialization
     * <li>action: behaviour execution
     * <li>dispose: last operation before behaviour end
     * </ul>
     */
    public void run() {
        try {
            setup();
            action();

        } catch (JAMBehaviourInterruptedException e) {
            if (isDone()) return;
            Log.e(this, e.getMessage());

        } finally {
            try {
                dispose();
            } catch (JAMBehaviourInterruptedException e) {
                Log.e(this, e.getMessage());
            }
            done();
        }
    }
}
