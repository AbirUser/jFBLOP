package blp.core;

import blp.encodings.variable.ArrayInt;
import blp.encodings.variable.Int;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author abir
 */
public class Solution implements Serializable {

    /**
     * Stores the problem
     */
    private Problem problem_;

    /**
     * Stores the level of solution
     */
    private String level_;

    /**
     * Stores the type of the encodings.variable
     */
    private SolutionType type_;

    /**
     * Stores the decision variables of the solution.
     */
    private Variable[] variable_;
    private int[] variable1_;

    /**
     * Stores the objectives values of the solution.
     */
    private final double[] objective_;

    /**
     * Stores the number of objective values of the solution
     */
    private int numberOfObjectives_;

    /**
     * Stores the so called fitness value. Used in some metaheuristics
     */
    private double fitness_;

    /**
     * Used in algorithm AbYSS, this field is intended to be used to know when a
     * <code>Solution</code> is marked.
     */
    private boolean marked_;

    /**
     * Stores the so called rank of the solution. Used in NSGA-II
     */
    private int rank_;

    /**
     * Stores the overall constraint violation of the solution.
     */
    private double overallConstraintViolation_;

    /**
     * Stores the number of constraints violated int the upper level by the
     * solution.
     */
    private int numberOfViolatedConstraintsU_;

    /**
     * Stores the number of constraints violated int the lower level by the
     * solution.
     */
    private int numberOfViolatedConstraintsL_;

    /**
     * This field is intended to be used to know the location of a solution into
     * a <code>SolutionSet</code>.
     */
    private int location_;

    /**
     * Stores the distance to his k-nearest neighbor into a
     * <code>SolutionSet</code>.
     */
    private double kDistance_;

    /**
     * Stores the crowding distance of the the solution in a
     * <code>SolutionSet</code>.
     */
    private double crowdingDistance_;

    /**
     * Stores the distance between this solution and a <code>SolutionSet</code>.
     */
    private double distanceToSolutionSet_;

    /**
     * Stores the distance between this solution and a <code>SolutionSet</code>.
     */
    private int directRationality;

    /**
     * Stores the distance between this solution and a <code>SolutionSet</code>.
     */
    private int numHit;

    /**
     * Stores the distance between this solution and a <code>SolutionSet</code>.
     */
    private int minHit;

    /**
     * Stores the distance between this solution and a <code>SolutionSet</code>.
     */
    private double KE;

    /**
     * Stores the distance between this solution and a <code>SolutionSet</code>.
     */
    private int localMin;

    /**
     * Stores the distance between this solution and a <code>SolutionSet</code>.
     */
    private int id;

    /**
     * Stores the distance between this solution and a <code>SolutionSet</code>.
     */
    private int parent1Index;

    /**
     * Stores the distance between this solution and a <code>SolutionSet</code>.
     */
    private int parent2Index;

    /**
     * Stores the distance between this solution and a <code>SolutionSet</code>.
     */
    private int child1Index;

    /**
     * Stores the distance between this solution and a <code>SolutionSet</code>.
     */
    private int child2Index;

    /**
     * Stores the distance between this solution and a <code>SolutionSet</code>.
     */
    private String operationType;

    /**
     * Stores the distance between this solution and a <code>SolutionSet</code>.
     */
    private double weightedRationality;

    /**
     * Stores the distance between this solution and a <code>SolutionSet</code>.
     */
    private double betterFitness;

    /**
     * Constructor.
     */
    public Solution() {
        problem_ = null;
        marked_ = false;
        overallConstraintViolation_ = 0.0;
        numberOfViolatedConstraintsU_ = 0;
        numberOfViolatedConstraintsL_ = 0;
        type_ = null;
        variable_ = null;
        objective_ = null;
        fitness_ = Double.POSITIVE_INFINITY;
    } // Solution

    /**
     * Constructor
     *
     * @param numberOfObjectives Number of objectives of the solution
     *
     * This constructor is used mainly to read objective values from a file to
     * variables of a SolutionSet to apply quality indicators
     */
    public Solution(int numberOfObjectives) {
        numberOfObjectives_ = numberOfObjectives;
        objective_ = new double[numberOfObjectives];
    }

