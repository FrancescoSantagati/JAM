package it.francescosantagati.jam;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * An intelligent object that can have multiple behaviours.
 * We can add behaviours with <code>addBehaviour</code> method.
 * <p>
 * Usage:
 * <ol>
 * <li><code>init()</code>: subscribe agent to an it.francescosantagati.jam.ADSL remote instance.
 * <li><code>start()</code>: start behaviours associated to agent and not currently in execution.
 * <li><code>destroy()</code>: remove agent association from it.francescosantagati.jam.ADSL and stop execution of all behaviours,
 * </ol>
 *
 * @author Francesco Santagati
 */
public abstract class JAMAgent extends Observable {

    private List<JAMBehaviour> myBehaviours;
    private MessageBox myMessageBox;
    private PersonalAgentID myID;
    private ADSL adsl;
    private String name;
    private String ip;
    private int port;

    /**
     * Construct a it.francescosantagati.jam.JAMAgent with default params.
     * <ul>
     * <li>IP: 127.0.0.1
     * <li>Port: 1099
     * <li>Name: it.francescosantagati.jam.ADSL
     * </ul>
     *
     * @param agentID Agent id
     * @throws JAMADSLException when fail to connect
     */
    public JAMAgent(PersonalAgentID agentID) throws JAMADSLException {
        this(agentID, "127.0.0.1", "it.francescosantagati.jam.ADSL", 1099);
    }

    /**
     * Construct a it.francescosantagati.jam.JAMAgent with params specified.
     *
     * @param agentID Agent id
     * @param ip      Ip address
     * @param name    Name
     * @param port    Port
     * @throws JAMADSLException when fail to connect
     */
    public JAMAgent(PersonalAgentID agentID, String ip, String name, int port) throws JAMADSLException {
        myID = agentID;
        this.ip = ip;
        this.name = name;
        this.port = port;

        myBehaviours = new ArrayList<>();

        try {
            myMessageBox = new MessageBox(agentID);
        } catch (RemoteException e) {
            throw new JAMADSLException();
        }

        new JAMAgentMonitor(this).showFrame();
    }

    /**
     * Add a new behaviour to agent.
     *
     * @param behaviour a it.francescosantagati.jam.JAMBehaviour instance
     */
    public void addBehaviour(JAMBehaviour behaviour) {
        myBehaviours.add(behaviour);
    }

    /**
     * Provide agent id.
     *
     * @return Agent id
     */
    public PersonalAgentID getMyID() {
        return myID;
    }

    /**
     * Initialize agent.
     * <ul>
     * <li>Tries to connect to an it.francescosantagati.jam.ADSL instance in the RMI registry;
     * <li>Subscribe agent message box to it.francescosantagati.jam.ADSL;
     * </ul>
     *
     * @throws JAMADSLException when fail
     */
    public void init() throws JAMADSLException {
        String url = RMIUtil.renderConnectionString(ip, port, name);
        try {
            adsl = (ADSL) Naming.lookup(url);
            adsl.insertRemoteMessageBox(myMessageBox);

        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            throw new JAMADSLException(e);
        }
    }

    /**
     * Start every beharious that is not currently in execution in a separate thread.
     */
    public void start() {
        for (JAMBehaviour behaviour : myBehaviours) {
            if (behaviour.hasNeverBeenStarted()) {
                Thread thread = new Thread(behaviour);
                behaviour.setMyThread(thread);
                thread.start();
            }
        }
    }

    /**
     * Destroy agent.
     * <ul>
     * <li>Remove message box from it.francescosantagati.jam.ADSL;
     * <li>Stop all behaviours currently in execution;
     * </ul>
     *
     * @throws JAMADSLException when fail
     */
    public void destroy() throws JAMADSLException {
        try {
            adsl.removeRemoteMessageBox(myID);

            for (JAMBehaviour behaviour : myBehaviours) {
                if (!behaviour.isDone()) {
                    behaviour.done();
                }
            }

        } catch (RemoteException | IllegalArgumentException e) {
            throw new JAMADSLException(e);
        }
    }

    /**
     * Check if there is a message with provided {@link Performative} in the agent message box.
     *
     * @param agent        Agent id
     * @param performative it.francescosantagati.jam.Performative
     * @return True if message is found, False otherwise.
     */
    public boolean check(AgentID agent, Performative performative) {
        return myMessageBox.isThereMessage(agent, performative);
    }

