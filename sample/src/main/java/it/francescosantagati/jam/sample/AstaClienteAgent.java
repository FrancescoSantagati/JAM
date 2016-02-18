package it.francescosantagati.jam.sample;

import it.francescosantagati.jam.JAMADSLException;
import it.francescosantagati.jam.JAMAgent;
import it.francescosantagati.jam.PersonalAgentID;

/**
 * Customer evaluates auctioner offer according to his availability
 * and, if he can, make a raise.
 */
public class AstaClienteAgent extends JAMAgent {

    private int wallet;

    public AstaClienteAgent(PersonalAgentID agentID) throws JAMADSLException {
        super(agentID);
    }

    /**
     * Init customer wallet.
     */
    synchronized public void initWallet() {
        if(wallet != 0) {
            return;
        }
        wallet = (int) (Math.random() * 200);
    }

    /**
     * Get customer wallet.
     *
     * @return wallet
     */
    public int getWallet() {
        return wallet;
    }

    /**
     * Raise a new offer
     *
     * @param bestOffer Current best offer
     * @return A new offer value.
     */
    synchronized public Offerta getNewOffer(Offerta bestOffer) {
        int diff = wallet - bestOffer.getValue();
        int newValue = bestOffer.getValue() + (int) (Math.random() * diff);
        return new Offerta(getMyID(), newValue);
    }

    public static void main(String[] args) throws JAMADSLException {

        AstaClienteAgent cliente1 = new AstaClienteAgent(new PersonalAgentID("Francesco", "Cliente"));
        cliente1.addBehaviour(new AstaClienteBehaviour(cliente1));
        //cliente1.init();
        //cliente1.start();

        AstaClienteAgent cliente2 = new AstaClienteAgent(new PersonalAgentID("Giuseppe", "Cliente"));
        cliente2.addBehaviour(new AstaClienteBehaviour(cliente2));
        //cliente2.init();
        //cliente2.start();

        AstaClienteAgent cliente3 = new AstaClienteAgent(new PersonalAgentID("Giovanni", "Cliente"));
        cliente3.addBehaviour(new AstaClienteBehaviour(cliente3));
        //cliente3.init();
        //cliente3.start();
    }
}
