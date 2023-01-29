package blp.approches.repair;

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
public class Repair extends Algorithm {

    private Solution lowerSolution;
    private Solution bestSolution;
    private Solution latestBestSolution;
    private double directRationality = Double.POSITIVE_INFINITY;
    private double weightedRationality = Double.POSITIVE_INFINITY;
    private int UFE = 0;
    private int LFE = 0;

    private int popSize;
    private int maxFE;
    private int maxNoImprovement;

    private Operator selectionOp;
    private Operator crossoverOp;
    private Operator mutationOp;

    public Repair(Problem problem) {
        super(problem);
    }

    @Override
    public SolutionSet execute() {
        SolutionSet result = new SolutionSet();
        try {
            // Upper level
            if (super.getInputParameter("populationSize") == null) {
                try {
                    throw new BLPException("Upper level population size `populationSize` is required");
                } catch (BLPException ex) {
                    Logger.getLogger(Repair.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (super.getInputParameter("maxNoImprovementUpperLevel") == null) {
                try {
                    throw new BLPException("Upper level generation number `maxNoImprovementUpperLevel` is required");
                } catch (BLPException ex) {
                    Logger.getLogger(Repair.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (super.getInputParameter("maxFE") == null) {
                try {
                    throw new BLPException("Upper level generation number `maxFE` is required");
                } catch (BLPException ex) {
                    Logger.getLogger(Repair.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (super.getOperator("selection") == null) {
                try {
                    throw new BLPException("Upper level co-evolution crossover operator `selection` is not defined");
                } catch (BLPException ex) {
                    Logger.getLogger(Repair.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (super.getOperator("crossover") == null) {
                try {
                    throw new BLPException("Upper level co-evolution crossover operator `crossover` is not defined");
                } catch (BLPException ex) {
                    Logger.getLogger(Repair.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (super.getOperator("mutation") == null) {
                try {
                    throw new BLPException("Upper level co-evolution crossover operator `mutation` is not defined");
                } catch (BLPException ex) {
                    Logger.getLogger(Repair.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            // Lower level
            if (super.getInputParameter("lPopulationSize") == null) {
                try {
                    throw new BLPException("Upper level population size `lPopulationSize` is required");
                } catch (BLPException ex) {
                    Logger.getLogger(Repair.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (super.getInputParameter("lMaxNoImprovementLowerLevel") == null) {
                try {
                    throw new BLPException("Upper level generation number `lMaxNoImprovementLowerLevel` is required");
                } catch (BLPException ex) {
                    Logger.getLogger(Repair.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (super.getInputParameter("lMaxFE") == null) {
                try {
                    throw new BLPException("Upper level generation number `lMaxFE` is required");
                } catch (BLPException ex) {
                    Logger.getLogger(Repair.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (super.getOperator("lSelection") == null) {
                try {
                    throw new BLPException("Upper level co-evolution crossover operator `lSelection` is not defined");
                } catch (BLPException ex) {
                    Logger.getLogger(Repair.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (super.getOperator("lCrossover") == null) {
                try {
                    throw new BLPException("Upper level co-evolution crossover operator `lCrossover` is not defined");
                } catch (BLPException ex) {
                    Logger.getLogger(Repair.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (super.getOperator("lMutation") == null) {
                try {
                    throw new BLPException("Upper level co-evolution crossover operator `lMutation` is not defined");
                } catch (BLPException ex) {
                    Logger.getLogger(Repair.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            // variables initialization
            popSize = (Integer) super.getInputParameter("populationSize");
            maxFE = (Integer) super.getInputParameter("maxFE");
            maxNoImprovement = (Integer) super.getInputParameter("maxNoImprovementUpperLevel");

            selectionOp = (Operator) super.getOperator("selection");
            crossoverOp = (Operator) super.getOperator("crossover");
            mutationOp = (Operator) super.getOperator("mutation");

            directRationality = Double.POSITIVE_INFINITY;
            weightedRationality = Double.POSITIVE_INFINITY;
            int numberNoImprovement = 0;
            SolutionSet population;
            bestSolution = new Solution(super.getProblem(), "upper");
            if (super.getProblem().isMaximize()) {
                bestSolution.setFitness(Double.NEGATIVE_INFINITY);
            } else {
                bestSolution.setFitness(Double.POSITIVE_INFINITY);
            }
            latestBestSolution = new Solution(super.getProblem(), "upper");
            // run Repair
            population = generatePopulation();
            evalutePopulation(population);
            SolutionSet pt = new SolutionSet(population);
            int generation = 0;
            while (UFE < maxFE) {
                generation++;
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
                    /*System.out.println("--- Selection:");
                        System.out.println("BinaryTournament  :");
                        System.out.println("Parent 1 ("+index1+") : "+parents[0].toString());
                        System.out.println("Parent 2 ("+index2+") : "+parents[1].toString());*/
                    Object[] selectionResult = selectionOp.execute(parents);
                    if ((int) selectionResult[0] == 1) {
                        index1 = index2;
                    }
                    parent1 = Parents.get(index1);
                    //System.out.println("Selected Parent ("+index1+") : "+parent1.toString());
                    parents[0] = Parents.get(index3);
                    parents[1] = Parents.get(index4);
                    /*System.out.println("BinaryTournament  :");
                        System.out.println("Parent 1 ("+index3+") : "+parents[0].toString());
                        System.out.println("Parent 2 ("+index4+") : "+parents[1].toString());*/
                    selectionResult = selectionOp.execute(parents);
                    if ((int) selectionResult[0] == 1) {
                        index3 = index4;
                    }
                    parent2 = Parents.get(index3);
                    //System.out.println("Selected Parent ("+index3+") : "+parent2.toString());
                    // CROISSSEMENT
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
            }
            //---------------------------------------------------
            // Step 3: Return the optimal solution
            result.add(bestSolution);
            directRationality /= generation;
            weightedRationality /= generation;
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        return result;
    }

    private void evalutePopulation(SolutionSet population) {
        Problem problem = this.getProblem();
        for (int i = 0; i < population.size(); i++) {
            try {
                Solution solution = population.get(i);
                LowerLevel lowerlevel = new LowerLevel(this.getProblem());
                lowerlevel.addUpperSolution(population.get(i));
                HashMap<String, Object> parameters = new HashMap();
                parameters.put("populationSize", getInputParameter("lPopulationSize"));
                parameters.put("maxFE", getInputParameter("lMaxFE"));
                parameters.put("maxNoImprovement", getInputParameter("lMaxNoImprovementLowerLevel"));
                lowerlevel.addInputParameters(parameters);

                HashMap<String, Operator> operators = new HashMap<>();
                operators.put("selection", getOperator("lSelection"));
                operators.put("crossover", getOperator("lCrossover"));
                operators.put("mutation", getOperator("lMutation"));
                lowerlevel.addOperators(operators);
                Solution lowerlevelSolution = new Solution(lowerlevel.execute().get(0));
                problem.evaluate(solution, lowerlevelSolution);
                if(lowerlevel.getDirectRationality() < this.directRationality) {
                    this.directRationality = lowerlevel.getDirectRationality();
                }
                UFE++;
                LFE += lowerlevel.getFE();
                if(lowerlevel.getWeightedRationality() < this.weightedRationality) {
                    this.weightedRationality = lowerlevel.getWeightedRationality();
                }
                this.weightedRationality += lowerlevel.getWeightedRationality();
                if (bestSolution.compare(solution, new SolutionComparator()) == 1) {
                    //if (bestSolution.getFitness() > solution.getFitness()) {
                    latestBestSolution.setFitness(bestSolution.getFitness());
                    bestSolution = new Solution(solution);
                    this.lowerSolution = new Solution(lowerlevelSolution);
                }
            } catch (Exception ex) {
                Logger.getLogger(Repair.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public Solution getLowerSolution() {
        return lowerSolution;
    }

    public double getDirectRationality() {
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

    public SolutionSet generatePopulation() {
        SolutionSet pop = new SolutionSet();
        for (int i = 0; i < popSize; i++) {
            try {
                pop.add(new Solution(this.getProblem(), "upper"));
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Repair.class.getName()).log(Level.SEVERE, null, ex);
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
