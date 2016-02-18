package it.francescosantagati.jam;

/**
 * Interface definition for a callback to be invoked when a log message occur.
 *
 * @author Francesco Santagati
 */
public interface LogListener {

    /**
     * Called when a event to log occur.
     *
     * @param logMessage it.francescosantagati.jam.Message
     */
    void onLogMessage(String logMessage);
}
