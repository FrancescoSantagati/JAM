package it.francescosantagati.jam.sample;

import it.francescosantagati.jam.AgentID;
import it.francescosantagati.jam.JAMADSLException;
import it.francescosantagati.jam.JAMAgent;
import it.francescosantagati.jam.PersonalAgentID;

import java.util.ArrayList;
import java.util.List;

/**
 * Auctioneer evaluates a customers offer received and give a positive feedback is
 * offer is accepted, negative otherwise.
 * Message received can be of two types:
 * <ul>
 *     <li>
 *         <code>QUERY_IF</code>: with "Valore corrente?" inside content.
 *         Auctioner will reply with:
 *         <ul>
 *             <li>
 *                 <code>REFUSE</code>: if object was awarded.
 *             </li>
 *             <li>
 *                 <code>INFORM</code>: with best offer value and bidder name inside content.
 *             </li>
 *         </ul>
 *     </li>
 *     <li>
 *         <code>REQUEST</code>: with offer value inside content. Auctioneer handle offer
 *         and check if has higher value then current one and reply with:
 *         <ul>
 *             <li>
 *                 <code>INFORM</code>: if that offer was accepted (best offer value update)
 *             </li>
 *             <li>
 *                 <code>REFUSE</code>: if that offer was refused because it is lower then current best offer
 *             </li>
 *             <li>
 *                 <code>REFUSE or NOT_UNDERSTOOD</code>: in other cases
 *             </li>
 *         </ul>
 *     </li>
 * </ul>
 *
 * @author Francesco Santagati
 */
public class AstaBanditoreAgent extends JAMAgent {

    private int countdown;
    private List<Offerta> offersHistory = new ArrayList<>();
    private boolean awarded;
    private Offerta bestOffer;

    /**
     * Constructor
     *
     * @param agentID Agent ID
     * @throws JAMADSLException if fail to connect
     */
    public AstaBanditoreAgent(PersonalAgentID agentID) throws JAMADSLException {
        super(agentID);

        countdown = 10;
        awarded = false;
    }

    /**
     * Object has been awarded.
     *
     * @return True if object has been awarded, False otherwise.
     */
    public boolean isAwarded() {
        return awarded;
    }

    /**
     * Get current best offer.
     *
     * @return current best offer
     */
    public Offerta getBestOffer() {
        return bestOffer;
    }

    /**
     * Get offers history.
     *
     * @return Offers list
     */
    public List<Offerta> getOffersHistory() {
        return offersHistory;
    }

    /**
     * Get current countdown
     *
     * @return countdown
     */
    public int getCountdown() {
        return countdown;
    }

    /**
     * Set auction base value
     */
    public void setBaseValue() {
        int value = ((int) (Math.random() * 3)) + 1;
        bestOffer = new Offerta(getMyID(), value);
    }

    /**
     *
     * @param agentID Agent ID
     * @param value new offer value
     * @return True is offer has been updated, False otherwise
     */
    public boolean updateNewOffer(AgentID agentID, int value) {
        if(bestOffer.getValue() < value) {
            bestOffer = new Offerta(agentID, value);
            offersHistory.add(bestOffer);
            return true;
        }
        return false;
    }

    /**
     * Reduce auction countdown
     */
    public void reduceCountdown() {
        if(countdown <= 0) {
            awarded = true;
        }
        else {
            countdown--;
        }
    }

    /**
     * Increase auction countdown
     */
    public void increaseCountdown() {
        if(countdown <= 0) {
            awarded = true;
        }
        else {
            countdown = countdown + 3;
        }
    }

    public static void main(String[] args) throws JAMADSLException {

        AstaBanditoreAgent auctioneer = new AstaBanditoreAgent(new PersonalAgentID("Mario", "Auctioneer"));
        auctioneer.addBehaviour(new AstaBanditoreBehaviour(auctioneer));
        //auctioneer.init();
        //auctioneer.start();
    }
}
