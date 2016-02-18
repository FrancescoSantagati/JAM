package it.francescosantagati.jam;

/**
 * A behaviour that is executed until {@link JAMBehaviour#done()} method is called.
 *
 * @author Francesco Santagati
 */
public abstract class JAMWhileBehaviour extends JAMBehaviour {

    public JAMWhileBehaviour(JAMAgent myAgent) {
        super(myAgent);
    }

    /**
     * A "while" behaviour have three steps:
     * <ul>
     * <li>setup: initialization
     * <li>action: executed until {@link JAMBehaviour#done()} is called
     * <li>dispose: last operation before behaviour end
     * </ul>
     */
    public void run() {
        try {
            setup();
            while (!isDone())
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
        }
    }
}
