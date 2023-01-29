package blp.approches.codbaIIcro;

import blp.core.Algorithm;
import blp.core.Operator;
import blp.core.Problem;
import blp.core.Solution;
import blp.core.SolutionSet;
import blp.operators.cro.Decomposition;
import blp.operators.cro.InterMolecular;
import blp.operators.cro.OnWall;
import blp.operators.cro.Synthesis;
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
    private int numberOfGenerationSubPopulation;
    private int maxNoImprovement;
    private int p;
    private Operator decompositionOp;
    private Operator onWallOp;
    private Operator synthesisOp;
    private Operator interMolecularOp;
    private Operator neighborhoodOp;
    private Operator coEvolutionOp;

    private static double collR;
    private static double synThres;
    private static int decThres;
    private static double lossR;
    private static double enBuff;

    private int directRationality = 0;
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
            numberOfGenerationSubPopulation = (Integer) super.getInputParameter("numberOfGenerationSubPopulation");
            maxNoImprovement = (Integer) super.getInputParameter("maxNoImprovement");
            p = (Integer) super.getInputParameter("p");

            decompositionOp = (Operator) super.getOperator("decomposition");
            onWallOp = (Operator) super.getOperator("onWall");
            synthesisOp = (Operator) super.getOperator("synthesis");
            interMolecularOp = (Operator) super.getOperator("interMolecular");
            neighborhoodOp = (Operator) super.getOperator("neighborhood");
            coEvolutionOp = (Operator) super.getOperator("coEvolutionCrossover");

            collR = (Double) super.getInputParameter("collR");
            lossR = (Double) super.getInputParameter("lossR");
            decThres = (Integer) super.getInputParameter("decThres");
            synThres = (Double) super.getInputParameter("synThres");
            enBuff = (Double) super.getInputParameter("enBuff");

            int numberNoImprovement = 0;
            Solution bestSolution = new Solution(super.getProblem(), "lower");
            if (super.getProblem().isMaximize()) {
                bestSolution.setFitness(Double.NEGATIVE_INFINITY);
            } else {
                bestSolution.setFitness(Double.POSITIVE_INFINITY);
            }
            Solution latestBestSolution = new Solution(super.getProblem(), "lower");
            // run Lower Level
            // Step 1: Initilization
            // 1- Generate reference points
            SolutionSet referencePoints = new DSDM(super.getProblem(), p, "lower").getReferencePoints();
            //System.out.println("Reference Points: " + referencePoints.size());
            // 2- Generate Sub populations
            ArrayList<Thread> subPopulations = new ArrayList();
            for (int i = 0; i < referencePoints.size(); i++) {
                SolutionSet subPopulation_i = new SolutionSet(generateSubPopulation(new Solution(referencePoints.get(i)), referencePoints.size()));
                subPopulations.add(new ThreadCRO(super.getProblem(), subPopulation_i));
            }
            while (numberNoImprovement < maxNoImprovement) {
                // STRATING MULTI-THREADING
                //System.out.println("Starting threads...");
                for (Thread subPop : subPopulations) {
                    subPop.start();
                }
                // CHECK IF ALL THREAD IS COMPLETED
                boolean threadsCompleted = false;
                while (!threadsCompleted) {
                    threadsCompleted = true;
                    for (Thread subPop : subPopulations) {
                        threadsCompleted &= ((ThreadCRO) subPop).isCompleted();
                    }
                }
                //System.out.println("all threads are completed.");
                // SAVE BEST SOLUTION
                boolean improvement = false;
                for (Thread subPop : subPopulations) {
                    if (bestSolution.compare(((ThreadCRO) subPop).getBestSolution(), new SolutionComparator()) == 1) {
                        //if (bestSolution.getFitness() > ((ThreadCRO) subPop).getBestSolution().getFitness()) {
                        latestBestSolution.setFitness(bestSolution.getFitness());
                        bestSolution = new Solution(((ThreadCRO) subPop).getBestSolution());
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
                ArrayList<SolutionSet> newSubPopulations = new ArrayList<>();
                for (Thread subPop : subPopulations) {
                    Solution randomlyBestSolution = ((ThreadCRO) subPopulations.get(PseudoRandom.randInt(0, subPopulations.size() - 1))).getBestSolution();
                    SolutionSet offsprings = new SolutionSet();
                    for (int i = 0; i < ((ThreadCRO) subPop).getPopulation().size(); i++) {
                        Object[] solutions = {randomlyBestSolution, ((ThreadCRO) subPop).getPopulation().get(i)};
                        solutions = coEvolutionOp.execute(solutions);
                        if (solutions.length > 1) {
                            int index = PseudoRandom.randInt(0, solutions.length - 1);
                            offsprings.add((Solution) solutions[index]);
                        } else {
                            offsprings.add((Solution) solutions[0]);
                        }
                    }
                    offsprings.union(((ThreadCRO) subPop).getPopulation());
                    offsprings.sort(new SolutionComparator());
                    for (int i = 0; i < offsprings.size(); i++) {
                        if (i >= ((ThreadCRO) subPop).getPopulation().size()) {
                            offsprings.remove(i);
                        }
                    }
                    newSubPopulations.add(offsprings);
                }
                subPopulations.clear();
                subPopulations = new ArrayList<>();
                for (int i = 0; i < newSubPopulations.size(); i++) {
                    subPopulations.add(new ThreadCRO(super.getProblem(), newSubPopulations.get(i)));
                }
                weightedRationality += (latestBestSolution.getFitness() / bestSolution.getFitness()) / (subPopulations.size() * (numberOfGenerationSubPopulation + 1));
            }
            //---------------------------------------------------
            // Step 3: Return the optimal solution
            result.add(bestSolution);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        return result;
    }

    public SolutionSet generateSubPopulation(Solution referancePoint, int numberReferencePoints) {
        SolutionSet subPop = new SolutionSet();
        for (int i = 0; i < popSize / numberReferencePoints; i++) {
            Solution solution = new Solution(referancePoint);
            Solution[] solutions = {solution};
            Object[] neighborhoodResult = neighborhoodOp.execute(solutions);
            Solution neighborhood = new Solution((Solution) neighborhoodResult[0]);
            neighborhood.setID(i);
            subPop.add(neighborhood);
        }
        return subPop;
    }

    public int getDirectRationality() {
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

    private class ThreadCRO extends Thread {

        private Problem problem;
        private SolutionSet population;
        private Solution bestSolution;
        private boolean completed;
        private double enBuff;
        private double collR;
        private double lossR;
        private int decThres;
        private double synThres;
        private int FE;
        private int numberdec;
        private int numberwall;
        private int numbersynth;
        private int numberinter;

        public ThreadCRO(Problem problem, SolutionSet population) throws ClassNotFoundException {
            this.problem = problem;
            this.population = population;
            this.bestSolution = new Solution(problem, "lower");
            if (problem.isMaximize()) {
                bestSolution.setFitness(Double.NEGATIVE_INFINITY);
            } else {
                bestSolution.setFitness(Double.POSITIVE_INFINITY);
            }
            FE = 0;
            numberdec = 0;
            numberwall = 0;
            numbersynth = 0;
            numberinter = 0;
            this.enBuff = LowerLevel.enBuff;
            this.collR = LowerLevel.collR;
            this.lossR = LowerLevel.lossR;
            this.decThres = LowerLevel.decThres;
            this.synThres = LowerLevel.synThres;
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
            this.completed = false;
            evalutePopulation(this.population);
            SolutionSet childs;
            SolutionSet Parent;
            for (int generation = 0; generation < numberOfGenerationSubPopulation; generation++) {
                /*System.out.println("Generation : " + (generation + 1));
                System.out.println("Population>>");
                for (int i = 0; i < population.size(); i++) {
                    System.out.println("Solution" + (i + 1) + "||");
                    System.out.println(population.get(i).toString());
                }*/
                Parent = new SolutionSet(this.population);
                // Iteration starts. 
                //System.out.println("generating childs...");
                childs = generateChilds(Parent);
                /*System.out.println("Childs>>");
                for (int i = 0; i < childs.size(); i++) {
                    System.out.println("Solution" + (i + 1) + "||");
                    System.out.println(childs.get(i).toString());
                }
                System.out.println(childs.size() + " childs is generated");
                System.out.println(numberdec + " decomposition(s)");
                System.out.println(numberwall + " wall(s)");
                System.out.println(numbersynth + " synthese(s)");
                System.out.println(numberinter + " inter(s)");
                System.out.println("Updating energy management rules...");*/
                evalutePopulation(childs);
                // Updated procedure with CRO energy management rules
                int j = 0;
                SolutionSet newPopulation = new SolutionSet();
                while (j < childs.size()) {
                    Solution child = childs.get(j);
                    String op = child.getOperationType();
                    Solution parent, parent1, child1;
                    double tempBuff;
                    if (op.equals("wall")) {
                        parent = population.get(child.getParent1Index());
                        tempBuff = parent.getFitness() + parent.getKE() - child.getFitness();
                        if (tempBuff >= 0) {
                            double newEnBuff = 0;
                            double KE = tempBuff * (PseudoRandom.randDouble() * (1.0 - lossR) + lossR);
                            child.setKE(KE);
                            newEnBuff += tempBuff - KE;
                            child.checkBetter();
                            this.enBuff += newEnBuff;
                            newPopulation.add(new Solution(child));
                            LFE++;
                        } else {
                            newPopulation.add(new Solution(parent));
                        }
                        j++;
                    } else if (op.equals("dec")) {
                        parent = population.get(child.getParent1Index());
                        child = childs.get(parent.getChild1Index());
                        child1 = childs.get(parent.getChild2Index());//population_q_t.get(parent.getChild2());
                        tempBuff = parent.getFitness() + parent.getKE() - child.getFitness() - child1.getFitness();
                        if (tempBuff >= 0 || (tempBuff + this.enBuff >= 0)) {
                            if (tempBuff >= 0) {
                                double KE = tempBuff * PseudoRandom.randDouble();
                                child.setKE(KE);
                                child1.setKE(tempBuff - KE);
                            } else {
                                this.enBuff += tempBuff;
                                double KE = this.enBuff * PseudoRandom.randDouble() * PseudoRandom.randDouble();
                                child.setKE(KE);
                                this.enBuff -= KE;
                                child1.setKE(this.enBuff * PseudoRandom.randDouble() * PseudoRandom.randDouble());
                                this.enBuff -= child1.getKE();
                            }
                            child.setNumHit(0);
                            child.setMinHit(0);
                            if (this.problem.isMaximize()) {
                                child.setBetterFitness(Double.NEGATIVE_INFINITY);
                                child1.setBetterFitness(Double.NEGATIVE_INFINITY);
                            } else {
                                child.setBetterFitness(Double.POSITIVE_INFINITY);
                                child1.setBetterFitness(Double.POSITIVE_INFINITY);
                            }
                            //child.setBetterFitness(Double.POSITIVE_INFINITY);
                            child1.setNumHit(0);
                            child1.setMinHit(0);
                            //child1.setBetterFitness(Double.POSITIVE_INFINITY);
                            child.checkBetter();
                            child1.checkBetter();
                            newPopulation.add(new Solution(child));
                            newPopulation.add(new Solution(child1));
                        } else {
                            newPopulation.add(new Solution(parent));
                        }
                        j += 2;
                    } else if (op.equals("inter")) {
                        parent = population.get(child.getParent1Index());
                        parent1 = population.get(child.getParent2Index());
                        child = childs.get(parent.getChild1Index());
                        child1 = childs.get(parent.getChild2Index());
                        tempBuff = parent.getFitness() + parent.getKE() + parent1.getFitness() + parent1.getKE() - child.getFitness() - child1.getFitness();
                        if (tempBuff >= 0) {
                            double KE = tempBuff * PseudoRandom.randDouble();
                            child.setKE(KE);
                            child1.setKE(tempBuff - KE);
                            //update the  numhit of the new molecular structures
                            child.checkBetter();
                            child1.checkBetter();
                            newPopulation.add(new Solution(child));
                            newPopulation.add(new Solution(child1));
                        } else {
                            newPopulation.add(new Solution(parent));
                            newPopulation.add(new Solution(parent1));
                        }
                        j += 2;
                    } else if (op.equals("syn")) {
                        parent = population.get(child.getParent1Index());
                        parent1 = population.get(child.getParent2Index());
                        tempBuff = parent.getFitness() + parent.getKE() + parent1.getFitness() + parent1.getKE() - child.getFitness();
                        if (tempBuff >= 0) {
                            child.setKE(tempBuff);
                            if (this.problem.isMaximize()) {
                                child.setBetterFitness(Double.NEGATIVE_INFINITY);
                            } else {
                                child.setBetterFitness(Double.POSITIVE_INFINITY);
                            }
                            //child.setBetterFitness(Double.POSITIVE_INFINITY);
                            child.setNumHit(0);
                            child.setMinHit(0);
                            child.checkBetter();
                            newPopulation.add(new Solution(child));
                        } else {
                            newPopulation.add(new Solution(parent));
                            newPopulation.add(new Solution(parent1));
                        }
                        j++;
                    }
                }
                population = new SolutionSet(newPopulation);
                //System.out.println("Energy management rules is updated " + newPopulation.size() + " | " + population.size());
            }
            evalutePopulation(population);
            this.completed = true;
            /*System.out.println("Best Solution: ");
            System.out.println(bestSolution.toString());*/
        }

        private void evalutePopulation(SolutionSet population) {
            for (int i = 0; i < population.size(); i++) {
                Solution solution = population.get(i);
                problem.evaluateLowerLevel(upperSolution, solution);
                LFE++;
                if (bestSolution.compare(solution, new SolutionComparator()) == 1) {
                    //if (bestSolution.getFitness() > solution.getFitness()) {
                    bestSolution = new Solution(solution);
                }
            }
        }

        private int indexOf(Solution solution) {
            int index = -1, i = 0;
            while (index == -1 && i < population.size()) {
                if (population.get(i).getID() == solution.getID()) {
                    index = i;
                }
                i++;
            }
            return index;
        }

        private SolutionSet generateChilds(SolutionSet Parent) {
            SolutionSet childs = new SolutionSet();
            while (!Parent.isEmpty()) {
                if ((PseudoRandom.randDouble() > collR) || (Parent.size() == 1)) {
                    int pos1 = PseudoRandom.randInt(0, Parent.size() - 1);
                    Solution p = (Solution) Parent.get(pos1);
                    if (p.decCheck(decThres)) {
                        //p.dec();
                        Solution solution = new Solution(p);
                        Solution[] solutions = {solution};
                        ArrayList<Integer> parentsIndex = new ArrayList<>();
                        parentsIndex.add(indexOf(solution));
                        HashMap<String, Object> parameters = new HashMap<>();
                        parameters.put("level", "lower");
                        parameters.put("parentIndex", parentsIndex);
                        decompositionOp = new Decomposition(parameters);
                        Object[] decompositionResults = decompositionOp.execute(solutions);
                        Solution decChild1 = new Solution((Solution) decompositionResults[0]);
                        Solution decChild2 = new Solution((Solution) decompositionResults[1]);
                        childs.add(decChild1);
                        population.get(indexOf(solution)).setChild1Index(childs.size() - 1);
                        childs.add(decChild2);
                        population.get(indexOf(solution)).setChild2Index(childs.size() - 1);
                        numberdec++;
                        FE += 2;
                    } // On-wall.
                    else {
                        Solution solution = new Solution(p);
                        Solution[] solutions = {solution};
                        ArrayList<Integer> parentsIndex = new ArrayList<>();
                        parentsIndex.add(indexOf(solution));
                        HashMap<String, Object> parameters = new HashMap<>();
                        parameters.put("level", "lower");
                        parameters.put("parentIndex", parentsIndex);
                        onWallOp = new OnWall(parameters);
                        Object[] wallResults = onWallOp.execute(solutions);
                        Solution wallChild = new Solution((Solution) wallResults[0]);
                        //p.wall();
                        childs.add(wallChild);
                        population.get(indexOf(solution)).setChild1Index(childs.size() - 1);
                        numberwall++;
                        FE++;
                    }
                    Parent.remove(pos1);
                } else {
                    // Synthesis.
                    int pos1 = PseudoRandom.randInt(0, Parent.size() - 1);
                    int pos2 = pos1;
                    while (pos1 == pos2) {
                        pos2 = PseudoRandom.randInt(0, Parent.size() - 1);
                    }
                    Solution p;
                    Solution q;
                    if (Parent.size() == 2) {
                        pos1 = 0;
                        pos2 = 1;
                    }
                    p = (Solution) Parent.get(pos1);
                    q = (Solution) Parent.get(pos2);
                    if (p.synCheck(synThres) && q.synCheck(synThres)) {
                        Solution solution1 = new Solution(p);
                        Solution solution2 = new Solution(q);
                        Solution[] solutions = {solution1, solution2};
                        ArrayList<Integer> parentsIndex = new ArrayList<>();
                        parentsIndex.add(indexOf(solution1));
                        parentsIndex.add(indexOf(solution2));
                        HashMap<String, Object> parameters = new HashMap<>();
                        parameters.put("level", "lower");
                        parameters.put("parentIndex", parentsIndex);
                        synthesisOp = new Synthesis(parameters);
                        Object[] synthResults = synthesisOp.execute(solutions);
                        Solution synthChild1 = new Solution((Solution) synthResults[0]);
                        childs.add(synthChild1);
                        population.get(indexOf(solution1)).setChild1Index(childs.size() - 1);
                        population.get(indexOf(solution2)).setChild2Index(childs.size() - 1);
                        numbersynth++;
                        FE += 2;
                        // Inter-Molecular.
                    } else {
                        //p.inter(q);
                        Solution solution1 = new Solution(p);
                        Solution solution2 = new Solution(q);
                        Solution[] solutions = {solution1, solution2};
                        ArrayList<Integer> parentsIndex = new ArrayList<>();
                        parentsIndex.add(indexOf(solution1));
                        parentsIndex.add(indexOf(solution2));
                        HashMap<String, Object> parameters = new HashMap<>();
                        parameters.put("level", "lower");
                        parameters.put("parentIndex", parentsIndex);
                        interMolecularOp = new InterMolecular(parameters);
                        Object[] interResults = interMolecularOp.execute(solutions);
                        Solution interChild1 = new Solution((Solution) interResults[0]);
                        Solution interChild2 = new Solution((Solution) interResults[1]);
                        childs.add(interChild1);
                        population.get(indexOf(solution1)).setChild1Index(childs.size() - 1);
                        population.get(indexOf(solution2)).setChild1Index(childs.size() - 1);
                        childs.add(interChild2);
                        population.get(indexOf(solution1)).setChild2Index(childs.size() - 1);
                        population.get(indexOf(solution2)).setChild2Index(childs.size() - 1);
                        numberinter++;
                        FE++;
                    }
                    if (pos1 < pos2) {
                        Parent.remove(pos1);
                        Parent.remove(pos2 - 1);
                    } else {
                        Parent.remove(pos2);
                        Parent.remove(pos1 - 1);
                    }
                }
            }
            return childs;
        }
    }
}