    /**
     * Constructor.
     *
     * @param problem The problem to solve
     * @param level The level of the solution
     * @throws ClassNotFoundException
     */
    public Solution(Variable[] variables) throws ClassNotFoundException {
        problem_ = null;
        level_ = "";
        type_ = null;
        numberOfObjectives_ = variables.length;
        objective_ = new double[numberOfObjectives_];
        // Setting initial values
        fitness_ = fitness_ = Double.POSITIVE_INFINITY;
        kDistance_ = 0.0;
        crowdingDistance_ = 0.0;
        distanceToSolutionSet_ = Double.POSITIVE_INFINITY;
        //variable_ = problem.solutionType_.createVariables() ; 
        variable_ = variables;
    } // Solution

    /**
     * Constructor.
     *
     * @param problem The problem to solve
     * @param level The level of the solution
     * @throws ClassNotFoundException
     */
    public Solution(Problem problem, String level) throws ClassNotFoundException {
        problem_ = problem;
        level_ = level;

        if (level_.equals("upper")) {
            type_ = problem.getUpperLevelSolutionType();

        } else if (level_.equals("lower")) {
            type_ = problem.getLowerLevelSolutionType();

        }
        numberOfObjectives_ = problem.getNumberOfObjectives();

        objective_ = new double[numberOfObjectives_];
        // Setting initial values
        fitness_ = fitness_ = Double.POSITIVE_INFINITY;
        kDistance_ = 0.0;
        crowdingDistance_ = 0.0;
        distanceToSolutionSet_ = Double.POSITIVE_INFINITY;
        //variable_ = problem.solutionType_.createVariables() ; 
        variable_ = type_.createVariables(level);
    } // Solution

    static public Solution getNewSolution(Problem problem, String level) throws ClassNotFoundException {
        return new Solution(problem, level);
    }

    /**
     * Constructor
     *
     * @param problem The problem to solve
     * @param level The level of the solution
     * @param variables
     */
    public Solution(int[] variables) {

        objective_ = new double[0];
        variable1_ = variables;
    } // Constructor

    public Solution(Problem problem, String level, Variable[] variables) {
        problem_ = problem;
        level_ = level;
        if (level_.equals("upper")) {
            type_ = problem.getUpperLevelSolutionType();
        } else if (level_.equals("lower")) {
            type_ = problem.getLowerLevelSolutionType();
        }
        numberOfObjectives_ = problem.getNumberOfObjectives();
        objective_ = new double[numberOfObjectives_];

        // Setting initial values
        fitness_ = fitness_ = Double.POSITIVE_INFINITY;
        kDistance_ = 0.0;
        crowdingDistance_ = 0.0;
        distanceToSolutionSet_ = Double.POSITIVE_INFINITY;
        //<-

        variable_ = variables;
    } // Constructor

    /**
     * Copy constructor.
     *
     * @param solution Solution to copy.
     */
    public Solution(Solution solution) {
        problem_ = solution.problem_;
        level_ = solution.level_;
        type_ = solution.type_;

        numberOfObjectives_ = solution.getNumberOfObjectives();
        objective_ = new double[numberOfObjectives_];
        for (int i = 0; i < objective_.length; i++) {
            objective_[i] = solution.getObjective(i);
        } // for
        //<-

        variable_ = type_.copyVariables(solution.getDecisionVariables());
        overallConstraintViolation_ = solution.getOverallConstraintViolation();
        numberOfViolatedConstraintsU_ = solution.getNumberOfViolatedConstraintUpperLevel();
        numberOfViolatedConstraintsL_ = solution.getNumberOfViolatedConstraintLowerLevel();
        distanceToSolutionSet_ = solution.getDistanceToSolutionSet();
        crowdingDistance_ = solution.getCrowdingDistance();
        kDistance_ = solution.getKDistance();
        fitness_ = solution.getFitness();
        rank_ = solution.getRank();
        directRationality = solution.getDirectRationality();
        weightedRationality = solution.getWeightedRationality();

        numHit = solution.numHit;
        minHit = solution.minHit;
        KE = solution.KE;
        localMin = solution.localMin;
        id = solution.id;
        parent1Index = solution.parent1Index;
        parent2Index = solution.parent2Index;
        child1Index = solution.child1Index;
        child2Index = solution.child2Index;
        operationType = solution.operationType;
        betterFitness = solution.betterFitness;
    } // Solution

