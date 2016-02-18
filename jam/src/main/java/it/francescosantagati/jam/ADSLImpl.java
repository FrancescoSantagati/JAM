package it.francescosantagati.jam;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Concrete class that allows agents who have registered to communicate.
 *
 * @author Francesco Santagati
 */
public class ADSLImpl extends UnicastRemoteObject implements ADSL {

    /**
     * Port used to enstabilish the connection
     */
    public int PORT;
    private String IP;
    private String NAME;
    private List<LogListener> listener;
    private List<RemoteMessageBox> messageBoxes;

    /**
     * Construct an it.francescosantagati.jam.ADSL with default params.
     *
     * @throws RemoteException when fail
     */
    public ADSLImpl() throws RemoteException {
        this(ADSL.IP, ADSL.PORT, ADSL.NAME);
    }

    /**
     * Construct an it.francescosantagati.jam.ADSL with params specified.
     *
     * @param ip   Ip address
     * @param port Port
     * @param name Adsl name
     * @throws RemoteException when fail
     */
    public ADSLImpl(String ip, int port, String name) throws RemoteException {
        IP = ip;
        PORT = port;
        NAME = name;

        messageBoxes = new ArrayList<>();
        new ADSLMonitor(this).showFrame();
        listener = new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized List<RemoteMessageBox> getRemoteMessageBox(AgentID agentID) throws RemoteException {

        String message = "Richiesto box (" + agentID + ")";
//        it.francescosantagati.jam.Log.d(this, message);
        notifyListener(message);

        List<RemoteMessageBox> listMatch = new ArrayList<>(messageBoxes.size());
        for (RemoteMessageBox remoteMessageBox : messageBoxes) {
            if (remoteMessageBox.getOwner().equals(agentID)) {
                listMatch.add(remoteMessageBox);
            }
        }
        return listMatch;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void insertRemoteMessageBox(RemoteMessageBox remoteMessageBox) throws RemoteException {
        if (messageBoxes.contains(remoteMessageBox)) {
            Log.e(this, "it.francescosantagati.jam.Message box " + remoteMessageBox.getOwner() + " already exists");
            throw new IllegalArgumentException();
        }

        try {
            String message = "Iscrizione nuovo box per " + remoteMessageBox.getOwner();
            messageBoxes.add(remoteMessageBox);
//            it.francescosantagati.jam.Log.d(this, message);
            notifyListener(message);
        } catch (RemoteException e) {
            notifyListener("Errore: " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void removeRemoteMessageBox(AgentID agentID) throws RemoteException {
        for (RemoteMessageBox remoteMessageBox : messageBoxes) {
            try {
                if (remoteMessageBox.getOwner().equals(agentID)) {
                    messageBoxes.remove(remoteMessageBox);
                    String message = "Cancellato box " + remoteMessageBox.getOwner();
//                    it.francescosantagati.jam.Log.d(this, message);
                    notifyListener(message);
                    return;
                }
            } catch (RemoteException e) {
                notifyListener("Errore: " + e.getMessage());
            }
        }

        throw new IllegalArgumentException("Agent not found");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startRMIRegistry() throws RemoteException {
        LocateRegistry.createRegistry(PORT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startADSL() throws RemoteException {
        try {
            Naming.rebind(getConnectionString(), this);
            notifyListener("Rebind it.francescosantagati.jam.ADSL on port " + PORT);
        } catch (MalformedURLException e) {
            notifyListener("Errore: " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stopADSL() throws RemoteException {
        try {
            Naming.unbind(getConnectionString());
            notifyListener("Unbind it.francescosantagati.jam.ADSL on port " + PORT);
        } catch (NotBoundException | MalformedURLException e) {
            notifyListener("Errore: " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void addListener(LogListener listener) {
        this.listener.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void removeListener(LogListener listener) {
        this.listener.remove(listener);
    }

    private void notifyListener(String message) {
        for(LogListener listener : this.listener) {
            listener.onLogMessage(message);
        }
    }

    public String getConnectionString() {
        return RMIUtil.renderConnectionString(IP, PORT, NAME);
    }
}