package blp.approches.codbaIIls;

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
    private int maxFE;
    //private int maxNoImprovement;

    private Operator[] neighborhoodOps;
    private Operator coEvolutionOp;
    private int directRationality = 0;
    private double weightedRationality = 0.0;
    private int LFE = 0;
    private int numberOfSubPopulations = 0;

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
        SolutionSet result = new SolutionSet();
        try {
            if (upperSolution == null) {
                try {
                    throw new BLPException("Upper level solution is not defined");
                } catch (BLPException ex) {
                    Logger.getLogger(LowerLevel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            //maxNoImprovement = (Integer) super.getInputParameter("maxNoImprovementLowerLevel");
            maxFE = (Integer) super.getInputParameter("maxFE");
            int p = (Integer) super.getInputParameter("p");
            String[] neighborhoods = (String[]) super.getInputParameter("neighborhoods");
            neighborhoodOps = new Operator[neighborhoods.length];
            for (int i = 0; i < neighborhoods.length; i++) {
                neighborhoodOps[i] = (Operator) super.getOperator(neighborhoods[i]);
            }
            coEvolutionOp = (Operator) super.getOperator("coEvolutionCrossover");
            int numberNoImprovement = 0;
            Solution bestSolution = new Solution(super.getProblem(), "lower");
            if (super.getProblem().isMaximize()) {
                bestSolution.setFitness(Double.NEGATIVE_INFINITY);
            } else {
                bestSolution.setFitness(Double.POSITIVE_INFINITY);
            }
            Solution latestBestSolution = new Solution(super.getProblem(), "lower");
            SolutionSet referencePoints = new DSDM(super.getProblem(), p, "lower").getReferencePoints();
            numberOfSubPopulations = referencePoints.size();
            int maxFESubPop = maxFE / referencePoints.size();
            ArrayList<Thread> SPThreads = new ArrayList();
            for (int i = 0; i < referencePoints.size(); i++) {
                Solution SPi = new Solution(referencePoints.get(i));
                SPThreads.add(new ThreadVNS(super.getProblem(), SPi, maxFESubPop));
            }
            //while (numberNoImprovement < maxNoImprovement) {
            while (LFE < maxFE) {
                for (Thread Spi : SPThreads) {
                    Spi.start();
                }
                // CHECK IF ALL THREAD IS COMPLETED
                boolean threadsCompleted = false;
                while (!threadsCompleted) {
                    threadsCompleted = true;
                    for (Thread Spi : SPThreads) {
                        threadsCompleted &= ((ThreadVNS) Spi).isCompleted();
                    }
                }
                // SAVE BEST SOLUTION
                boolean improvement = false;
                for (Thread Spi : SPThreads) {
                    if (bestSolution.compare(((ThreadVNS) Spi).getBestSolution(), new SolutionComparator()) == 1) {
                        //if (bestSolution.getFitness() > ((ThreadVNS) Spi).getBestSolution().getFitness()) {
                        latestBestSolution.setFitness(bestSolution.getFitness());
                        bestSolution = new Solution(((ThreadVNS) Spi).getBestSolution());
                        improvement = true;
                        directRationality++;
                    }
                }
                if (!improvement) {
                    numberNoImprovement++;
                } else {
                    numberNoImprovement = 0;
                }
                // CROSSOVER
                int c = 0;
                for (Thread Spi : SPThreads) {
                    int position = c;
                    while (position == c) {
                        position = PseudoRandom.randInt(0, SPThreads.size() - 1);
                    }
                    Solution randomlyBestSolution = ((ThreadVNS) SPThreads.get(position)).getBestSolution();
                    Object[] solutions = {randomlyBestSolution, ((ThreadVNS) Spi).getBestSolution()};
                    solutions = coEvolutionOp.execute(solutions);
                    if (solutions.length > 1) {
                        int index = PseudoRandom.randInt(0, solutions.length - 1);
                        SPThreads.set(c, new ThreadVNS(super.getProblem(), (Solution) solutions[index], maxFESubPop));
                    } else {
                        SPThreads.set(c, new ThreadVNS(super.getProblem(), (Solution) solutions[0], maxFESubPop));
                    }
                    c++;
                }
                weightedRationality += (latestBestSolution.getFitness() / bestSolution.getFitness()) / (SPThreads.size() * (maxFESubPop + 1));
            }
            result.add(bestSolution);

        } catch (Exception e) {
            System.err.println(e.toString());
        }
        return result;
    }

    public int getFE() {
        return LFE;
    }

    public int getDirectRationality() {
        return directRationality;
    }

    public double getWeightedRationality() {
        return weightedRationality;
    }

    public int getNumberOfSubPopulations() {
        return numberOfSubPopulations;
    }

    private class ThreadVNS extends Thread {

        private Problem problem;
        private Solution inputSolution;
        private Solution bestSolution;
        private boolean completed;
        private int FE;
        private int limitFE;

        public ThreadVNS(Problem problem, Solution solution, int maxFE) throws ClassNotFoundException {
            this.problem = problem;
            this.inputSolution = new Solution(solution);
            this.bestSolution = new Solution(problem, "lower");
            if (problem.isMaximize()) {
                bestSolution.setFitness(Double.NEGATIVE_INFINITY);
            } else {
                bestSolution.setFitness(Double.POSITIVE_INFINITY);
            }
            this.FE = 0;
            this.limitFE = maxFE;
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
            evaluteSolution(inputSolution);
            int k = 0;
            while (FE < limitFE) {
                Solution[] parents = new Solution[2];
                parents[0] = new Solution(inputSolution);
                Object[] opResult = neighborhoodOps[k].execute(parents);
                Solution neighborhoodResult = (Solution) opResult[0];
                evaluteSolution(neighborhoodResult);
                if (neighborhoodResult.compare(inputSolution, new SolutionComparator()) == -1) {
                    inputSolution = new Solution(neighborhoodResult);
                } else {
                    if (k < neighborhoodOps.length - 1) {
                        k++;
                    } else {
                        k = 0;
                    }
                }
            }
            if (this.bestSolution.compare(inputSolution, new SolutionComparator()) == 1) {
                //if(this.bestSolution.getFitness() > inputSolution.getFitness()) {
                this.bestSolution = new Solution(inputSolution);
            }
            this.completed = true;
        }

        private void evaluteSolution(Solution solution) {
            problem.evaluateLowerLevel(upperSolution, solution);
            FE++;
            LFE++;
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
