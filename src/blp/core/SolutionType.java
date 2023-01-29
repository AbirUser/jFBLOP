//  SolutionType.java
package blp.core;

/**
 * Abstract class representing solution types, which define the types of the
 * variables constituting a solution
 */
public abstract class SolutionType {

    public final Problem problem_;

    /**
     * Problem to be solved
     */

    /**
     * Constructor
     *
     * @param problem The problem to solve
     */
    public SolutionType(Problem problem) {
        problem_ = problem;
    } // Constructor

    /**
     * Abstract method to create the variables of the solution
     */
    public abstract Variable[] createVariables(String level) throws ClassNotFoundException;

    /**
     * Copies the decision variables
     *
     * @param vars
     * @return An array of variables
     */
    public Variable[] copyVariables(Variable[] vars) {
        Variable[] variables;

        variables = new Variable[vars.length];
        for (int var = 0; var < vars.length; var++) {
            variables[var] = vars[var].copy();
        } // for

        return variables;
    } // copyVariables

} // SolutionType
