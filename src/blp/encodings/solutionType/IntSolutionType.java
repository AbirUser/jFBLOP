/**
 *
 * @author abir
 */
package blp.encodings.solutionType;

import blp.core.Problem;
import blp.core.SolutionType;
import blp.core.Variable;
import blp.encodings.variable.Int;

/**
 * Class representing the solution type of solutions composed of Int variables
 */
public class IntSolutionType extends SolutionType {

    /**
     * Constructor
     *
     * @param problem Problem to solve
     */
    public IntSolutionType(Problem problem) {
        super(problem);
    } // Constructor

    /**
     * Creates the variables of the solution
     *
     * @param level
     * @return
     */
    @Override
    public Variable[] createVariables(String level) {
        Variable[] variables = null;
        if (level.equals("lower") || level.equals("upper")) {
            if (level.equals("upper")) {
                variables = new Variable[problem_.getUpperLevelNumberOfVariables()];
            } else {
                variables = new Variable[problem_.getLowerLevelNumberOfVariables()];
            }
            for (int i = 0; i < variables.length; i++) {
                if (level.equals("lower")) {
                    variables[i] = new Int((int) problem_.getLowerLevelLowerLimit(i),
                            (int) problem_.getLowerLevelUpperLimit(i));
                } else {
                    variables[i] = new Int((int) problem_.getUpperLevelLowerLimit(i),
                            (int) problem_.getUpperLevelUpperLimit(i));
                }
            }
        }
        return variables;
    } // createVariables
} // IntSolutionType
