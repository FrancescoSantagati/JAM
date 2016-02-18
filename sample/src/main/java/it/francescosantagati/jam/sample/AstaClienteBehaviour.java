package it.francescosantagati.jam.sample;

import it.francescosantagati.jam.*;

/**
 * Behaviour that simulate customer actions in an auctioneer context
 *
 * @author Francesco Santagati
 */
public class AstaClienteBehaviour extends JAMWhileBehaviour {

    private Offerta bestOffer;
    private boolean retired = false;

    public AstaClienteBehaviour(JAMAgent myAgent) {
        super(myAgent);
    }

    @Override
    public void setup() throws JAMBehaviourInterruptedException {
        ((AstaClienteAgent) myAgent).initWallet();
    }

    @Override
    public void action() throws JAMBehaviourInterruptedException {

        CategoryAgentID auctioneer = new CategoryAgentID("Auctioneer");
        AstaClienteAgent me = ((AstaClienteAgent) myAgent);

        try {

            if (me.getWallet() <= 0) {
                Log.agent(me, "Sono al VERDE. MI RITIRO.");
                done();
            }

            if( ! retired) {
                Log.agent(me, "Nel PORTAFOGLIO ho " + me.getWallet() + " euro.");
            }

            sendQueryOffer(auctioneer);

            Message answerInform = myAgent.receive(auctioneer);

            if (answerInform.getPerformative() == Performative.INFORM) {

                bestOffer = (Offerta) answerInform.getExtraArgument();
//                Log.agent(me, "AGGIORNAMENTO1 migliore offerta:  " + bestOffer.getValue() + " di " + bestOffer.getAgentID());

                // SE HO I SOLDI PER RILANCIARE E NON SONO IL MIGLIORE OFFERENTE
                if(bestOffer.getValue() > me.getWallet() && ! bestOffer.getAgentID().equals(me.getMyID())) {

                    if( ! retired) {
                        Log.agent(me, "Non posso permettermi la cifra di " + bestOffer.getValue() + " euro. MI RITIRO.");
                        retired = true;
                    }
                }
                else if ( ! bestOffer.getAgentID().equals(me.getMyID())) {


                    Offerta newOffer = me.getNewOffer(bestOffer);
                    Log.agent(me, "RILANCIO di " + newOffer.getValue() + " euro");
                    sendNewOffer(auctioneer, newOffer);

                    Message answerAccepted = myAgent.receive(auctioneer);
                    if (answerAccepted.getPerformative() == Performative.INFORM) {

                        Log.agent(me, "La mia offerta di " + newOffer.getValue() + " euro e' stata ACCETTATA!");

                        // Mi riposo qualche secondo
                        sleep(5000);

                        // CONTROLLO DI ESSERE ANCORA IO IL MIGLIORE OFFERENTE
                        sendQueryOffer(auctioneer);
                        Message answerInform2 = myAgent.receive(auctioneer, Performative.INFORM);
                        bestOffer = (Offerta) answerInform2.getExtraArgument();
//                        Log.agent(me, "AGGIORNAMENTO2 migliore offerta:  " + bestOffer.getValue() + " di " + bestOffer.getAgentID());

                    } else {
                        Log.agent(me, "La mia offerta e' troppo bassa. RIPROVO");
                    }
                }
                else {
                    Log.agent(me, "Sono ancora io il migliore offerente.");
                }

            } else {
                // ASTA CONCLUSA
                Log.agent(me, "L'asta si e' CONCLUSA.");
                done();
            }
        } catch (JAMADSLException e) {
            e.printStackTrace();
        }

        sleep(3000);
    }

    @Override
    public void dispose() throws JAMBehaviourInterruptedException {
        Log.agent(myAgent, "L'asta si e' conclusa con con la cifra di " + bestOffer.getValue() + " euro" +
                " e se l'e' aggiudicata " + bestOffer.getAgentID());

        if (bestOffer.getAgentID().equals(myAgent.getMyID())) {
            Log.agent(myAgent, "VITTORIA!");
        }
    }

    private void sendQueryOffer(AgentID agentID) throws JAMADSLException, JAMBehaviourInterruptedException {
        Message request = new Message(
                myAgent.getMyID(),
                agentID,
                Performative.QUERY_IF,
                "Valore corrente?"
        );
        myAgent.send(request);
    }

    private void sendNewOffer(AgentID agentID, Offerta offer) throws JAMADSLException, JAMBehaviourInterruptedException {
        Message request = new Message(
                myAgent.getMyID(),
                agentID,
                Performative.REQUEST,
                Integer.toString(offer.getValue())
        );
        myAgent.send(request);
    }
}
