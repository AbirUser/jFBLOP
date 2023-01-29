/**
 *
 * @author abir
 */
package blp.core;

import java.util.ArrayList;

public abstract class Problem {

    /**
     * Defines the default precision of binary-coded variables
     */
    private final static int DEFAULT_PRECISSION = 16;

    /**
     * Stores the number of variables of the upper level problem
     */
    protected int numberOfVariablesU_;
    
    /**
     * Stores the number of variables of the lower problem
     */
    protected int numberOfVariablesL_;

    /**
     * Stores the number of objectives of the problem
     */
    protected int numberOfObjectives_;

    /**
     * Stores the number of constraints of the problem
     */
    protected int numberOfConstraints_;

    /**
     * Stores the type of the upperlevel solutions of the problem
     */
    protected SolutionType solutionType_;

    /**
     * Stores the type of the lowerlevel solutions of the problem
     */
    protected SolutionType lowerlevelSolutionType_;

    /**
     * Stores the problem name
     */
    protected String problemName_;

    /**
     * Stores the lower bound values for each encodings.variable (upperlevel)
     * (only if needed)
     */
    protected Object[] lowerLimitU_;

    /**
     * Stores the upper bound values for each encodings.variable (upperlevel)
     * (only if needed)
     */
    protected Object[] upperLimitU_;

    /**
     * Stores the lower bound values for each encodings.variable (lowerlevel)
     * (only if needed)
     */
    protected Object[] lowerLimitL_;

    /**
     * Stores the upper bound values for each encodings.variable (lowerlevel)
     * (only if needed)
     */
    protected Object[] upperLimitL_;
    
    /**
     * Stores the problem type maximization or minimization
     * 0 if minimization 
     * 1 if maximization
     * By default is minimization
     * (only if needed)
     */
    protected int compareType_ = 0;

    /**
     * Stores the number of bits used by binary-coded variables (e.g.,
     * BinaryReal variables). By default, they are initialized to
     * DEFAULT_PRECISION)
     */
    private int[] precision_;

    /**
     *
     *
     */
    protected ArrayList<Variable> decisionVariablesDomain;
    
    protected ArrayList<Variable> decisionVariablesDomainLowerLevel_;

    /**
     * Stores the length of each encodings.variable when applicable (e.g.,
     * Binary and Permutation variables)
     */
    protected int[] length_;

    /**
     * Constructor.
     */
    public Problem() {

    }

    /**
     * Constructor.
     *
     * @param problemName
     */
    public Problem(String problemName) {
        this.problemName_ = problemName;
    } // Problem

    /**
     * Gets the number of decision variables of the upper level problem.
     *
     * @return the number of decision variables.
     */
    public int getUpperLevelNumberOfVariables() {
       
        return numberOfVariablesU_;
    } // getNumberOfVariables
    
    /**
     * Gets the number of decision variables of the lower level problem.
     *
     * @return the number of decision variables.
     */
    public int getLowerLevelNumberOfVariables() {
       
        return numberOfVariablesL_;
    } // getNumberOfVariables

    /**
     * Sets the number of decision variables of the problem.
     *
     * @param numberOfVariables
     */
    public void setUpperLevelNumberOfVariables(int numberOfVariables) {
        numberOfVariablesU_ = numberOfVariables;
    } // getNumberOfVariables
    
    /**
     * Sets the number of decision variables of the problem.
     *
     * @param numberOfVariables
     */
    public void setLowerLevelNumberOfVariables(int numberOfVariables) {
        numberOfVariablesL_ = numberOfVariables;
    } // getNumberOfVariables

    /**
     * Gets the the number of objectives of the problem.
     *
     * @return the number of objectives.
     */
    public int getNumberOfObjectives() {
        return numberOfObjectives_;
    } // getNumberOfObjectives

    /**
     * Gets the lower bound of the ith encodings.variable of the problem.
     *
     * @param i The index of the encodings.variable.
     * @return The lower bound.
     */
    public Object getUpperLevelLowerLimit(int i) {
        return lowerLimitU_[i];
    } // getLowerLimit

    /**
     * Gets the upper bound of the ith encodings.variable of the problem.
     *
     * @param i The index of the encodings.variable.
     * @return The upper bound.
     */
    public Object getUpperLevelUpperLimit(int i) {
        return upperLimitU_[i];
    } // getUpperLimit 
    
