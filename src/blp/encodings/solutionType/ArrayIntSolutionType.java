/**
 *
 * @author abir
 */
package blp.encodings.solutionType;

import blp.core.Problem;
import blp.core.SolutionType;
import blp.core.Variable;
import blp.encodings.variable.ArrayInt;

/**
 * Class representing the solution type of solutions composed of an ArrayInt
 * encodings.variable
 */
public class ArrayIntSolutionType extends SolutionType {

    /**
     * Constructor
     *
     * @param problem Problem being solved
     */
    public ArrayIntSolutionType(Problem problem) {
        super(problem);
    }

    /**
     * Creates the variables of the solution
     *
     * @return
     */
    @Override
    public Variable[] createVariables(String level) {
        Variable[] variables = new Variable[1];
        if(level.equals("upper")) {
            variables[0] = new ArrayInt(problem_.getUpperLevelNumberOfVariables(), level, problem_);
        } else {
            variables[0] = new ArrayInt(problem_.getLowerLevelNumberOfVariables(), level, problem_);
        }
        
        return variables;
    } // createVariables

    /**
     * Copy the variables
     *
     * @param vars Variables to copy
     * @return An array of variables
     */
    @Override
    public Variable[] copyVariables(Variable[] vars) {
        Variable[] variables;

        variables = new Variable[1];
        variables[0] = vars[0].copy();

        return variables;
    } // copyVariables
} // ArrayIntSolutionType
