package it.francescosantagati.jam;

import java.rmi.RemoteException;

/**
 * A synchronized mailbox remotely accessible and belonging to a particular agent.
 * Other agents can send message by specifying the recipient.
 *
 * @author Francesco Santagati
 */
public class MessageBox extends MessageBoxNoSync implements RemoteMessageBox {

    /**
     * Construct a synchronized message box with a max message limit.
     *
     * @param owner      Agent ID
     * @param maxMessage Max message number
     * @throws RemoteException when fail to connect
     */
    public MessageBox(AgentID owner, int maxMessage) throws RemoteException {
        super(owner, maxMessage);

        if (owner == null) {
            throw new IllegalArgumentException("Wrong parameters");
        }
        if (maxMessages <= 0) {
            throw new IllegalArgumentException("Max messages MUST be a positive number");
        }
    }

    /**
     * Construct a synchronized message box.
     *
     * @param owner Agent ID
     * @throws RemoteException when fail to connect
     */
    public MessageBox(AgentID owner) throws RemoteException {
        super(owner);

        if (owner == null) {
            throw new IllegalArgumentException("Wrong parameters");
        }
    }

    /**
     * {@inheritDoc}
     */
    synchronized public boolean isBoxEmpty() {
        return super.isBoxEmpty();
    }

    /**
     * {@inheritDoc}
     */
    synchronized public boolean isBoxFull() {
        return super.isBoxFull();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AgentID getOwner() {
        return this.owner;
    }

    /**
     * Insert a message in to message box.
     *
     * @param message it.francescosantagati.jam.Message
     * @throws InterruptedException if write fail
     */
    @Override
    public synchronized void writeMessage(Message message) throws InterruptedException {

        if (message == null) {
            throw new IllegalArgumentException("Tutti i parametri sono obbligatori e devono essere diversi da null.");
        }

        try {
            super.writeMessage(message);
//            it.francescosantagati.jam.Log.d(this, "A message writed to " + message.getReceiver().toString());
            notifyAll();
        } catch (JAMMessageBoxException ignored) {
            ignored.printStackTrace();
        }
    }

    /**
     * Retrieve and delete from message box the first message.
     * If no message found an exception it.francescosantagati.jam.JAMMessageBoxException will be thrown
     *
     * @return message
     * @throws InterruptedException if read message fail
     */
    @Override
    public synchronized Message readMessage() throws InterruptedException, JAMMessageBoxException {
        return readMessage(new GenericAgentID());
    }

    /**
     * Retrieve and delete from message box the first message sent by agent provided.
     * If no message found an exception it.francescosantagati.jam.JAMMessageBoxException will be thrown
     *
     * @param agentID it.francescosantagati.jam.AgentID
     * @return message
     * @throws InterruptedException if read message fail
     */
    @Override
    public synchronized Message readMessage(AgentID agentID) throws InterruptedException, JAMMessageBoxException {
        if (agentID == null) {
            throw new IllegalArgumentException("Wrong parameters");
        }

        while ( ! isThereMessage(agentID)) {
//            it.francescosantagati.jam.Log.e(this, "Waiting a message from " + agentID.toString());
            wait();
        }
        notifyAll();

        return super.readMessage(agentID);
    }

    /**
     * Retrieve and delete from message box the first message with performative provided.
     * If no message found an exception it.francescosantagati.jam.JAMMessageBoxException will be thrown
     *
     * @param performative it.francescosantagati.jam.Performative
     * @return message
     * @throws InterruptedException if read message fail
     */
    @Override
    public synchronized Message readMessage(Performative performative) throws InterruptedException, JAMMessageBoxException {
        if (performative == null) {
            throw new IllegalArgumentException("Wrong parameters");
        }

        while ( ! isThereMessage(performative)) {
//            it.francescosantagati.jam.Log.e(this, "Waiting a message with " + performative.toString() + " type");
            wait();
        }
        notifyAll();

        return super.readMessage(performative);
    }

    /**
     * Retrieve and delete from message box the first message sent by agent and with performative provided.
     * If no message found an exception it.francescosantagati.jam.JAMMessageBoxException will be thrown
     *
     * @param agentID      it.francescosantagati.jam.AgentID
     * @param performative it.francescosantagati.jam.Performative
     * @return message
     * @throws InterruptedException if read message fail
     */
    @Override
    public synchronized Message readMessage(AgentID agentID, Performative performative) throws InterruptedException, JAMMessageBoxException {
        if (agentID == null || performative == null) {
            throw new IllegalArgumentException("Wrong parameters");
        }

        while ( ! isThereMessage(agentID, performative)) {
//            it.francescosantagati.jam.Log.e(this, "Waiting a message from " + agentID.toString() + " and with " + performative.toString() + " type");
            wait();
        }
        notifyAll();

        return super.readMessage(agentID, performative);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized boolean isThereMessage() {
        return super.isThereMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized boolean isThereMessage(AgentID agentID) {
        return super.isThereMessage(agentID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized boolean isThereMessage(AgentID agentID, Performative performative) {
        return super.isThereMessage(agentID, performative);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized boolean isThereMessage(Performative performative) {
        return super.isThereMessage(performative);
    }
}
