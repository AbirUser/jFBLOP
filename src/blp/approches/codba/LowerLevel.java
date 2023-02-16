package blp.approches.codba;

import blp.core.Algorithm;
import blp.core.Operator;
import blp.core.Problem;
import blp.core.Solution;
import blp.core.SolutionSet;
import blp.util.BLPException;
import blp.util.PseudoRandom;
import java.util.ArrayList;
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
    private int popSize;
    private int maxFE;
    private int numberOfGenerationSubPopulation;
    private int p;
    private Operator selectionOp;
    private Operator crossoverOp;
    private Operator mutationOp;
    private Operator neighborhoodOp;
    private Operator coEvolutionOp;

    private double directRationality = 0;
    private double weightedRationality = 0.0;
    private int LFE = 0;

    public LowerLevel(Problem problem) {
        super(problem);
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
            numberOfGenerationSubPopulation = (Integer) super.getInputParameter("numberOfGenerationSubPopulation");
            maxFE = (Integer) super.getInputParameter("maxFE");
            p = (Integer) super.getInputParameter("p");

            selectionOp = (Operator) super.getOperator("selection");
            crossoverOp = (Operator) super.getOperator("crossover");
            mutationOp = (Operator) super.getOperator("mutation");
            neighborhoodOp = (Operator) super.getOperator("neighborhood");
            coEvolutionOp = (Operator) super.getOperator("coEvolutionCrossover");

            int numberNoImprovement = 0;
            Solution bestSolution = new Solution(super.getProblem(), "lower");
            if (super.getProblem().isMaximize()) {
                bestSolution.setFitness(Double.NEGATIVE_INFINITY);
            } else {
                bestSolution.setFitness(Double.POSITIVE_INFINITY);
            }
            Solution latestBestSolution = new Solution();

            int numberOfGeneration = 0;
            SolutionSet populationDR = new SolutionSet();
            SolutionSet newPopulationDR = new SolutionSet();
            directRationality = 0.0;
            weightedRationality = 0.0;
            // run Lower Level
            // Step 1: Initilization
            // 1- Generate reference points
            SolutionSet referencePoints = new DSDM(super.getProblem(), p, "lower").getReferencePoints();
            int maxFESubPop = (maxFE / referencePoints.size()) / numberOfGenerationSubPopulation;
            //System.out.println("Reference Points: " + referencePoints.size());
            // 2- Generate Sub populations
            ArrayList<Thread> subPopulations = new ArrayList();
            for (int i = 0; i < referencePoints.size(); i++) {
                SolutionSet subPopulation_i = new SolutionSet(generateSubPopulation(new Solution(referencePoints.get(i)), referencePoints.size()));
                populationDR.add(subPopulation_i);
                subPopulations.add(new ThreadGA(super.getProblem(), subPopulation_i, maxFESubPop));
            }
            //while (numberNoImprovement < maxNoImprovement) {
            while (LFE < maxFE) {
                // STRATING MULTI-THREADING
                //System.out.println("Starting threads...");
                numberOfGeneration++;
                for (Thread subPop : subPopulations) {
                    subPop.start();
                }
                // CHECK IF ALL THREAD IS COMPLETED
                boolean threadsCompleted = false;
                while (!threadsCompleted) {
                    threadsCompleted = true;
                    for (Thread subPop : subPopulations) {
                        threadsCompleted &= ((ThreadGA) subPop).isCompleted();
                    }
                }
                //System.out.println("all threads are completed.");
                // SAVE BEST SOLUTION
                boolean improvement = false;
                for (Thread subPop : subPopulations) {
                    if (bestSolution.compare(((ThreadGA) subPop).getBestSolution(), new SolutionComparator()) == 1) {
                        //if (bestSolution.getFitness() > ((ThreadGA) subPop).getBestSolution().getFitness()) {
                        latestBestSolution.setFitness(bestSolution.getFitness());
                        bestSolution = new Solution(((ThreadGA) subPop).getBestSolution());
                        improvement = true;
                        //directRationality++;
                    }
                }
                if (!improvement) {
                    numberNoImprovement++;
                } else {
                    numberNoImprovement = 0;
                }
                // CROSSOVER
                ArrayList<SolutionSet> newSubPopulations = new ArrayList<>();
                for (Thread subPop : subPopulations) {
                    Solution randomlyBestSolution = ((ThreadGA) subPopulations.get(PseudoRandom.randInt(0, subPopulations.size() - 1))).getBestSolution();
                    SolutionSet offsprings = new SolutionSet();
                    for (int i = 0; i < ((ThreadGA) subPop).getPopulation().size(); i++) {
                        Object[] solutions = {randomlyBestSolution, ((ThreadGA) subPop).getPopulation().get(i)};
                        solutions = coEvolutionOp.execute(solutions);
                        if (solutions.length > 1) {
                            int index = PseudoRandom.randInt(0, solutions.length - 1);
                            offsprings.add((Solution) solutions[index]);
                        } else {
                            offsprings.add((Solution) solutions[0]);
                        }
                    }
                    offsprings.union(((ThreadGA) subPop).getPopulation());
                    offsprings.sort(new SolutionComparator());
                    for (int i = 0; i < offsprings.size(); i++) {
                        if (i >= ((ThreadGA) subPop).getPopulation().size()) {
                            offsprings.remove(i);
                        }
                    }
                    newSubPopulations.add(offsprings);
                }
                newPopulationDR.clear();
                subPopulations.clear();
                subPopulations = new ArrayList<>();
                for (int i = 0; i < newSubPopulations.size(); i++) {
                    subPopulations.add(new ThreadGA(super.getProblem(), newSubPopulations.get(i), maxFESubPop));
                    newPopulationDR.add(newSubPopulations.get(i));
                }
                //weightedRationality += (latestBestSolution.getFitness() / bestSolution.getFitness()) / (subPopulations.size() * (numberOfGenerationSubPopulation + 1));
                //Direct rationality
                for (int i = 0; i < newPopulationDR.size(); i++) {
                    Solution solution = newPopulationDR.get(i);
                    for (int j = 0; j < populationDR.size(); j++) {
                        if (solution.compare(populationDR.get(j), new SolutionComparator()) == 1) {
                            directRationality++;
                            weightedRationality += Math.abs(solution.getFitness() - populationDR.get(j).getFitness());
                        }
                    }
                }
                populationDR.clear();
                populationDR.add(newPopulationDR);
            }
            //---------------------------------------------------
            // Step 3: Return the optimal solution
            SolutionSet result = new SolutionSet();
            result.add(bestSolution);
            directRationality = directRationality / numberOfGeneration;
            weightedRationality = weightedRationality / numberOfGeneration;
            return result;
        } catch (Exception e) {
            System.out.println(e.toString());
            return new SolutionSet();
        }
    }

    public SolutionSet generateSubPopulation(Solution referancePoint, int numberReferencePoints) {
        SolutionSet subPop = new SolutionSet();
        for (int i = 0; i < popSize / numberReferencePoints; i++) {
            Solution solution = new Solution(referancePoint);
            Solution[] solutions = {solution};
            Object[] neighborhoodResult = neighborhoodOp.execute(solutions);
            Solution neighborhood = new Solution((Solution) neighborhoodResult[0]);
            subPop.add(neighborhood);
        }
        return subPop;
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

    private class ThreadGA extends Thread {

        private Problem problem;
        private SolutionSet population;
        private Solution bestSolution;
        private boolean completed;
        private int FE;
        private int limitFE;

        public ThreadGA(Problem problem, SolutionSet population, int maxFE) throws ClassNotFoundException {
            this.problem = problem;
            this.population = population;
            this.bestSolution = new Solution(problem, "lower");
            if (problem.isMaximize()) {
                bestSolution.setFitness(Double.NEGATIVE_INFINITY);
            } else {
                bestSolution.setFitness(Double.POSITIVE_INFINITY);
            }
            FE = 0;
            this.limitFE = maxFE;
        }

        public SolutionSet getPopulation() {
            return population;
        }

        public Solution getBestSolution() {
            return bestSolution;
        }

        public boolean isCompleted() {
            return completed;
        }

        @Override
        public void run() {
            //System.out.println("Running thread " + this.getName() + " ...");
            this.completed = false;
            evalutePopulation(this.population);
            bestSolution = new Solution(population.best(new SolutionComparator()));
            SolutionSet pt = new SolutionSet(this.population);
            /*System.out.println("Initial population:");
             for (int i = 0; i < pt.size(); i++) {
             System.out.println(pt.get(i).toString());
             }*/
            //for (int generation = 0; generation < numberOfGenerationSubPopulation; generation++) {
            while (FE < limitFE) {
                //System.out.println("Generation " + generation);
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
            this.completed = true;
            //System.out.println("best solution is:" + bestSolution.toString());
            //System.out.println("Thread " + this.getName() + " is completed");
        }

        private void evalutePopulation(SolutionSet population) {
            for (int i = 0; i < population.size(); i++) {
                problem.evaluateLowerLevel(upperSolution, population.get(i));
                LFE++;
                FE++;
            }
        }
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
