package blp.encodings.solutionType;

import blp.core.Problem;
import blp.core.SolutionType;
import blp.core.Variable;
import blp.encodings.variable.ArrayInt;

/**
 *
 * @author Abir
 */
public class MatrixIntSolutionType extends SolutionType {
    
    
    private int numberOfLignes;

    /**
     * Constructor
     *
     * @param problem Problem to solve
     * @param numberOfLignes
     */
    public MatrixIntSolutionType(Problem problem, int numberOfLignes) {
        super(problem);
        this.numberOfLignes = numberOfLignes;
    } // Constructor

    /**
     * Creates the variables of the solution
     *
     * @param level
     * @return
     * @throws java.lang.ClassNotFoundException
     */
    @Override
    public Variable[] createVariables(String level) throws ClassNotFoundException {
        Variable[] variables = null;
        if(level.equals("lower") || level.equals("upper")) {
            if(level.equals("upper")) {
                variables = new Variable[numberOfLignes];
                for (int i = 0; i < variables.length; i++) {
                    variables[i] = new ArrayInt(problem_.getUpperLevelNumberOfVariables(), level, problem_);
                }
            }
        }
        return variables;
    }
    
}
