package it.francescosantagati.jam;

import java.io.IOException;

/**
 * Exception that can be thrown during the normal operation of the {@link ADSL}.
 *
 * @author Francesco Santagati
 */
public class JAMIOException extends Exception {

    public JAMIOException(IOException exception) {
        super(exception);
    }
}
