package blp.approches.codbals;

import blp.core.Algorithm;
import blp.core.Operator;
import blp.core.Problem;
import blp.core.Solution;
import blp.core.SolutionSet;
import blp.util.BLPException;
import blp.util.PseudoRandom;
import java.util.Comparator;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Abir
 */
public class CODBALS extends Algorithm {

    private Solution lowerSolution;
    private Solution bestSolution;
    private Solution latestBestSolution;
    private Solution bestLowerLevelSolution;
    private int directRationality = 0;
    private double weightedRationality = 0.0;
    private int UFE = 0;
    private int LFE = 0;

    private int popSize;
    private int maxFE;
    //private int maxNoImprovement;
    private int p;
    private int numberOfSubPopulations;

    private Operator selectionOp;
    private Operator crossoverOp;
    private Operator mutationOp;

    public CODBALS(Problem problem) {
        super(problem);
    }

    public void addInputParameters(HashMap<String, Object> parameters) {
        super.setInputParameter(parameters);
    }

    public void addOperators(HashMap<String, Operator> operators) {
        super.setOperator(operators);
    }

    @Override
    public SolutionSet execute() {
        SolutionSet result = new SolutionSet();
        try {
            if (super.getInputParameter("populationSize") == null) {
                try {
                    throw new BLPException("Upper population size `populationSize` is required");
                } catch (BLPException ex) {
                    Logger.getLogger(CODBALS.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (super.getInputParameter("maxFE") == null) {
                try {
                    throw new BLPException("Upper level maximum number of function evaluation `maxFE` is required");
                } catch (BLPException ex) {
                    Logger.getLogger(CODBALS.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            /*if (super.getInputParameter("maxNoImprovementUpperLevel") == null) {
            try {
            throw new BLPException("Upper level generation number `maxNoImprovementUpperLevel` is required");
            } catch (BLPException ex) {
            Logger.getLogger(CODBALS.class.getName()).log(Level.SEVERE, null, ex);
            }
            }*/
            if (super.getOperator("selection") == null) {
                try {
                    throw new BLPException("Selection operator `selection` is not defined");
                } catch (BLPException ex) {
                    Logger.getLogger(CODBALS.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (super.getOperator("crossover") == null) {
                try {
                    throw new BLPException("Crossover operator `crossover` is not defined");
                } catch (BLPException ex) {
                    Logger.getLogger(CODBALS.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (super.getOperator("mutation") == null) {
                try {
                    throw new BLPException("Mutation operator `mutation` is not defined");
                } catch (BLPException ex) {
                    Logger.getLogger(CODBALS.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (super.getInputParameter("lMaxFE") == null) {
                try {
                    throw new BLPException("Upper level maximum number of function evaluation `lMaxFE` is required");
                } catch (BLPException ex) {
                    Logger.getLogger(CODBALS.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            /*if (super.getInputParameter("lMaxNoImprovementLowerLevel") == null) {
            try {
            throw new BLPException("Upper level generation number `lMaxNoImprovementLowerLevel` is required");
            } catch (BLPException ex) {
            Logger.getLogger(CODBALS.class.getName()).log(Level.SEVERE, null, ex);
            }
            }*/
            if (super.getInputParameter("lP") == null) {
                try {
                    throw new BLPException("Upper level number of parts `lP` is required");
                } catch (BLPException ex) {
                    Logger.getLogger(CODBALS.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (super.getInputParameter("neighborhoods") == null) {
                try {
                    throw new BLPException("Lower level neighborhoods operations `neighborhoods` is required");
                } catch (BLPException ex) {
                    Logger.getLogger(CODBALS.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (super.getOperator("lCoEvolutionCrossover") == null) {
                try {
                    throw new BLPException("Upper level co-evolution crossover operator `lCoEvolutionCrossover` is not defined");
                } catch (BLPException ex) {
                    Logger.getLogger(CODBALS.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            // variables initialization
            popSize = (Integer) super.getInputParameter("populationSize");
            maxFE = (Integer) super.getInputParameter("maxFE");
            //maxNoImprovement = (Integer) super.getInputParameter("maxNoImprovementUpperLevel");
            selectionOp = (Operator) super.getOperator("selection");
            crossoverOp = (Operator) super.getOperator("crossover");
            mutationOp = (Operator) super.getOperator("mutation");

            SolutionSet population;
            bestSolution = new Solution(super.getProblem(), "upper");
            if (super.getProblem().isMaximize()) {
                bestSolution.setFitness(Double.NEGATIVE_INFINITY);
            } else {
                bestSolution.setFitness(Double.POSITIVE_INFINITY);
            }
            latestBestSolution = new Solution(super.getProblem(), "upper");
            bestLowerLevelSolution = new Solution();
            if (super.getProblem().isMaximize()) {
                bestLowerLevelSolution.setFitness(Double.NEGATIVE_INFINITY);
            } else {
                bestLowerLevelSolution.setFitness(Double.POSITIVE_INFINITY);
            }
            // Step 1: Initilization
            //     1- Generate UpperLevel Population randomly
            population = generatePopulation();
            //System.out.println("UpperLevel Population is genereted successfully");
            //System.out.println("Evalute UpperLevel population....");
            //     2- Execute the lower level optimization problem to identify the optimal lower level solutions
            evalutePopulation(population);
            //System.out.println("UpperLevel population is evaluted successfully");
            // -------------------------------------------------
            //
            SolutionSet pt = new SolutionSet(population);
            // Step 2: Upper Evolution
            int generation = 0;
            while (UFE < maxFE) {
                generation++;
                //System.out.println("Generation (UpperLevel) : " + (UFE) + " / " + maxFE);
                SolutionSet Parents = new SolutionSet(pt);
                SolutionSet offsprings = new SolutionSet();
                int i = 0;
                while (i < (pt.size() / 2)) {
                    // SELECTION
                    int index1 = PseudoRandom.randInt(0, Parents.size() - 1), index2 = index1, index3 = index1, index4 = index1;
                    while (index2 == index1) {
                        index2 = PseudoRandom.randInt(0, Parents.size() - 1);
                    }
                    while (index3 == index1 || index3 == index2) {
                        index3 = PseudoRandom.randInt(0, Parents.size() - 1);
                    }
                    while (index4 == index1 || index4 == index2 || index4 == index3) {
                        index4 = PseudoRandom.randInt(0, Parents.size() - 1);
                    }
                    Solution parent1, parent2;
                    Solution[] parents = new Solution[2];
                    parents[0] = Parents.get(index1);
                    parents[1] = Parents.get(index2);
                    Object[] selectionResult = selectionOp.execute(parents);
                    if ((int) selectionResult[0] == 1) {
                        index1 = index2;
                    }
                    parent1 = Parents.get(index1);
                    parents[0] = Parents.get(index3);
                    parents[1] = Parents.get(index4);
                    selectionResult = selectionOp.execute(parents);
                    if ((int) selectionResult[0] == 1) {
                        index3 = index4;
                    }
                    // CROISSSEMENT
                    parent2 = Parents.get(index3);
                    parents[0] = parent1;
                    parents[1] = parent2;
                    Object[] crossoverResult = crossoverOp.execute(parents);
                    // MUTATION
                    for (int c = 0; c < crossoverResult.length; c++) {
                        Solution[] toMutate = {(Solution) crossoverResult[c]};
                        //System.out.println("Offspring "+(c+1)+" : "+toMutate[0].toString());
                        Object[] mutationResult = mutationOp.execute(toMutate);
                        //System.out.println("Mutated offspring "+(c+1)+" : "+((Solution) mutationResult[0]).toString());
                        Solution mutatedSolution = (Solution) mutationResult[0];
                        //problem.evaluateLowerLevelConstraints(mutatedSolution);
                        offsprings.add(mutatedSolution);
                    }
                    Parents.remove(index1);
                    if (index3 > index1) {
                        index3--;
                    }
                    Parents.remove(index3);
                    i += 2;
                }
                // EVALUATION
                evalutePopulation(offsprings);
                // ENVIRONMENTAL SELECTION
                pt.union(offsprings);
                pt.sort(new SolutionComparator());
                SolutionSet pt1 = new SolutionSet();
                //System.out.println("New population Pt+1:");
                for (int j = 0; j < population.size(); j++) {
                    //problem.evaluateLowerLevelConstraints(pt.get(j));
                    pt1.add(pt.get(j));
                    //System.out.println(pt.get(j).toString());
                    /*if (pt.get(j).getFitness() == 0.0) {
                        System.exit(0);
                        }*/
                }
                if (this.bestSolution.compare(pt.get(0), new SolutionComparator()) == 1) {
                    //if (this.bestSolution.getFitness() > pt.get(0).getFitness()) {
                    this.bestSolution = pt.get(0);
                }
                pt = new SolutionSet(pt1);
                //System.out.println("Environmental selection finish successfully");
                //System.exit(0);
            }
            //---------------------------------------------------
            // Step 3: Return the optimal solution

            result.add(bestSolution);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        return result;
    }

    private void evalutePopulation(SolutionSet population) {
        for (int i = 0; i < population.size(); i++) {
            try {
                LowerLevel lowerlevel = new LowerLevel(super.getProblem());
                lowerlevel.addUpperSolution(population.get(i));
                HashMap<String, Object> parameters = new HashMap();
                //parameters.put("maxNoImprovementLowerLevel", getInputParameter("lMaxNoImprovementLowerLevel"));
                parameters.put("maxFE", getInputParameter("lMaxFE"));
                parameters.put("p", getInputParameter("lP"));
                parameters.put("neighborhoods", getInputParameter("neighborhoods"));
                lowerlevel.addInputParameters(parameters);
                HashMap<String, Operator> operators = new HashMap<>();
                String[] lneighborhoods = (String[]) getInputParameter("neighborhoods");
                for (String lneighborhood : lneighborhoods) {
                    operators.put(lneighborhood, getOperator(lneighborhood));
                }
                operators.put("coEvolutionCrossover", getOperator("lCoEvolutionCrossover"));
                lowerlevel.addOperators(operators);

                //System.out.println("Execute lowerlevel...");
                Solution lowerlevelSolution = new Solution(lowerlevel.execute().get(0));
                //System.out.println("End lowerlevel. ");
                //System.out.println("Best lowerlevel solution ==> " + lowerlevelSolution.toString());
                //System.out.println("DirectRationality        ==> " + lowerlevelSolution.getDirectRationality());
                //System.out.println("WeightedRationality      ==> " + lowerlevelSolution.getWeightedRationality());
                //System.exit(0);
                if (bestLowerLevelSolution.compare(lowerlevelSolution, new SolutionComparator()) == 1) {
                    //if(bestLowerLevelSolution.getFitness() > lowerlevelSolution.getFitness()) {
                    bestLowerLevelSolution = new Solution(lowerlevelSolution);
                }
                super.getProblem().evaluate(population.get(i), lowerlevelSolution);
                this.directRationality = lowerlevel.getDirectRationality();
                UFE++;
                LFE += lowerlevel.getFE();
                numberOfSubPopulations = lowerlevel.getNumberOfSubPopulations();
                this.weightedRationality = lowerlevel.getWeightedRationality();
                if (bestSolution.compare(population.get(i), new SolutionComparator()) == 1) {
                    //if (bestSolution.getFitness() > population.get(i).getFitness()) {
                    bestSolution = new Solution(population.get(i));
                    this.lowerSolution = new Solution(lowerlevelSolution);
                }
            } catch (Exception ex) {
                Logger.getLogger(CODBALS.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public Solution getLowerSolution() {
        return lowerSolution;
    }

    public int getDirectRationality() {
        return directRationality;
    }

    public double getWeightedRationality() {
        return weightedRationality;
    }

    public int getUpperFE() {
        return UFE;
    }

    public int getLowerFE() {
        return LFE;
    }

    public int getNumberOfSubPopulations() {
        return numberOfSubPopulations;
    }

    public SolutionSet generatePopulation() {
        SolutionSet pop = new SolutionSet();
        for (int i = 0; i < popSize; i++) {
            try {
                pop.add(new Solution(this.getProblem(), "upper"));
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(CODBALS.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return pop;
    }

    class SolutionComparator implements Comparator<Solution> {

        @Override
        public int compare(Solution s1, Solution s2) {
            if (s2.getProblem().isMaximize()) {
                if (s1.getFitness() == s2.getFitness()) {
                    return 0;
                } else if (s1.getFitness() < s2.getFitness()) {
                    return 1;
                } else {
                    return -1;
                }
            } else {
                if (s1.getFitness() == s2.getFitness()) {
                    return 0;
                } else if (s1.getFitness() > s2.getFitness()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        }
    }
}
