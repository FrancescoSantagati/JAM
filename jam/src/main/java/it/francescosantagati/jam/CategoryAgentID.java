package it.francescosantagati.jam;

/**
 * Identify an agent by category.
 *
 * @author Francesco Santagati
 */
public class CategoryAgentID extends GenericAgentID implements AgentID {

    /**
     * Construct a category agent id.
     *
     * @param c Category
     */
    public CategoryAgentID(String c) {
        name = GENERIC_VALUE;
        category = c;
    }

    /**
     * Compare two agents.
     *
     * @param agentID Agent to compare
     * @return True if agent have same category. False otherwise.
     */
    @Override
    public boolean equals(AgentID agentID) {
        return agentID.getCategory().equals(this.getCategory())
                || agentID.getCategory().equals(GENERIC_VALUE);
    }

    /**
     * Format agent object to be printed.
     * @return (category)
     * <p>
     * Ex. "(Time Provider)"
     * </p>
     */
    @Override
    public String toString() {
        return "( , " + getCategory() + ")";
    }
}