package it.francescosantagati.jam;

import java.io.Serializable;

/**
 * A message that an agent can send to another one.
 * <p>Every message is require:</p>
 * <ul>
 * <li>A sender: who sends the message
 * <li>A recipient: who receives the message
 * <li>A performative: type of message
 * <li>A content: body of the message
 * <li>Extra arguments
 * </ul>
 *
 * @author Francesco Santagati
 */
public class Message implements Serializable {

    private AgentID sender;
    private AgentID receiver;
    private Performative performative;
    private String content;
    private Object extraArgument;

    /**
     * Construct a message with extra arguments
     *
     * @param sender        Sender
     * @param receiver      Receiver
     * @param performative  it.francescosantagati.jam.Performative
     * @param content       Content
     * @param extraArgument Extra arguments
     */
    public Message(AgentID sender, AgentID receiver, Performative performative, String content, Object extraArgument) {
        this(sender, receiver, performative, content);
        this.extraArgument = extraArgument;
    }

    /**
     * Construct a message
     *
     * @param sender       Sender
     * @param receiver     Receiver
     * @param performative it.francescosantagati.jam.Performative
     * @param content      Content
     */
    public Message(AgentID sender, AgentID receiver, Performative performative, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.performative = performative;
        this.content = content;
    }

    /**
     * Empty constructor.
     */
    public Message() {
    }

    /**
     * @return Formatted message to be printed.
     * <p>Example:</p>
     * <p><code>it.francescosantagati.jam.Performative: REQUEST</code></p>
     * <p><code>Sender: (Mario, Rossi)</code></p>
     * <p><code>Receiver: (Giovanni, Bianchi)</code></p>
     * <p><code>Content:</code></p>
     * <p><code>Che ora �?</code></p>
     * <p><code>ExtraArgument:</code></p>
     * <p><code>(arguments)</code></p>
     */
    @Override
    public String toString() {
        String string = "it.francescosantagati.jam.Performative: " + performative +
                "\nSender: " + sender.toString() +
                "\nReceiver: " + receiver.toString() +
                "\nContent:" +
                "\n" + content;

        if (extraArgument != null) {
            string += "\nExtraArgument:" +
                    "\n" + extraArgument.toString();
        }

        return string;
    }


    /**
     * @return extra arguments
     */
    public Object getExtraArgument() {
        return extraArgument;
    }

    /**
     * @param extraArgument Extra argument
     */
    public void setExtraArgument(Object extraArgument) {
        this.extraArgument = extraArgument;
    }

    /**
     * @return content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content Content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return performative
     */
    public Performative getPerformative() {
        return performative;
    }

    /**
     * @param performative it.francescosantagati.jam.Performative
     */
    public void setPerformative(Performative performative) {
        this.performative = performative;
    }

    /**
     * @return sender
     */
    public AgentID getSender() {
        return sender;
    }

    /**
     * @param sender Sender
     */
    public void setSender(AgentID sender) {
        this.sender = sender;
    }

    /**
     * @return receiver
     */
    public AgentID getReceiver() {
        return receiver;
    }

    /**
     * @param receiver Receiver
     */
    public void setReceiver(AgentID receiver) {
        this.receiver = receiver;
    }
}