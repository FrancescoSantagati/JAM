package it.francescosantagati.jam;

/**
 * Helper class for RMI connection.
 *
 * @author Francesco Santagati
 */
public class RMIUtil {

    /**
     * Format a connection string to start a RMI registry connection
     *
     * @param ip   Ip address
     * @param port Port
     * @param name Name
     * @return Connection string
     */
    public static String renderConnectionString(String ip, int port, String name) {
        return "rmi://" + ip + ":" + Integer.toString(port) + "/" + name;
    }
}