    /**
     * Sets the distance between this solution and a <code>SolutionSet</code>.
     * The value is stored in <code>distanceToSolutionSet_</code>.
     *
     * @param distance The distance to a solutionSet.
     */
    public void setDistanceToSolutionSet(double distance) {
        distanceToSolutionSet_ = distance;
    } // SetDistanceToSolutionSet

    /**
     * Gets the distance from the solution to a <code>SolutionSet</code>.
     * <b> REQUIRE </b>: this method has to be invoked after calling
     * <code>setDistanceToPopulation</code>.
     *
     * @return the distance to a specific solutionSet.
     */
    public double getDistanceToSolutionSet() {
        return distanceToSolutionSet_;
    } // getDistanceToSolutionSet

    /**
     * Sets the distance between the solution and its k-nearest neighbor in a
     * <code>SolutionSet</code>. The value is stored in <code>kDistance_</code>.
     *
     * @param distance The distance to the k-nearest neighbor.
     */
    public void setKDistance(double distance) {
        kDistance_ = distance;
    } // setKDistance

    /**
     * Gets the distance from the solution to his k-nearest nighbor in a
     * <code>SolutionSet</code>. Returns the value stored in
     * <code>kDistance_</code>. <b> REQUIRE </b>: this method has to be invoked
     * after calling <code>setKDistance</code>.
     *
     * @return the distance to k-nearest neighbor.
     */
    double getKDistance() {
        return kDistance_;
    } // getKDistance

    public String getlevel() {
        return level_;
    } // getKDistance

    /**
     * Sets the crowding distance of a solution in a <code>SolutionSet</code>.
     * The value is stored in <code>crowdingDistance_</code>.
     *
     * @param distance The crowding distance of the solution.
     */
    public void setCrowdingDistance(double distance) {
        crowdingDistance_ = distance;
    } // setCrowdingDistance

    /**
     * Gets the crowding distance of the solution into a
     * <code>SolutionSet</code>. Returns the value stored in
     * <code>crowdingDistance_</code>.
     * <b> REQUIRE </b>: this method has to be invoked after calling
     * <code>setCrowdingDistance</code>.
     *
     * @return the distance crowding distance of the solution.
     */
    public double getCrowdingDistance() {
        return crowdingDistance_;
    } // getCrowdingDistance

    /**
     * Sets the fitness of a solution. The value is stored in
     * <code>fitness_</code>.
     *
     * @param fitness The fitness of the solution.
     */
    public void setFitness(double fitness) {
        fitness_ = fitness;
    } // setFitness

    /**
     * Gets the fitness of the solution. Returns the value of stored in the
     * encodings.variable <code>fitness_</code>.
     * <b> REQUIRE </b>: This method has to be invoked after calling
     * <code>setFitness()</code>.
     *
     * @return the fitness.
     */
    public double getFitness() {
        return fitness_;
    } // getFitness

    /**
     * Sets the value of the i-th objective.
     *
     * @param i The number identifying the objective.
     * @param value The value to be stored.
     */
    public void setObjective(int i, double value) {
        objective_[i] = value;
    } // setObjective

    /**
     * Returns the value of the i-th objective.
     *
     * @param i The value of the objective.
     * @return
     */
    public double getObjective(int i) {
        return objective_[i];
    } // getObjective

    /**
     * Returns the number of objectives.
     *
     * @return The number of objectives.
     */
    public int getNumberOfObjectives() {
        if (objective_ == null) {
            return 0;
        } else {
            return numberOfObjectives_;
        }
    } // getNumberOfObjectives

    /**
     * Returns the number of decision variables of the solution.
     *
     * @return The number of decision variables.
     */
    public int numberOfVariables() {
        if (level_.equals("upper")) {
            return problem_.getUpperLevelNumberOfVariables();
        } else {
            return problem_.getLowerLevelNumberOfVariables();
        }
    } // numberOfVariables

