package it.francescosantagati.jam;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * A no-synchronized mailbox remotely accessible and belonging to a particular agent.
 * Other agents can send message by specifying the recipient.
 *
 * @author Francesco Santagati
 */
public class MessageBoxNoSync<E extends Message> extends UnicastRemoteObject {

    protected int maxMessages;
    protected AgentID owner;
    protected List<E> box;

    /**
     * Construct a message box with a max message limit.
     *
     * @param owner Agent ID
     * @throws RemoteException when fail to connect
     */
    public MessageBoxNoSync(AgentID owner) throws RemoteException {
        this(owner, 10);
    }

    /**
     * Construct a message box with a max message limit.
     *
     * @param owner       Agent ID
     * @param maxMessages Max message number
     * @throws RemoteException when fail to connect
     */
    public MessageBoxNoSync(AgentID owner, int maxMessages) throws RemoteException {
        this.owner = owner;
        this.maxMessages = maxMessages;
        box = new ArrayList<>(maxMessages);
    }

    /**
     * @return it.francescosantagati.jam.AgentID
     */
    public AgentID getOwner() {
        return owner;
    }

    /**
     * Check if box is empty.
     *
     * @return True if is empty, False otherwise
     */
    public boolean isBoxEmpty() {
        return box.isEmpty();
    }

    /**
     * Check if box is full
     *
     * @return Trus if is full. False otherwise
     */
    public boolean isBoxFull() {
        return maxMessages == box.size();
    }

    /**
     * Insert an element in message box.
     *
     * @param element An element
     * @throws JAMMessageBoxException if message box is full
     * @throws InterruptedException if write fail
     */
    public void writeMessage(E element) throws InterruptedException, JAMMessageBoxException {
        if (element == null) {
            throw new IllegalArgumentException("Wrong parameters");
        }
        if (isBoxFull()) {
            throw new JAMMessageBoxException("it.francescosantagati.jam.Message box is full");
        }
        box.add(element);
    }



    /**
     * Retrieve and delete from message box the first message sent by agent and with performative provided.
     * If no message found an exception it.francescosantagati.jam.JAMMessageBoxException will be thrown
     *
     * @param agentID      it.francescosantagati.jam.AgentID
     * @param performative it.francescosantagati.jam.Performative
     * @return message
     * @throws JAMMessageBoxException if message not found
     * @throws InterruptedException if read fail
     */
    public Message readMessage(AgentID agentID, Performative performative) throws JAMMessageBoxException, InterruptedException {
        if (agentID == null || performative == null) {
            throw new IllegalArgumentException("Wrong parameters");
        }
        if (box.isEmpty()) {
            throw new JAMMessageBoxException("it.francescosantagati.jam.Message box is empty");
        }
        ListIterator<E> it = box.listIterator();
        while (it.hasNext()) {
            Message m = it.next();
            if (agentID.equals(m.getSender()) && m.getPerformative().toString().equals(performative.toString())) {
                it.remove();
                return m;
            }
        }

        throw new JAMMessageBoxException("it.francescosantagati.jam.Message not found");
    }

    /**
     * Retrieve and delete from message box the first message sent by agent provided.
     * If no message found an exception it.francescosantagati.jam.JAMMessageBoxException will be thrown
     *
     * @param agentID it.francescosantagati.jam.AgentID
     * @return message
     * @throws JAMMessageBoxException if message not found
     * @throws InterruptedException if read fail
     */
    public Message readMessage(AgentID agentID) throws JAMMessageBoxException, InterruptedException {
        if (agentID == null) {
            throw new IllegalArgumentException("Wrong parameters");
        }
        if (box.isEmpty()) {
            throw new JAMMessageBoxException("it.francescosantagati.jam.Message box is empty");
        }
        ListIterator<E> it = box.listIterator();
        while (it.hasNext()) {
            Message m = it.next();
            if (agentID.equals(m.getSender())) {
                it.remove();
                return m;
            }
        }

        throw new JAMMessageBoxException("it.francescosantagati.jam.Message not found");
    }

    /**
     * Retrieve and delete from message box the first message with performative provided.
     * If no message found an exception it.francescosantagati.jam.JAMMessageBoxException will be thrown
     *
     * @param performative it.francescosantagati.jam.Performative
     * @return message
     * @throws JAMMessageBoxException if message not found
     * @throws InterruptedException if read fail
     */
    public Message readMessage(Performative performative) throws JAMMessageBoxException, InterruptedException {
        if (performative == null) {
            throw new IllegalArgumentException("Wrong parameters");
        }
        if (box.isEmpty()) {
            throw new JAMMessageBoxException("it.francescosantagati.jam.Message box is empty");
        }
        ListIterator<E> it = box.listIterator();
        while (it.hasNext()) {
            Message m = it.next();
            if (m.getPerformative().toString().equals(performative.toString())) {
                it.remove();
                return m;
            }
        }

        throw new JAMMessageBoxException("it.francescosantagati.jam.Message not found");
    }

    /**
     * Retrieve and delete from message box the first message.
     * If no message found an exception it.francescosantagati.jam.JAMMessageBoxException will be thrown
     *
     * @return message
     * @throws JAMMessageBoxException if message not found
     * @throws InterruptedException if read fail
     */
    public Message readMessage() throws JAMMessageBoxException, InterruptedException {
        return readMessage(new GenericAgentID());
    }

    /**
     * Check if a message sent by agent and with provided performative is in the message box.
     *
     * @param agentID      it.francescosantagati.jam.AgentID
     * @param performative it.francescosantagati.jam.Performative
     * @return True if message found, false otherwise
     */
    public boolean isThereMessage(AgentID agentID, Performative performative) {
        if (agentID == null || performative == null) {
            throw new IllegalArgumentException("Wrong parameters");
        }
        for (E m : box) {
            if (agentID.equals(m.getSender()) && m.getPerformative().toString().equals(performative.toString())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if a message sent by provided agent in the message box.
     *
     * @param agentID it.francescosantagati.jam.AgentID
     * @return True if message found, false otherwise
     */
    public boolean isThereMessage(AgentID agentID) {
        for (E m : box) {
            if (agentID.equals(m.getSender())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if a message with provided performative is in the message box.
     *
     * @param performative it.francescosantagati.jam.Performative
     * @return True if message found, false otherwise
     */
    public boolean isThereMessage(Performative performative) {
        for (E m : box) {
            if (m.getPerformative().toString().equals(performative.toString())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if a message is in the message box.
     *
     * @return True if message found, false otherwise
     */
    public boolean isThereMessage() {
        return ! box.isEmpty();
    }
}