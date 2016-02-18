package it.francescosantagati.jam;

/**
 * Helper class to print console log
 *
 * @author Francesco Santagati
 */
public class Log {

    /**
     * Debug log
     *
     * @param caller  Object that generate the log
     * @param message it.francescosantagati.jam.Log message
     */
    public static void d(Object caller, String message) {
        print("DEBUG - " + caller.getClass().getSimpleName() + ": " + message);
    }

    /**
     * Debug log
     *
     * @param agent  it.francescosantagati.jam.JAMAgent that generate the log
     * @param message it.francescosantagati.jam.Log message
     */
    public static void agent(JAMAgent agent, String message) {
        print("DEBUG - " + agent.getMyID().getName().toUpperCase() + ", " + agent.getMyID().getCategory() + ": " + message);
    }

    /**
     * Error log
     *
     * @param caller  Object that generate the log
     * @param message Error message
     */
    public static void e(Object caller, String message) {
        print("ERROR - " + caller.getClass().getSimpleName() + ": " + message);
    }

    private static void print(String message) {
        System.out.println(message);
    }
}