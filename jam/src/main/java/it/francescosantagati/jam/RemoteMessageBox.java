package it.francescosantagati.jam;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface definition for a remote message box object.
 *
 * @author Francesco Santagati
 */
public interface RemoteMessageBox extends Remote {

    /**
     * Insert a message into message box
     *
     * @param message it.francescosantagati.jam.Message
     * @throws RemoteException when fail to connect
     * @throws InterruptedException when thread is interrupted
     */
    void writeMessage(Message message) throws RemoteException, InterruptedException;

    /**
     * Retrieve agent id that is the message box owner
     *
     * @return it.francescosantagati.jam.AgentID
     * @throws RemoteException when fail to connect
     */
    AgentID getOwner() throws RemoteException;
}
