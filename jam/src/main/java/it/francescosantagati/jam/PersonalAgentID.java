package it.francescosantagati.jam;

/**
 * Identify an agent by name and category.
 *
 * @author Francesco Santagati
 */
public class PersonalAgentID extends CategoryAgentID implements AgentID {

    /**
     * Construct a personal agent id.
     *
     * @param n Name
     * @param c Category
     */
    public PersonalAgentID(String n, String c) {
        super(c);
        name = n;
    }

    /**
     * Compare two agents.
     *
     * @param agentID Agent to compare
     * @return True if agent have same nme and category. False otherwise.
     */
    @Override
    public boolean equals(AgentID agentID) {
        return (agentID.getName().equals(name) || agentID.getName().equals(GENERIC_VALUE))
                && (agentID.getCategory().equals(category) || agentID.getCategory().equals(GENERIC_VALUE));
    }

    /**
     * Format agent object to be printed.
     * @return (name, category)
     * <p><code>Ex. "(Berlin, Time Provider)"</code></p>
     */
    @Override
    public String toString() {
        return "(" + name + ", " + category + ")";
    }
}