    /**
     * Gets the lower bound of the ith encodings.variable of the problem.
     *
     * @param i The index of the encodings.variable.
     * @return The lower bound.
     */
    public Object getLowerLevelLowerLimit(int i) {
        
        return lowerLimitL_[i];
    } // getLowerLimit

    /**
     * Gets the upper bound of the ith encodings.variable of the problem.
     *
     * @param i The index of the encodings.variable.
     * @return The upper bound.
     */
    public Object getLowerLevelUpperLimit(int i) {
        return upperLimitL_[i];
    } // getUpperLimit 

    /**
     * Evaluates a upper level <code>Solution</code> object.
     *
     * @param upperSolution The <code>Upper Level Solution</code> to evaluate.
     * @param lowerSolution The <code>Lower Level Solution</code> to evaluate.
     */
    public abstract void evaluate(Solution upperSolution, Solution lowerSolution);

    /**
     * Evaluates a lower level <code>Solution</code> object.
     *
     * @param solution The <code>Solution</code> to evaluate.
     */
    public abstract void evaluateLowerLevel(Solution upperSolution, Solution lowerSolution);

    /**
     * Gets the number of side constraints in the problem.
     *
     * @return the number of constraints.
     */
    public int getNumberOfConstraints() {
        return numberOfConstraints_;
    } // getNumberOfConstraints

    /**
     * Evaluates the overall constraint violation of a <code>Solution</code>
     * object.
     *
     * @param solution The <code>Solution</code> to evaluate.
     */
    public abstract void evaluateUpperLevelConstraints(Solution solution);
    
    /**
     * Evaluates the overall constraint violation of a <code>Solution</code>
     * object.
     *
     * @param solution The <code>Solution</code> to evaluate.
     */
    public abstract void evaluateLowerLevelConstraints(Solution solution);

    /**
     * Returns the number of bits that must be used to encode binary-real
     * variables
     *
     * @param var
     * @return the number of bits.
     */
    public int getPrecision(int var) {
        return precision_[var];
    } // getPrecision

    /**
     * Returns array containing the number of bits that must be used to encode
     * binary-real variables.
     *
     * @return the number of bits.
     */
    public int[] getPrecision() {
        return precision_;
    } // getPrecision

    /**
     * Sets the array containing the number of bits that must be used to encode
     * binary-real variables.
     *
     * @param precision The array
     */
    public void setPrecision(int[] precision) {
        precision_ = precision;
    } // getPrecision

    public void setUpperLevelSolutionType(SolutionType type) {
        solutionType_ = type;
    } // setSolutionType

    /**
     * Returns the type of the variables of the problem.
     *
     * @return type of the variables of the problem.
     */
    public SolutionType getUpperLevelSolutionType() {
        return solutionType_;
    } // getSolutionType

    public void setLowerLevelSolutionType(SolutionType type) {
        solutionType_ = type;
    } // setSolutionType

    /**
     * Returns the type of the variables of the problem.
     *
     * @return type of the variables of the problem.
     */
    public SolutionType getLowerLevelSolutionType() {
        return lowerlevelSolutionType_;
    } // getSolutionType

    /**
     * Returns the length of the encodings.variable.
     *
     * @param var
     * @return the encodings.variable length.
     */
    public int getLength(int var) {
        if (length_ == null) {
            return DEFAULT_PRECISSION;
        }
        return length_[var];
    }

    /**
     * Returns the problem name
     *
     * @return The problem name
     */
    public String getName() {
        return problemName_;
    }

    /**
     * Returns the number of bits of the solutions of the problem
     *
     * @return The number of bits solutions of the problem
     */
    public int getNumberOfBits() {
        int result = 0;
        /*for (int var = 0; var < numberOfVariables_; var++) {
            result += getLength(var);
        }*/
        return result;
    } // getNumberOfBits();

    public ArrayList<Variable> getDecisionVariablesAndDomainUpperLevel() {
        return decisionVariablesDomain;
    }
    
    public ArrayList<Variable> getDecisionVariablesAndDomainLowerLevel() {
        return decisionVariablesDomainLowerLevel_;
    }

    public void setDecisionVariablesDomain(ArrayList decisionVariablesDomain) {
        this.decisionVariablesDomain = decisionVariablesDomain;
    }
    
    public boolean isMaximize() {
        return (compareType_ == 1);
    }
    
    public boolean isMinimize() {
        return (compareType_ == 0);
    }
}
