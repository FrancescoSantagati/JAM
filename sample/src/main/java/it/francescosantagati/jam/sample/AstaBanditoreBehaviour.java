package it.francescosantagati.jam.sample;

import it.francescosantagati.jam.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Behaviour that simulate auctioneer actions
 *
 * @author Francesco Santagati
 */
public class AstaBanditoreBehaviour extends JAMWhileBehaviour {

    private List<String> customers;

    public AstaBanditoreBehaviour(JAMAgent myAgent) {
        super(myAgent);
        customers = new ArrayList<>();
    }

    /**
     * Setup auction base value.
     *
     * @throws JAMBehaviourInterruptedException when thread is interrupted
     */
    @Override
    public void setup() throws JAMBehaviourInterruptedException {

        ((AstaBanditoreAgent) myAgent).setBaseValue();
        Log.agent(myAgent, "Il prezzo base e' impostato a: " + ((AstaBanditoreAgent) myAgent).getBestOffer().getValue());
    }

    /**
     * Read a message and give a response according with auction status and offer.
     * Every time new offers not found countdown will be decreases.
     *
     * @throws JAMBehaviourInterruptedException when thread is interrupted
     */
    @Override
    public void action() throws JAMBehaviourInterruptedException {

        AstaBanditoreAgent auctioneer = (AstaBanditoreAgent) myAgent;

        Log.agent(myAgent, "Ci sono clienti? " + ! customers.isEmpty());

        // Se è stata assegnata l'offerta e non ci sono clienti -> l'asta si è conclusa
        if(auctioneer.isAwarded() && customers.isEmpty()) {
            Log.agent(myAgent, "L'asta si � conclusa e tutti sono stati notificati");
            done();
        }

        Message msgReceived = myAgent.receive(new CategoryAgentID("Cliente"));
        PersonalAgentID customer = (PersonalAgentID) msgReceived.getSender();

        try {

            // Gestisco messaggi di tipo QUERY_IF
            if (msgReceived.getPerformative() == Performative.QUERY_IF
                    && msgReceived.getContent().equals("Valore corrente?")) {

                Log.agent(myAgent, "RICEVUTA richiesta da parte di " + msgReceived.getSender());

                // Se il cliente non si era ancora presentato, lo aggiunge alla lista locale
                if( ! customers.contains(msgReceived.getSender().toString())) {
                    customers.add(msgReceived.getSender().toString());
                }

                // Se l'offerta è stata aggiudicata, rispondo che l'asta si è conclusa
                if(auctioneer.isAwarded()) {
                    Log.agent(myAgent, "RISPONDO a " + msgReceived.getSender() + " che l'asta si e' CONCLUSA");
                    sendRefuseAwarded(customer);
                    customers.remove(msgReceived.getSender().toString());
                }
                // Comunico l'attuale best offer
                else {
                    Log.agent(myAgent, "RISPONDO a " + msgReceived.getSender() +
                            " che l'offerta CORRENTE e' di " + auctioneer.getBestOffer().getValue());
                    sendInformBestOffer(customer);
                }
            }

            // Gestisco messaggi di tipo REQUEST
            else if (msgReceived.getPerformative() == Performative.REQUEST) {
                int offer = Integer.parseInt(msgReceived.getContent());
                Log.agent(myAgent, "RICEVUTA nuova offerta " + offer + " da parte di " + msgReceived.getSender());

                // Provo ad aggiornare l'offerta se è maggiore dell'attuale
                if (auctioneer.updateNewOffer(customer, offer)) {

                    Log.agent(myAgent, "ACCETTO l'offerta di " + offer + " di " + msgReceived.getSender());
                    auctioneer.increaseCountdown();
                    sendInformOfferAccepted(customer);
                }
                // Rifiuto l'offerta perchè ne esiste già una più alta
                else {

                    Log.agent(myAgent, "RIFIUTO l'offerta di " + offer + " di " + msgReceived.getSender());
                    sendRefuseOffer(customer);
                }
            }

            // Non ho compreso il tipo di richiesta
            else {
                Log.agent(myAgent, "Invio richiesta incomprensibile a " + msgReceived.getSender() +
                        ": " + msgReceived.getContent());
                sendNotUnderstood(customer);
            }
        }
        catch(JAMADSLException e) {
            e.printStackTrace();
        }

        auctioneer.reduceCountdown();
        Log.agent(myAgent, "Mancano " + auctioneer.getCountdown() + " alla fine dell\'asta");
        sleep(1000);
    }

    /**
     * Auction stats.
     *
     * @throws JAMBehaviourInterruptedException when thread is interrupted
     */
    @Override
    public void dispose() throws JAMBehaviourInterruptedException {

        AstaBanditoreAgent auctioneer = (AstaBanditoreAgent) myAgent;
        Log.agent(myAgent, "Asta conclusa! Si e' aggiudicato l'oggetto: " + auctioneer.getBestOffer().getAgentID() + "\n");
        Log.agent(myAgent, "Le offerte sono state:");
        for (Offerta offerta : ((AstaBanditoreAgent) myAgent).getOffersHistory()) {
            Log.agent(myAgent, offerta.getAgentID().toString() + " - Offerta: " + offerta.getValue());
        }
    }

    private void sendRefuseAwarded(AgentID agentID) throws JAMADSLException, JAMBehaviourInterruptedException {
        Offerta bestOffer = ((AstaBanditoreAgent) myAgent).getBestOffer();
        Message request = new Message(
                myAgent.getMyID(),
                agentID,
                Performative.REFUSE,
                "L\'oggetto e' stato aggiudicato a " + bestOffer.getAgentID(),
                bestOffer
        );
        myAgent.send(request);
    }

    private void sendInformBestOffer(AgentID agentID) throws JAMADSLException, JAMBehaviourInterruptedException {
        Offerta bestOffer = ((AstaBanditoreAgent) myAgent).getBestOffer();
        Message request = new Message(
                myAgent.getMyID(),
                agentID,
                Performative.INFORM,
                "La migliore offerta e' di " + bestOffer.getValue() + "�, fatta da " + bestOffer.getAgentID(),
                bestOffer
        );
        myAgent.send(request);
    }

    private void sendInformOfferAccepted(AgentID agentID) throws JAMADSLException, JAMBehaviourInterruptedException {
        Offerta bestOffer = ((AstaBanditoreAgent) myAgent).getBestOffer();
        Message request = new Message(
                myAgent.getMyID(),
                agentID,
                Performative.INFORM,
                "La tua offerta e' stata accettata",
                bestOffer
        );
        myAgent.send(request);
    }

    private void sendRefuseOffer(AgentID agentID) throws JAMADSLException, JAMBehaviourInterruptedException {
        Offerta bestOffer = ((AstaBanditoreAgent) myAgent).getBestOffer();
        Message request = new Message(
                myAgent.getMyID(),
                agentID,
                Performative.REFUSE,
                "La tua offerta e' più bassa dell\'offerta attuale ed e' stata rifiutata",
                bestOffer
        );
        myAgent.send(request);
    }

    private void sendNotUnderstood(AgentID agentID) throws JAMADSLException, JAMBehaviourInterruptedException {
        Message request = new Message(
                myAgent.getMyID(),
                agentID,
                Performative.NOT_UNDERSTOOD,
                "Non ho capito la tua richiesta"
        );
        myAgent.send(request);
    }
}