    /**
     * Returns a string representing the solution.
     *
     * @return The string.
     */
    @Override
    public String toString() {
        String solution = "";
        for (int i = 0; i < this.variable_.length; i++) {
            if (level_.equals("lower")) {
                solution = solution + this.variable_[i].toString() + "\t";
            } else {
                if (ArrayInt.class.isInstance(problem_.getUpperLevelSolutionType())) {
                    try {
                        for (int j = 0; j < this.variable_[i].getLength(); j++) {
                            solution = solution + this.variable_[i].getValue(j) + "\t";
                        }
                        solution = solution + "\n";
                    } catch (Exception ex) {
                        Logger.getLogger(Solution.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (Int.class.isInstance(problem_.getUpperLevelSolutionType())) {
                    solution = solution + this.variable_[i].toString() + "\t";
                }
            }
        }
        solution = solution + "(" + fitness_ + ")";
        return solution;
    } // toString

    /**
     * Returns the decision variables of the solution.
     *
     * @return the <code>DecisionVariables</code> object representing the
     * decision variables of the solution.
     */
    public Variable[] getDecisionVariables() {
        return variable_;
    } // getDecisionVariables

    /**
     * Returns the decision variables of the solution.
     *
     * @param index
     * @return the <code>DecisionVariables</code> object representing the
     * decision variables of the solution.
     */
    public Variable getDecisionVariable(int index) {
        return variable_[index];
    } // getDecisionVariables

    /**
     * Sets the decision variables for the solution.
     *
     * @param variables The <code>DecisionVariables</code> object representing
     * the decision variables of the solution.
     */
    public void setDecisionVariables(Variable[] variables) {
        variable_ = new Variable[variables.length];
        for (int i = 0; i < variables.length; i++) {
            variable_[i] = variables[i].copy();
        }
    } // setDecisionVariabless

    /**
     * Sets the decision variables for the solution.
     *
     * @param variables The <code>DecisionVariables</code> object representing
     * the decision variables of the solution.
     */
    public void setDecisionVariables(ArrayList<Variable> variables) {
        variable_ = new Variable[variables.size()];
        for (int i = 0; i < variable_.length; i++) {
            variable_[i] = variables.get(i).copy();
        }
    } // setDecisionVariabless

    /**
     * Sets the decision variables for the solution.
     *
     * @param index
     * @param variable
     */
    public void setDecisionVariables(int index, Variable variable) {
        //System.out.println("INDEX : " + index + " || SIZE : " + variable_.length);
        variable_[index] = variable.copy();
    } // setDecisionVariables

    public Problem getProblem() {
        return problem_;
    }

    /**
     * Sets the rank of a solution.
     *
     * @param value The rank of the solution.
     */
    public void setRank(int value) {
        this.rank_ = value;
    } // setRank

    /**
     * Gets the rank of the solution.
     * <b> REQUIRE </b>: This method has to be invoked after calling
     * <code>setRank()</code>.
     *
     * @return the rank of the solution.
     */
    public int getRank() {
        return this.rank_;
    } // getRank

    /**
     * Sets the overall constraints violated by the solution.
     *
     * @param value The overall constraints violated by the solution.
     */
    public void setOverallConstraintViolation(double value) {
        this.overallConstraintViolation_ = value;
    } // setOverallConstraintViolation

    /**
     * Gets the overall constraint violated by the solution.
     * <b> REQUIRE </b>: This method has to be invoked after calling
     * <code>overallConstraintViolation</code>.
     *
     * @return the overall constraint violation by the solution.
     */
    public double getOverallConstraintViolation() {
        return this.overallConstraintViolation_;
    }  //getOverallConstraintViolation

    /**
     * Sets the number of constraints violated by the solution.
     *
     * @param value The number of constraints violated by the solution.
     */
    public void setNumberOfViolatedConstraintUpperLevel(int value) {
        this.numberOfViolatedConstraintsU_ = value;
    } //setNumberOfViolatedConstraint

    /**
     * Gets the number of constraint violated by the solution.
     * <b> REQUIRE </b>: This method has to be invoked after calling
     * <code>setNumberOfViolatedConstraint</code>.
     *
     * @return the number of constraints violated by the solution.
     */
    public int getNumberOfViolatedConstraintUpperLevel() {
        return this.numberOfViolatedConstraintsU_;
    } // getNumberOfViolatedConstraint

    /**
     * Sets the number of constraints violated by the solution.
     *
     * @param value The number of constraints violated by the solution.
     */
    public void setNumberOfViolatedConstraintLowerLevel(int value) {
        this.numberOfViolatedConstraintsL_ = value;
    } //setNumberOfViolatedConstraint

    /**
     * Gets the number of constraint violated by the solution.
     * <b> REQUIRE </b>: This method has to be invoked after calling
     * <code>setNumberOfViolatedConstraint</code>.
     *
     * @return the number of constraints violated by the solution.
     */
    public int getNumberOfViolatedConstraintLowerLevel() {
        return this.numberOfViolatedConstraintsL_;
    } // getNumberOfViolatedConstraint

    /**
     * Sets the type of the encodings.variable.
     *
     * @param type The type of the encodings.variable.
     */
    //public void setType(String type) {
    // type_ = Class.forName("") ;
    //} // setType
    /**
     * Sets the type of the encodings.variable.
     *
     * @param type The type of the encodings.variable.
     */
    public void setType(SolutionType type) {
        type_ = type;
    } // setType

    public void setMarked(boolean marked) {
        this.marked_ = marked;
    }

    public boolean isMarked() {
        return this.marked_;
    }

    /**
     * Gets the type of the encodings.variable
     *
     * @return the type of the encodings.variable
     */
    public SolutionType getType() {
        return type_;
    } // getType

    /**
     * Returns the aggregative value of the solution
     *
     * @return The aggregative value.
     */
    public double getAggregativeValue() {
        double value = 0.0;
        for (int i = 0; i < getNumberOfObjectives(); i++) {
            value += getObjective(i);
        }
        return value;
    } // getAggregativeValue

    public int getDirectRationality() {
        return directRationality;
    }

    public double getWeightedRationality() {
        return weightedRationality;
    }

    public void setDirectRationality(int directRationality) {
        this.directRationality = directRationality;
    }

    public void setWeightedRationality(double weightedRationality) {
        this.weightedRationality = weightedRationality;
    }

    /**
     * @param solution the solution to be compared with current solution.
     * @param comparator comparator.
     * @return a negative integer, zero, or a positive integer as the first
     * argument is less than, equal to, or greater than the second.
     */
    public int compare(Solution solution, Comparator comparator) {
        return comparator.compare(this, solution);
    }

    public void checkBetter() {
        if (fitness_ < betterFitness) {
            betterFitness = fitness_;
            minHit = numHit;
        }
    }

    public boolean decCheck(int decThres) {
        return (numHit - minHit) > decThres;
    }

    public boolean synCheck(double synThres) {
        return KE < synThres;
    }

    public int getID() {
        return id;
    }

    public int getNumHit() {
        return numHit;
    }

    public double getKE() {
        return KE;
    }

    public int getParent1Index() {
        return parent1Index;
    }

    public int getParent2Index() {
        return parent2Index;
    }

    public int getChild1Index() {
        return child1Index;
    }

    public int getChild2Index() {
        return child2Index;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setKE(double KE) {
        this.KE = KE;
    }

    public void addHit() {
        this.numHit++;
    }

    public void setID(int id) {
        this.id = id;
    }

    public void setNumHit(int numHit) {
        this.numHit = numHit;
    }

    public void setMinHit(int minHit) {
        this.numHit = minHit;
    }

    public void setParent1Index(int index) {
        parent1Index = index;
    }

    public void setParent2Index(int index) {
        parent2Index = index;
    }

    public void setChild1Index(int index) {
        child1Index = index;
    }

    public void setChild2Index(int index) {
        child2Index = index;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public void setBetterFitness(double betterFitness) {
        this.betterFitness = betterFitness;
    }

    public double sumDiff(Solution solution) throws Exception {
        double sumDiff = 0.0;
        for (int i = 0; i < variable_.length; i++) {
            sumDiff += variable_[i].AbsDiff(solution.getDecisionVariable(i));
        }
        return sumDiff;
    }
}
