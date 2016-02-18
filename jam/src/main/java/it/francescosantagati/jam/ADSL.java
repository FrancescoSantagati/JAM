package it.francescosantagati.jam;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Interface definition for an it.francescosantagati.jam.ADSL (Agent Directory Service Layer) Remote object
 *
 * @author Francesco Santagati
 */
public interface ADSL extends Remote {

    /**
     * Default ip addres for connection.
     */
    String IP = "127.0.0.1";

    /**
     * Default PORT for connection.
     */
    int PORT = 1099;

    /**
     * Default name for connection.
     */
    String NAME = "it.francescosantagati.jam.ADSL";

    /**
     * Return a {@link RemoteMessageBox} references list whose owner is agentID
     *
     * @param agentID Owner of the {@link RemoteMessageBox} that you want to get references.
     * @return A {@link RemoteMessageBox} list.
     * @throws RemoteException when fail
     */
    List<RemoteMessageBox> getRemoteMessageBox(AgentID agentID) throws RemoteException;

    /**
     * Insert a {@link RemoteMessageBox}
     *
     * @param remoteMessageBox it.francescosantagati.jam.MessageBox to insert into the list.
     * @throws RemoteException when fail
     */
    void insertRemoteMessageBox(RemoteMessageBox remoteMessageBox) throws RemoteException;

    /**
     * Delete a {@link RemoteMessageBox} whose owner is agentID
     *
     * @param agentID Owner of the {@link RemoteMessageBox} that you want to delete.
     * @throws RemoteException when fail
     */
    void removeRemoteMessageBox(AgentID agentID) throws RemoteException;

    /**
     * Start RMI registry with the PORT specified.
     *
     * @throws RemoteException when fail
     */
    void startRMIRegistry() throws RemoteException;

    /**
     * Bind it.francescosantagati.jam.ADSL to RMI registry with the params specified in constructor.
     *
     * @throws RemoteException when fail
     */
    void startADSL() throws RemoteException;

    /**
     * Unbind it.francescosantagati.jam.ADSL from RMI registry.
     *
     * @throws RemoteException when fail
     */
    void stopADSL() throws RemoteException;

    /**
     * Set listener to it.francescosantagati.jam.ADSL.
     * Listener must implement {@link LogListener} interface.
     *
     * @param listener Listener to add
     * @throws RemoteException when fail
     */
    void addListener(LogListener listener) throws RemoteException;

    /**
     * Unset listener from it.francescosantagati.jam.ADSL.
     *
     * @param listener A LogListener instance.
     * @throws RemoteException when fail
     */
    void removeListener(LogListener listener) throws RemoteException;
}