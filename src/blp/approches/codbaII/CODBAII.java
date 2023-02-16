package blp.approches.codbaII;

import blp.core.Algorithm;
import blp.core.Operator;
import blp.core.Problem;
import blp.core.Solution;
import blp.core.SolutionSet;
import blp.util.BLPException;
import blp.util.PseudoRandom;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Abir
 */
public class CODBAII extends Algorithm {

    private Solution lowerSolution;
    private double directRationality;
    private double weightedRationality;
    private int UFE = 0;
    private int LFE = 0;

    private int popSize;
    private int numberOfGenerationSubPopulation;
    private int maxFE;
    private int maxNoImprovement;
    private int p;

    private Operator selectionOp;
    private Operator crossoverOp;
    private Operator mutationOp;
    private Operator neighborhoodOp;
    private Operator coEvolutionOp;

    public CODBAII(Problem problem) {
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
                    Logger.getLogger(CODBAII.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (super.getInputParameter("maxFE") == null) {
                try {
                    throw new BLPException("Upper level maximum number of function evaluation `maxFE` is required");
                } catch (BLPException ex) {
                    Logger.getLogger(CODBAII.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (super.getInputParameter("maxNoImprovementUpperLevel") == null) {
                try {
                    throw new BLPException("Upper level generation number `maxNoImprovementUpperLevel` is required");
                } catch (BLPException ex) {
                    Logger.getLogger(CODBAII.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (super.getInputParameter("numberOfGenerationSubPopulation") == null) {
                try {
                    throw new BLPException("Upper level generation number `numberOfGenerationSubPopulation` is required");
                } catch (BLPException ex) {
                    Logger.getLogger(CODBAII.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (super.getInputParameter("p") == null) {
                try {
                    throw new BLPException("Upper level number of parts `p` is required");
                } catch (BLPException ex) {
                    Logger.getLogger(CODBAII.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (super.getOperator("selection") == null) {
                try {
                    throw new BLPException("Upper level co-evolution crossover operator `selection` is not defined");
                } catch (BLPException ex) {
                    Logger.getLogger(CODBAII.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (super.getOperator("crossover") == null) {
                try {
                    throw new BLPException("Upper level co-evolution crossover operator `crossover` is not defined");
                } catch (BLPException ex) {
                    Logger.getLogger(CODBAII.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (super.getOperator("mutation") == null) {
                try {
                    throw new BLPException("Upper level co-evolution crossover operator `mutation` is not defined");
                } catch (BLPException ex) {
                    Logger.getLogger(CODBAII.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (super.getOperator("neighborhood") == null) {
                try {
                    throw new BLPException("Upper level co-evolution crossover operator `neighborhood` is not defined");
                } catch (BLPException ex) {
                    Logger.getLogger(CODBAII.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (super.getOperator("coEvolutionCrossover") == null) {
                try {
                    throw new BLPException("Upper level co-evolution crossover operator `coEvolutionCrossover` is not defined");
                } catch (BLPException ex) {
                    Logger.getLogger(CODBAII.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            // Lower level
            if (super.getInputParameter("lPopulationSize") == null) {
                try {
                    throw new BLPException("Upper level population size `lPopulationSize` is required");
                } catch (BLPException ex) {
                    Logger.getLogger(CODBAII.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (super.getInputParameter("lMaxFE") == null) {
                try {
                    throw new BLPException("Upper level maximum number of function evaluation `lMaxFE` is required");
                } catch (BLPException ex) {
                    Logger.getLogger(CODBAII.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (super.getInputParameter("lMaxNoImprovementLowerLevel") == null) {
                try {
                    throw new BLPException("Upper level generation number `lMaxNoImprovementLowerLevel` is required");
                } catch (BLPException ex) {
                    Logger.getLogger(CODBAII.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (super.getInputParameter("lNumberOfGenerationSubPopulation") == null) {
                try {
                    throw new BLPException("Upper level generation number `lNumberOfGenerationSubPopulation` is required");
                } catch (BLPException ex) {
                    Logger.getLogger(CODBAII.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (super.getInputParameter("lP") == null) {
                try {
                    throw new BLPException("Upper level number of parts `lP` is required");
                } catch (BLPException ex) {
                    Logger.getLogger(CODBAII.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (super.getOperator("lSelection") == null) {
                try {
                    throw new BLPException("Upper level co-evolution crossover operator `lSelection` is not defined");
                } catch (BLPException ex) {
                    Logger.getLogger(CODBAII.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (super.getOperator("lCrossover") == null) {
                try {
                    throw new BLPException("Upper level co-evolution crossover operator `lCrossover` is not defined");
                } catch (BLPException ex) {
                    Logger.getLogger(CODBAII.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (super.getOperator("lMutation") == null) {
                try {
                    throw new BLPException("Upper level co-evolution crossover operator `lMutation` is not defined");
                } catch (BLPException ex) {
                    Logger.getLogger(CODBAII.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (super.getOperator("lNeighborhood") == null) {
                try {
                    throw new BLPException("Upper level co-evolution crossover operator `lNeighborhood` is not defined");
                } catch (BLPException ex) {
                    Logger.getLogger(CODBAII.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (super.getOperator("lCoEvolutionCrossover") == null) {
                try {
                    throw new BLPException("Upper level co-evolution crossover operator `lCoEvolutionCrossover` is not defined");
                } catch (BLPException ex) {
                    Logger.getLogger(CODBAII.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            // variables initialization
            popSize = (Integer) super.getInputParameter("populationSize");
            numberOfGenerationSubPopulation = (Integer) super.getInputParameter("numberOfGenerationSubPopulation");
            maxFE = (Integer) super.getInputParameter("maxFE");
            maxNoImprovement = (Integer) super.getInputParameter("maxNoImprovementUpperLevel");
            p = (Integer) super.getInputParameter("p");

            selectionOp = (Operator) super.getOperator("selection");
            crossoverOp = (Operator) super.getOperator("crossover");
            mutationOp = (Operator) super.getOperator("mutation");
            neighborhoodOp = (Operator) super.getOperator("neighborhood");
            coEvolutionOp = (Operator) super.getOperator("coEvolutionCrossover");
            
            directRationality = Double.POSITIVE_INFINITY;
            weightedRationality = Double.POSITIVE_INFINITY;

            int numberNoImprovement = 0;
            Solution bestSolution = new Solution(super.getProblem(), "upper");
            if (super.getProblem().isMaximize()) {
                bestSolution.setFitness(Double.NEGATIVE_INFINITY);
            } else {
                bestSolution.setFitness(Double.POSITIVE_INFINITY);
            }
            Solution latestBestSolution = new Solution(super.getProblem(), "upper");
            // run CODBA-II/CRO
            // Step 1: Initilization
            // 1- Generate reference points
            SolutionSet referencePoints = new DSDM(super.getProblem(), p, "upper").getReferencePoints();
            int maxFESubPop = maxFE / referencePoints.size();
            maxFESubPop /= numberOfGenerationSubPopulation;
            //System.out.println("Reference Points: " + referencePoints.size());
            // 2- Generate Sub populations
            ArrayList<Thread> subPopulations = new ArrayList();
            for (int i = 0; i < referencePoints.size(); i++) {
                SolutionSet subPopulation_i = new SolutionSet(generateSubPopulation(new Solution(referencePoints.get(i)), referencePoints.size()));
                subPopulations.add(new ThreadGA(super.getProblem(), subPopulation_i, maxFESubPop));
            }
            int generation = 0;
            //while (numberNoImprovement < maxNoImprovement) {
            while (UFE < maxFE) {
                generation++;
                //System.out.println(numberNoImprovement+"/"+maxNoImprovement);
                // STRATING MULTI-THREADING
                //System.out.println("Starting threads ("+subPopulations.size()+")...");
                for (Thread subPop : subPopulations) {
                    //System.out.println(subPop.getName());
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
                        lowerSolution = ((ThreadGA) subPop).getLowerSolution();
                        //directRationality = ((ThreadGA) subPop).getDirectRationality();
                        //weightedRationality = ((ThreadGA) subPop).getWeightedRationality();
                        improvement = true;
                    }
                }
                if (!improvement) {
                    numberNoImprovement++;
                } else {
                    numberNoImprovement = 0;
                }
                // CROSSOVER
                //System.out.println("Co-evaluation.");
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
                subPopulations.clear();
                subPopulations = new ArrayList<>();
                for (int i = 0; i < newSubPopulations.size(); i++) {
                    subPopulations.add(new ThreadGA(super.getProblem(), newSubPopulations.get(i), maxFESubPop));
                }
                //System.out.println("end co-evaluation.");
            }
            //---------------------------------------------------
            // Step 3: Return the optimal solution
            directRationality /= generation;
            weightedRationality /= generation;
            result.add(bestSolution);
        } catch (Exception e) {
            System.err.println(getStackTrace(e));
        }
        return result;
    }

    private String getStackTrace(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
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

    private class ThreadGA extends Thread {

        private Problem problem;
        private SolutionSet population;
        private Solution bestSolution;
        private boolean completed;
        private int FE;
        private int FElimit;

        private Solution lowerSolution;

        public ThreadGA(Problem problem, SolutionSet population, int maxFE) throws ClassNotFoundException {
            this.problem = problem;
            this.population = population;
            this.bestSolution = new Solution(problem, "upper");
            if (problem.isMaximize()) {
                bestSolution.setFitness(Double.NEGATIVE_INFINITY);
            } else {
                bestSolution.setFitness(Double.POSITIVE_INFINITY);
            }
            this.FElimit = maxFE;
            FE = 0;
        }

        public Solution getLowerSolution() {
            return lowerSolution;
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
            //System.out.println("["+this.getName()+"] is Running...");
            this.completed = false;
            evalutePopulation(this.population);
            SolutionSet pt = new SolutionSet(this.population);
            //for (int generation = 0; generation < numberOfGenerationSubPopulation; generation++) {
            while (FE < FElimit) {
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
        }

        private void evalutePopulation(SolutionSet population) {
            for (int i = 0; i < population.size(); i++) {
                try {
                    Solution solution = population.get(i);
                    LowerLevel lowerlevel = new LowerLevel(problem);
                    lowerlevel.addUpperSolution(population.get(i));
                    HashMap<String, Object> parameters = new HashMap();
                    parameters.put("populationSize", getInputParameter("lPopulationSize"));
                    parameters.put("numberOfGenerationSubPopulation", getInputParameter("lNumberOfGenerationSubPopulation"));
                    parameters.put("maxFE", getInputParameter("lMaxFE"));
                    parameters.put("maxNoImprovement", getInputParameter("lMaxNoImprovementLowerLevel"));
                    parameters.put("p", getInputParameter("lP"));
                    lowerlevel.addInputParameters(parameters);

                    HashMap<String, Operator> operators = new HashMap<>();
                    operators.put("selection", getOperator("lSelection"));
                    operators.put("crossover", getOperator("lCrossover"));
                    operators.put("mutation", getOperator("lMutation"));
                    operators.put("neighborhood", getOperator("lNeighborhood"));
                    operators.put("coEvolutionCrossover", getOperator("lCoEvolutionCrossover"));
                    lowerlevel.addOperators(operators);
                    /*if(this.getName().equals("Thread-3")) {
                        System.out.println("["+this.getName()+"] ==> Execute lowerlevel...");
                    }*/
                    //System.out.println("Execute lowerlevel...");
                    Solution lowerlevelSolution = new Solution(lowerlevel.execute().get(0));
                    /*if(this.getName().equals("Thread-3")) {
                        System.out.println("["+this.getName()+"] ==> End lowerlevel...");
                    }
                    System.out.println("End lowerlevel. ");
                    System.out.println("Best lowerlevel solution ==> " + lowerlevelSolution.toString());
                    System.out.println("DirectRationality        ==> " + lowerlevel.getDirectRationality());
                    System.out.println("WeightedRationality      ==> " + lowerlevel.getWeightedRationality());*/
                    //problem.evaluateLowerLevel(solution);
                    /*if(this.getName().equals("Thread-3")) {
                        System.out.println("["+this.getName()+"] ==> Execution evaluation...");
                        solution.setMarked(true);
                    }*/
                    problem.evaluate(solution, lowerlevelSolution);
                    /*if(this.getName().equals("Thread-3")) {
                        System.out.println("["+this.getName()+"] ==> End evaluation...");
                    }*/
                    if(lowerlevel.getDirectRationality()< directRationality && lowerlevel.getDirectRationality() > 0) {
                        directRationality = lowerlevel.getDirectRationality();
                    }
                    FE++;
                    UFE++;
                    LFE += lowerlevel.getFE();
                    if(lowerlevel.getWeightedRationality() < weightedRationality && lowerlevel.getWeightedRationality() > 0) {
                        weightedRationality = lowerlevel.getWeightedRationality();
                    }
                    if (bestSolution.compare(solution, new SolutionComparator()) == 1) {
                        //if (bestSolution.getFitness() > solution.getFitness()) {
                        bestSolution = new Solution(solution);
                        this.lowerSolution = new Solution(lowerlevelSolution);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(CODBAII.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