    /**
     * Send a message to message recipients.
     *
     * @param message it.francescosantagati.jam.Message to send
     * @throws JAMADSLException se il collegamento con l'it.francescosantagati.jam.ADSL non � andato a buon fine
     * @throws JAMBehaviourInterruptedException when send fail
     */
    public void send(Message message) throws JAMBehaviourInterruptedException, JAMADSLException {
        try {
            List<RemoteMessageBox> boxList = adsl.getRemoteMessageBox(message.getReceiver());
            for (RemoteMessageBox box : boxList) {
                box.writeMessage(message);
            }

        } catch (RemoteException e) {
            throw new JAMADSLException(e);
        } catch (InterruptedException e) {
            throw new JAMBehaviourInterruptedException();
        }

        String logMessage = "SEND message " + message.getPerformative() + " to " + message.getReceiver();
        setChanged();
        notifyObservers(logMessage);
    }

    /**
     * Retrieve and delete the first message from message box sent by agent and with performative provided.
     * If no message found an exception it.francescosantagati.jam.JAMMessageBoxException will be thrown
     *
     * @param agentID      Agent ID
     * @param performative it.francescosantagati.jam.Performative
     * @return message
     * @throws JAMBehaviourInterruptedException when send fail
     */
    public Message receive(AgentID agentID, Performative performative) throws JAMBehaviourInterruptedException {
        Message message;
        try {
            message = myMessageBox.readMessage(agentID, performative);
        } catch (InterruptedException | JAMMessageBoxException e) {
            throw new JAMBehaviourInterruptedException();
        }
        String logMessage = "RECEIVE message " + message.getPerformative() + " to " + message.getReceiver();
        setChanged();
        notifyObservers(logMessage);
        return message;
    }

    /**
     * Retrieve and delete the first message from message box sent by agent provided.
     * If no message found an exception it.francescosantagati.jam.JAMMessageBoxException will be thrown
     *
     * @param agentID it.francescosantagati.jam.AgentID
     * @return message
     * @throws JAMBehaviourInterruptedException when send fail
     */
    public Message receive(AgentID agentID) throws JAMBehaviourInterruptedException {
        Message message;
        try {
            message = myMessageBox.readMessage(agentID);
        } catch (InterruptedException | JAMMessageBoxException e) {
            throw new JAMBehaviourInterruptedException();
        }
        String logMessage = "RECEIVE message " + message.getPerformative() + " to " + message.getReceiver();
        setChanged();
        notifyObservers(logMessage);
        return message;
    }

    /**
     * Retrieve and delete the first message from message box with performative provided.
     * If no message found an exception it.francescosantagati.jam.JAMMessageBoxException will be thrown
     *
     * @param performative it.francescosantagati.jam.Performative
     * @return message
     * @throws JAMBehaviourInterruptedException when send fail
     */
    public Message receive(Performative performative) throws JAMBehaviourInterruptedException {
        Message message;
        try {
            message = myMessageBox.readMessage(performative);
        } catch (InterruptedException | JAMMessageBoxException e) {
            throw new JAMBehaviourInterruptedException();
        }
        String logMessage = "RECEIVE message " + message.getPerformative() + " to " + message.getReceiver();
        setChanged();
        notifyObservers(logMessage);
        return message;
    }

    /**
     * Retrieve and delete the first message from message box.
     * If no message found an exception it.francescosantagati.jam.JAMMessageBoxException will be thrown
     *
     * @return message
     * @throws JAMBehaviourInterruptedException when send fail
     */
    public Message receive() throws JAMBehaviourInterruptedException {
        Message message;
        try {
            message = myMessageBox.readMessage();
        } catch (InterruptedException | JAMMessageBoxException e) {
            throw new JAMBehaviourInterruptedException();
        }
        String logMessage = "RECEIVE message " + message.getPerformative() + " to " + message.getReceiver();
        setChanged();
        notifyObservers(logMessage);
        return message;
    }

    /**
     * Check if a message with provided agent and performative is in the message box.
     *
     * @param agentID      it.francescosantagati.jam.AgentID
     * @param performative it.francescosantagati.jam.Performative
     * @return True if message found, false otherwise
     */
    public boolean isThereMessage(AgentID agentID, Performative performative) {
        return myMessageBox.isThereMessage(agentID, performative);
    }

    /**
     * Check if a message with provided agent is in the message box.
     *
     * @param agentID      it.francescosantagati.jam.AgentID
     * @return True if message found, false otherwise
     */
    public boolean isThereMessage(AgentID agentID) {
        return myMessageBox.isThereMessage(agentID);
    }

    /**
     * Check if a message with provided performative is in the message box.
     *
     * @param performative it.francescosantagati.jam.Performative
     * @return True if message found, false otherwise
     */
    public boolean isThereMessage(Performative performative) {
        return myMessageBox.isThereMessage(performative);
    }

    /**
     * Check if a message is in the message box.
     *
     * @return True if message found, false otherwise
     */
    public boolean isThereMessage() {
        return myMessageBox.isThereMessage();
    }
}
