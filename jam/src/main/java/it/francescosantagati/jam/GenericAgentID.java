package it.francescosantagati.jam;

/**
 * Identify an agent by name and category.
 *
 * @author Francesco Santagati
 */
public class GenericAgentID implements AgentID {

    /**
     * Agent Name
     */
    protected String name;

    /**
     * Agent Category
     */
    protected String category;

    public GenericAgentID() {
        name = GENERIC_VALUE;
        category = GENERIC_VALUE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCategory() {
        return category;
    }

    /**
     * Compare two agents.
     */
    @Override
    public boolean equals(AgentID agentID) {
        return true;
    }

    /**
     * Format agent object to be printed.
     * return always "Generic Agent"
     */
    @Override
    public String toString() {
        return "Generic Agent";
    }
}
