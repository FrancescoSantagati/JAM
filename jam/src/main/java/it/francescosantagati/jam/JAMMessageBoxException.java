package it.francescosantagati.jam;

/**
 * Exception that can be thrown during the normal operation of the {@link MessageBox}.
 *
 * @author Francesco Santagati
 */
public class JAMMessageBoxException extends Exception {

    public JAMMessageBoxException() {
        super();
    }

    public JAMMessageBoxException(String message) {
        super(message);
    }

    public JAMMessageBoxException(Exception e) {
        super(e.getMessage(), e.getCause());
    }
}
