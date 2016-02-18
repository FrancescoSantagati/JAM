package it.francescosantagati.jam.sample;

import it.francescosantagati.jam.AgentID;

import java.io.Serializable;

/**
 * A pair of agent and his offer.
 * Allow to send easily an offer to auctioneer with relative agent info.
 *
 * @author Francesco Santagati
 */
public class Offerta implements Serializable {

    private AgentID agentID;
    private int value;

    public Offerta(AgentID agentID, int value) {
        this.agentID = agentID;
        this.value = value;
    }

    public AgentID getAgentID() {
        return agentID;
    }

    public int getValue() {
        return value;
    }

    public boolean equals(Offerta offerta) {
        return offerta.getAgentID().equals(agentID)
                && offerta.getValue() == value;
    }
}
