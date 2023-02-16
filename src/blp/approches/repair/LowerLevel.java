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
public class LowerLevel extends Algorithm {

    private Solution upperSolution = null;
    private Solution bestSolution;
    private Solution latestBestSolution;
    private int popSize;
    private int maxFE;
    private int maxNoImprovement;
    private Operator selectionOp;
    private Operator crossoverOp;
    private Operator mutationOp;

    private double directRationality = 0;
    private double weightedRationality = 0.0;
    private int LFE = 0;

    public LowerLevel(Problem problem) throws ClassNotFoundException {
        super(problem);
        upperSolution = null;
    }

    public void addUpperSolution(Solution upperSolution) {
        this.upperSolution = upperSolution;
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
            if (upperSolution == null) {
                try {
                    throw new BLPException("Upper level solution is not defined");
                } catch (BLPException ex) {
                    Logger.getLogger(LowerLevel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            // variables initialization
            popSize = (Integer) super.getInputParameter("populationSize");
            maxFE = (Integer) super.getInputParameter("maxFE");
            maxNoImprovement = (Integer) super.getInputParameter("maxNoImprovement");

            selectionOp = (Operator) super.getOperator("selection");
            crossoverOp = (Operator) super.getOperator("crossover");
            mutationOp = (Operator) super.getOperator("mutation");

            SolutionSet populationDR = new SolutionSet();
            SolutionSet newPopulationDR = new SolutionSet();
            directRationality = 0.0;
            weightedRationality = 0.0;
            int numberNoImprovement = 0;
            SolutionSet population;
            bestSolution = new Solution(super.getProblem(), "lower");
            if (super.getProblem().isMaximize()) {
                bestSolution.setFitness(Double.NEGATIVE_INFINITY);
            } else {
                bestSolution.setFitness(Double.POSITIVE_INFINITY);
            }
            latestBestSolution = new Solution(super.getProblem(), "lower");
            // run Lower Level
            population = generatePopulation();
            evalutePopulation(population);
            bestSolution = new Solution(population.best(new SolutionComparator()));
            SolutionSet pt = new SolutionSet(population);
            populationDR.add(population);
            //for (int generation = 0; generation < maxFE; generation++) {
            int generation = 0;
            while (LFE < maxFE) {
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
                    this.bestSolution = new Solution(pt.get(0));
                    //directRationality++;
                }
                pt = new SolutionSet(pt1);
                newPopulationDR.clear();
                newPopulationDR.add(pt1);
                for (int c = 0; c < newPopulationDR.size(); c++) {
                    Solution solution = newPopulationDR.get(c);
                    for (int k = 0; k < populationDR.size(); k++) {
                        if (solution.compare(populationDR.get(k), new SolutionComparator()) == 1) {
                            directRationality++;
                            weightedRationality += Math.abs(solution.getFitness() - populationDR.get(k).getFitness());
                        }
                    }
                }
                populationDR.clear();
                populationDR.add(newPopulationDR);
                //weightedRationality += (latestBestSolution.getFitness() / bestSolution.getFitness()) / (population.size() * (generation + 1));
            }
            //---------------------------------------------------
            // Step 3: Return the optimal solution
            result.add(bestSolution);
            directRationality = directRationality / generation;
            weightedRationality = weightedRationality / generation;
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return result;
    }

    public SolutionSet generatePopulation() {
        SolutionSet pop = new SolutionSet();
        for (int i = 0; i < popSize; i++) {
            try {
                pop.add(new Solution(this.getProblem(), "lower"));
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Repair.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return pop;
    }

    private void evalutePopulation(SolutionSet population) {
        for (int i = 0; i < population.size(); i++) {
            this.getProblem().evaluateLowerLevel(upperSolution, population.get(i));
            LFE++;
        }
    }

    public double getDirectRationality() {
        return directRationality;
    }

    public double getWeightedRationality() {
        return weightedRationality;
    }

    public int getFE() {
        return LFE;
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
