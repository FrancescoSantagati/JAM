package it.francescosantagati.jam.sample;

import it.francescosantagati.jam.ADSLImpl;

import java.rmi.RemoteException;

/**
 * A class that allows Banditore and Clienti to communicate.
 *
 * @author Francesco Santagati
 */
public class AstaADSL {

    public static void main(String[] args) {

        try {
            new ADSLImpl("127.0.0.1", 1099, "ADSL");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
