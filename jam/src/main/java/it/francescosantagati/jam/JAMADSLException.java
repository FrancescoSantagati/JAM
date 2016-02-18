package it.francescosantagati.jam;

/**
 * Exception that can be thrown during the normal operation of the {@link ADSL}.
 *
 * @author Francesco Santagati
 */
public class JAMADSLException extends Exception {

    public JAMADSLException() {
        super();
    }

    public JAMADSLException(String message) {
        super(message);
    }

    public JAMADSLException(Exception e) {
        super(e.getMessage(), e.getCause());
    }
}