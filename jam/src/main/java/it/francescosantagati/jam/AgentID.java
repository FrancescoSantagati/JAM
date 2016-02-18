package it.francescosantagati.jam;

import java.io.Serializable;

/**
 * Interface definition for an it.francescosantagati.jam.AgentID instance.
 *
 * @author Francesco Santagati
 */
public interface AgentID extends Serializable {

    String GENERIC_VALUE = "";

    /**
     * Compare two agents.
     *
     * @param agentID Agent to compare
     * @return True if agent have same name and category. False otherwise.
     */
    boolean equals(AgentID agentID);

    /**
     * Provide agent name.
     *
     * @return agent name
     */
    String getName();

    /**
     * Provide agent category.
     *
     * @return agent category
     */
    String getCategory();

    /**
     * Format agent object to be printed.
     *
     * @return agent in string format
     */
    String toString();
}
