package blp.approches.codbaII;

import blp.core.Operator;
import blp.core.Problem;
import blp.core.Solution;
import blp.operators.crossover.UniformCrossover;
import blp.operators.mutation.UniformMutation;
import blp.operators.selection.BinaryTournament;
import blp.problems.KP;
import blp.problems.VRP;
import blp.problems.parameters.Parameters;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Abir
 */
public class ExperimentalStudy {

    public static void main(String[] args) {
        String[] problems = {/*"VRP"*/"KP"};
        String[] instances = {"ins1"/*"p05","p06","p07","p08""_A-n32-k5.vrp", "_A-n37-k6.vrp", "_A-n38-k5.vrp", "_A-n39-k6.vrp", "_A-n45-k6.vrp", "_A-n54-k7.vrp", "_A-n69-k9.vrp", "_A-n80-k10.vrp", "_B-n31-k5.vrp", "_B-n34-k5.vrp", "_B-n41-k6.vrp", "_B-n44-k7.vrp", "_B-n57-k7.vrp", "_B-n64-k9.vrp", "_B-n68-k9.vrp", "_B-n78-k10.vrp", "_E-n101-k14.vrp", "_E-n101-k8.vrp", "_E-n30-k3.vrp", "_E-n76-k14.vrp", "_E-n76-k8.vrp","pr01", "pr02", "pr03", "pr04", "pr05", "pr06", "pr07", "pr08", "pr09", "pr10", "p01", "p02", "p03", "p04", "p05", "p06", "p17", "p08", "p09", "p10", "p11", "p12", "p13", "p14", "p15", "p16", "p17", "p18", "p19", "p20", "p21"*/};
        String experimentName = "CODBAII";
        String experimentBaseDirectory = System.getProperty("user.dir") + System.getProperty("file.separator") + "ExprimentalStudy" + System.getProperty("file.separator") + experimentName;
        int independentRuns = 2;
        try {
            for (String problem : problems) {
                String path = experimentBaseDirectory + System.getProperty("file.separator") + problem;
                for (String instance : instances) {
                    String _path = path + System.getProperty("file.separator") + instance;
                    File f = new File(_path);
                    if (!f.exists()) {
                        f.mkdirs();
                    }
                    ProblemInit prb = new ProblemInit(problem + "." + instance);
                    File upperLevelFitnessFile = new File(_path + System.getProperty("file.separator") + "UpperLevelFitness.upf");
                    FileWriter writerUpperLevelFitness = new FileWriter(upperLevelFitnessFile);
                    File directRationalityFile = new File(_path + System.getProperty("file.separator") + "DirectRationality.ldr");
                    FileWriter writerDirectRationality = new FileWriter(directRationalityFile);
                    File weightedRationalityFile = new File(_path + System.getProperty("file.separator") + "WeightedRationality.lwr");
                    FileWriter writerWeightedRationality = new FileWriter(weightedRationalityFile);
                    File upperFEFile = new File(_path + System.getProperty("file.separator") + "UpperFE.ufe");
                    FileWriter writerUpperFE = new FileWriter(upperFEFile);
                    File lowerFEFile = new File(_path + System.getProperty("file.separator") + "LowerFE.lfe");
                    FileWriter writerLowerFE = new FileWriter(lowerFEFile);
                    File cpuTimeFile = new File(_path + System.getProperty("file.separator") + "CPUTime.ct");
                    FileWriter writerCPUTime = new FileWriter(cpuTimeFile);
                    for (int i = 0; i < independentRuns; i++) {
                        // Problem initialization
                        CODBAII codbaii = initProblem(problem, instance, prb);
                        System.out.print("Running : " + problem + "/" + instance + " run : " + (i + 1) + " Please wait ...");
                        long initTime = System.currentTimeMillis();
                        Solution upperLevelSolution = codbaii.execute().get(0);
                        long estimatedTime = System.currentTimeMillis() - initTime;
                        Solution lowerLevelSolution = codbaii.getLowerSolution();
                        double directRationality = codbaii.getDirectRationality();
                        double weightedRationality = codbaii.getWeightedRationality();
                        int upperFE = codbaii.getUpperFE();
                        int lowerFE = codbaii.getLowerFE();
                        File varUFile = new File(_path + System.getProperty("file.separator") + "VARU." + i);
                        FileWriter writerVARU = new FileWriter(varUFile);
                        writerVARU.write(upperLevelSolution.toString());
                        writerVARU.close();
                        File varLFile = new File(_path + System.getProperty("file.separator") + "VARL." + i);
                        FileWriter writerVARL = new FileWriter(varLFile);
                        writerVARL.write(lowerLevelSolution.toString());
                        writerVARL.close();
                        writerUpperLevelFitness.write("" + upperLevelSolution.getFitness());
                        writerUpperLevelFitness.write(System.getProperty("line.separator"));
                        writerDirectRationality.write("" + directRationality);
                        writerDirectRationality.write(System.getProperty("line.separator"));
                        writerWeightedRationality.write("" + weightedRationality);
                        writerWeightedRationality.write(System.getProperty("line.separator"));
                        writerUpperFE.write("" + upperFE);
                        writerUpperFE.write(System.getProperty("line.separator"));
                        writerLowerFE.write("" + lowerFE);
                        writerLowerFE.write(System.getProperty("line.separator"));
                        writerCPUTime.write("" + estimatedTime);
                        writerCPUTime.write(System.getProperty("line.separator"));
                        System.out.println("\t done");
                    }
                    writerUpperLevelFitness.close();
                    writerDirectRationality.close();
                    writerWeightedRationality.close();
                    writerUpperFE.close();
                    writerLowerFE.close();
                    writerCPUTime.close();
                }
            }
        } catch (Exception e) {
            Logger.getLogger(ExperimentalStudy.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private static CODBAII initProblem(String problem, String instance, ProblemInit prb) {
        // Problem initialization
        Parameters.lowerBoundProductionCost = prb.getLowerBoundProductCost();
        Parameters.upperBoundProductionCost = prb.getUpperBoundProductCost();
        Parameters.lowerBoundDeleveryCost = prb.getLowerDeliveryCost();
        Parameters.upperBoundDeleveryCost = prb.getUpperDeliveryCost();
        Parameters.plantProductionsAvailability = prb.getPlantProductionsAvailability();
        //Problem prob = new VRP(instance, prb.getUpperSolutionType(), prb.getLowerSolutionType());
        Problem prob = new KP(instance, prb.getUpperSolutionType(), prb.getLowerSolutionType());
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("populationSize", prb.getPopulationSize());
        parameters.put("lPopulationSize", prb.getlPopulationSize());
        parameters.put("maxFE", prb.getMaxFE());
        parameters.put("lMaxFE", prb.getlMaxFE());
        parameters.put("maxNoImprovementUpperLevel", prb.getMaxNoImprovementUpperLevel());
        parameters.put("lMaxNoImprovementLowerLevel", prb.getlMaxNoImprovementUpperLevel());
        parameters.put("numberOfGenerationSubPopulation", prb.getNumberOfGenerationSubPopulation());
        parameters.put("lNumberOfGenerationSubPopulation", prb.getlNumberOfGenerationSubPopulation());
        parameters.put("p", prb.getP());
        parameters.put("lP", prb.getlP());

        HashMap<String, Object> uCrossoverParameters = new HashMap<>();
        uCrossoverParameters.put("level", "upper");
        HashMap<String, Object> uMutationParameters = new HashMap<>();
        uMutationParameters.put("mutationProbability", 0.1);
        uMutationParameters.put("level", "upper");
        HashMap<String, Object> uNeighborhoodParameters = new HashMap<>();
        uNeighborhoodParameters.put("mutationProbability", 0.1);
        uNeighborhoodParameters.put("level", "upper");
        HashMap<String, Object> uCoEvolutionCrossoverParameters = new HashMap<>();
        uCoEvolutionCrossoverParameters.put("mutationProbability", 0.1);
        uCoEvolutionCrossoverParameters.put("level", "upper");

        HashMap<String, Object> lCrossoverParameters = new HashMap<>();
        lCrossoverParameters.put("level", "lower");
        HashMap<String, Object> lMutationParameters = new HashMap<>();
        lMutationParameters.put("mutationProbability", 0.1);
        lMutationParameters.put("level", "lower");
        HashMap<String, Object> lNeighborhoodParameters = new HashMap<>();
        lNeighborhoodParameters.put("mutationProbability", 0.1);
        lNeighborhoodParameters.put("level", "lower");
        HashMap<String, Object> lCoEvolutionCrossoverParameters = new HashMap<>();
        lCoEvolutionCrossoverParameters.put("mutationProbability", 0.1);
        lCoEvolutionCrossoverParameters.put("level", "lower");

        HashMap<String, Operator> operators = new HashMap<>();
        operators.put("selection", new BinaryTournament(new HashMap<>()));
        operators.put("crossover", new UniformCrossover(uCrossoverParameters));
        operators.put("mutation", new UniformMutation(uMutationParameters));
        operators.put("neighborhood", new UniformMutation(uNeighborhoodParameters));
        operators.put("coEvolutionCrossover", new UniformMutation(uCoEvolutionCrossoverParameters));

        operators.put("lSelection", new BinaryTournament(new HashMap<>()));
        operators.put("lCrossover", new UniformCrossover(lCrossoverParameters));
        operators.put("lMutation", new UniformMutation(lMutationParameters));
        operators.put("lNeighborhood", new UniformMutation(lNeighborhoodParameters));
        operators.put("lCoEvolutionCrossover", new UniformMutation(lCoEvolutionCrossoverParameters));

        CODBAII algorithm = new CODBAII(prob);
        algorithm.setInputParameter(parameters);
        algorithm.setOperator(operators);

        return algorithm;
    }

    private static class ProblemInit {

        private int numberOfPlants;
        private int numberOfDepots;
        private double lowerBoundProductCost;
        private double upperBoundProductCost;
        private double lowerDeliveryCost;
        private double upperDeliveryCost;
        private int plantProductionsAvailability;
        private int[] productionsAvailability;
        private String upperSolutionType;
        private String lowerSolutionType;
        private int populationSize;
        private int lPopulationSize;
        private int maxFE;
        private int lMaxFE;
        private int maxNoImprovementUpperLevel;
        private int lMaxNoImprovementUpperLevel;
        private int numberOfGenerationSubPopulation;
        private int lNumberOfGenerationSubPopulation;
        private int p;
        private int lP;

        public ProblemInit(String Problem) {
            if (Problem.equals("KP.p01")) {
                upperSolutionType = "Int";
                lowerSolutionType = "Int";
                populationSize = 100;
                lPopulationSize = 100;
                maxFE = 2000;
                lMaxFE = 2000;
                maxNoImprovementUpperLevel = 1;
                lMaxNoImprovementUpperLevel = 1;
                numberOfGenerationSubPopulation = 1;
                lNumberOfGenerationSubPopulation = 1;
                p = 1;
                lP = 1;
            } else if (Problem.equals("KP.p02")) {
                upperSolutionType = "Int";
                lowerSolutionType = "Int";
                populationSize = 100;
                lPopulationSize = 100;
                maxFE = 2000;
                lMaxFE = 2000;
                maxNoImprovementUpperLevel = 1;
                lMaxNoImprovementUpperLevel = 1;
                numberOfGenerationSubPopulation = 1;
                lNumberOfGenerationSubPopulation = 1;
                p = 1;
                lP = 1;
            } else if (Problem.equals("KP.p03")) {
                upperSolutionType = "Int";
                lowerSolutionType = "Int";
                populationSize = 100;
                lPopulationSize = 100;
                maxFE = 2000;
                lMaxFE = 2000;
                maxNoImprovementUpperLevel = 1;
                lMaxNoImprovementUpperLevel = 1;
                numberOfGenerationSubPopulation = 1;
                lNumberOfGenerationSubPopulation = 1;
                p = 1;
                lP = 1;
            } else if (Problem.equals("KP.p04")) {
                upperSolutionType = "Int";
                lowerSolutionType = "Int";
                populationSize = 100;
                lPopulationSize = 100;
                maxFE = 2000;
                lMaxFE = 2000;
                maxNoImprovementUpperLevel = 1;
                lMaxNoImprovementUpperLevel = 1;
                numberOfGenerationSubPopulation = 1;
                lNumberOfGenerationSubPopulation = 1;
                p = 1;
                lP = 1;
            } else if (Problem.equals("KP.p04")) {
                upperSolutionType = "Int";
                lowerSolutionType = "Int";
                populationSize = 100;
                lPopulationSize = 100;
                maxFE = 2000;
                lMaxFE = 2000;
                maxNoImprovementUpperLevel = 1;
                lMaxNoImprovementUpperLevel = 1;
                numberOfGenerationSubPopulation = 1;
                lNumberOfGenerationSubPopulation = 1;
                p = 1;
                lP = 1;
            } else if (Problem.equals("KP.ins1")) {
                upperSolutionType = "Int";
                lowerSolutionType = "Int";
                populationSize = 100;
                lPopulationSize = 100;
                maxFE = 2000;
                lMaxFE = 2000;
                maxNoImprovementUpperLevel = 1;
                lMaxNoImprovementUpperLevel = 1;
                numberOfGenerationSubPopulation = 1;
                lNumberOfGenerationSubPopulation = 1;
                p = 1;
                lP = 1;
            } else if (Problem.equals("KP.p06")) {
                upperSolutionType = "Int";
                lowerSolutionType = "Int";
                populationSize = 100;
                lPopulationSize = 100;
                maxFE = 2000;
                lMaxFE = 2000;
                maxNoImprovementUpperLevel = 1;
                lMaxNoImprovementUpperLevel = 1;
                numberOfGenerationSubPopulation = 1;
                lNumberOfGenerationSubPopulation = 1;
                p = 1;
                lP = 1;
            } else if (Problem.equals("KP.p07")) {
                upperSolutionType = "Int";
                lowerSolutionType = "Int";
                populationSize = 100;
                lPopulationSize = 100;
                maxFE = 2000;
                lMaxFE = 2000;
                maxNoImprovementUpperLevel = 1;
                lMaxNoImprovementUpperLevel = 1;
                numberOfGenerationSubPopulation = 1;
                lNumberOfGenerationSubPopulation = 1;
                p = 1;
                lP = 1;
            } else if (Problem.equals("KP.p08")) {
                upperSolutionType = "Int";
                lowerSolutionType = "Int";
                populationSize = 100;
                lPopulationSize = 100;
                maxFE = 2000;
                lMaxFE = 2000;
                maxNoImprovementUpperLevel = 1;
                lMaxNoImprovementUpperLevel = 1;
                numberOfGenerationSubPopulation = 1;
                lNumberOfGenerationSubPopulation = 1;
                p = 1;
                lP = 1;
            } else if (Problem.equals("VRP.pr01")) {
                numberOfPlants = 4;
                numberOfDepots = 4;
                lowerBoundProductCost = 1.0;
                upperBoundProductCost = 4.0;
                lowerDeliveryCost = 0.36;
                upperDeliveryCost = 5.36;
                productionsAvailability = new int[numberOfPlants];
                for (int i = 0; i < productionsAvailability.length; i++) {
                    productionsAvailability[i] = 2000;
                }
                plantProductionsAvailability = 2000;
                upperSolutionType = "ArrayInt";
                lowerSolutionType = "Int";
                populationSize = 100;
                lPopulationSize = 100;
                maxFE = 1000;
                lMaxFE = 1000;
                maxNoImprovementUpperLevel = 5;
                lMaxNoImprovementUpperLevel = 5;
                numberOfGenerationSubPopulation = 10;
                lNumberOfGenerationSubPopulation = 10;
                p = 1;
                lP = 1;
            } else if (Problem.equals("VRP.pr02")) {
                numberOfPlants = 4;
                numberOfDepots = 4;
                lowerBoundProductCost = 1.0;
                upperBoundProductCost = 4.0;
                lowerDeliveryCost = 0.36;
                upperDeliveryCost = 5.36;
                productionsAvailability = new int[numberOfPlants];
                for (int i = 0; i < productionsAvailability.length; i++) {
                    productionsAvailability[i] = 2000;
                }
                plantProductionsAvailability = 2000;
                upperSolutionType = "ArrayInt";
                lowerSolutionType = "Int";
                populationSize = 100;
                lPopulationSize = 100;
                maxFE = 2500;
                lMaxFE = 2500;
                maxNoImprovementUpperLevel = 5;
                lMaxNoImprovementUpperLevel = 5;
                numberOfGenerationSubPopulation = 10;
                lNumberOfGenerationSubPopulation = 10;
                p = 1;
                lP = 1;
            } else if (Problem.equals("VRP.pr03")) {
                numberOfPlants = 4;
                numberOfDepots = 4;
                lowerBoundProductCost = 1.0;
                upperBoundProductCost = 4.0;
                lowerDeliveryCost = 0.36;
                upperDeliveryCost = 5.36;
                productionsAvailability = new int[numberOfPlants];
                for (int i = 0; i < productionsAvailability.length; i++) {
                    productionsAvailability[i] = 2000;
                }
                plantProductionsAvailability = 2000;
                upperSolutionType = "ArrayInt";
                lowerSolutionType = "Int";
                populationSize = 100;
                lPopulationSize = 100;
                maxFE = 1000;
                lMaxFE = 1000;
                maxNoImprovementUpperLevel = 5;
                lMaxNoImprovementUpperLevel = 5;
                numberOfGenerationSubPopulation = 10;
                lNumberOfGenerationSubPopulation = 10;
                p = 1;
                lP = 1;
            } else if (Problem.equals("VRP.pr04")) {
                numberOfPlants = 4;
                numberOfDepots = 4;
                lowerBoundProductCost = 1.0;
                upperBoundProductCost = 4.0;
                lowerDeliveryCost = 0.36;
                upperDeliveryCost = 5.36;
                productionsAvailability = new int[numberOfPlants];
                for (int i = 0; i < productionsAvailability.length; i++) {
                    productionsAvailability[i] = 2000;
                }
                plantProductionsAvailability = 2000;
                upperSolutionType = "ArrayInt";
                lowerSolutionType = "Int";
                populationSize = 100;
                lPopulationSize = 100;
                maxFE = 1000;
                lMaxFE = 1000;
                maxNoImprovementUpperLevel = 5;
                lMaxNoImprovementUpperLevel = 5;
                numberOfGenerationSubPopulation = 10;
                lNumberOfGenerationSubPopulation = 10;
                p = 1;
                lP = 1;
            } else if (Problem.equals("VRP.pr05")) {
                numberOfPlants = 4;
                numberOfDepots = 4;
                lowerBoundProductCost = 1.0;
                upperBoundProductCost = 4.0;
                lowerDeliveryCost = 0.36;
                upperDeliveryCost = 5.36;
                productionsAvailability = new int[numberOfPlants];
                for (int i = 0; i < productionsAvailability.length; i++) {
                    productionsAvailability[i] = 2000;
                }
                plantProductionsAvailability = 2000;
                upperSolutionType = "ArrayInt";
                lowerSolutionType = "Int";
                populationSize = 100;
                lPopulationSize = 100;
                maxFE = 1000;
                lMaxFE = 1000;
                maxNoImprovementUpperLevel = 5;
                lMaxNoImprovementUpperLevel = 5;
                numberOfGenerationSubPopulation = 10;
                lNumberOfGenerationSubPopulation = 10;
                p = 1;
                lP = 1;
            } else if (Problem.equals("VRP.pr06")) {
                numberOfPlants = 4;
                numberOfDepots = 4;
                lowerBoundProductCost = 1.0;
                upperBoundProductCost = 4.0;
                lowerDeliveryCost = 0.36;
                upperDeliveryCost = 5.36;
                productionsAvailability = new int[numberOfPlants];
                for (int i = 0; i < productionsAvailability.length; i++) {
                    productionsAvailability[i] = 2000;
                }
                plantProductionsAvailability = 2000;
                upperSolutionType = "ArrayInt";
                lowerSolutionType = "Int";
                populationSize = 100;
                lPopulationSize = 100;
                maxFE = 2000;
                lMaxFE = 2000;
                maxNoImprovementUpperLevel = 5;
                lMaxNoImprovementUpperLevel = 5;
                numberOfGenerationSubPopulation = 10;
                lNumberOfGenerationSubPopulation = 10;
                p = 1;
                lP = 1;
            } else if (Problem.equals("VRP.pr07")) {
                numberOfPlants = 4;
                numberOfDepots = 4;
                lowerBoundProductCost = 1.0;
                upperBoundProductCost = 4.0;
                lowerDeliveryCost = 0.36;
                upperDeliveryCost = 5.36;
                productionsAvailability = new int[numberOfPlants];
                for (int i = 0; i < productionsAvailability.length; i++) {
                    productionsAvailability[i] = 2000;
                }
                plantProductionsAvailability = 2000;
                upperSolutionType = "ArrayInt";
                lowerSolutionType = "Int";
                populationSize = 100;
                lPopulationSize = 100;
                maxFE = 1000;
                lMaxFE = 1000;
                maxNoImprovementUpperLevel = 5;
                lMaxNoImprovementUpperLevel = 5;
                numberOfGenerationSubPopulation = 10;
                lNumberOfGenerationSubPopulation = 10;
                p = 1;
                lP = 1;
            } else if (Problem.equals("VRP.pr08")) {
                numberOfPlants = 4;
                numberOfDepots = 4;
                lowerBoundProductCost = 1.0;
                upperBoundProductCost = 4.0;
                lowerDeliveryCost = 0.36;
                upperDeliveryCost = 5.36;
                productionsAvailability = new int[numberOfPlants];
                for (int i = 0; i < productionsAvailability.length; i++) {
                    productionsAvailability[i] = 2000;
                }
                plantProductionsAvailability = 2000;
                upperSolutionType = "ArrayInt";
                lowerSolutionType = "Int";
                populationSize = 100;
                lPopulationSize = 100;
                maxFE = 1000;
                lMaxFE = 1000;
                maxNoImprovementUpperLevel = 5;
                lMaxNoImprovementUpperLevel = 5;
                numberOfGenerationSubPopulation = 10;
                lNumberOfGenerationSubPopulation = 10;
                p = 1;
                lP = 1;
            } else if (Problem.equals("VRP.pr09")) {
                numberOfPlants = 4;
                numberOfDepots = 4;
                lowerBoundProductCost = 1.0;
                upperBoundProductCost = 4.0;
                lowerDeliveryCost = 0.36;
                upperDeliveryCost = 5.36;
                productionsAvailability = new int[numberOfPlants];
                for (int i = 0; i < productionsAvailability.length; i++) {
                    productionsAvailability[i] = 2000;
                }
                plantProductionsAvailability = 2000;
                upperSolutionType = "ArrayInt";
                lowerSolutionType = "Int";
                populationSize = 100;
                lPopulationSize = 100;
                maxFE = 1000;
                lMaxFE = 1000;
                maxNoImprovementUpperLevel = 5;
                lMaxNoImprovementUpperLevel = 5;
                numberOfGenerationSubPopulation = 10;
                lNumberOfGenerationSubPopulation = 10;
                p = 1;
                lP = 1;
            } else if (Problem.equals("VRP.pr10")) {
                numberOfPlants = 4;
                numberOfDepots = 4;
                lowerBoundProductCost = 1.0;
                upperBoundProductCost = 4.0;
                lowerDeliveryCost = 0.36;
                upperDeliveryCost = 5.36;
                productionsAvailability = new int[numberOfPlants];
                for (int i = 0; i < productionsAvailability.length; i++) {
                    productionsAvailability[i] = 2000;
                }
                plantProductionsAvailability = 2000;
                upperSolutionType = "ArrayInt";
                lowerSolutionType = "Int";
                populationSize = 100;
                lPopulationSize = 100;
                maxFE = 2500;
                lMaxFE = 2500;
                maxNoImprovementUpperLevel = 5;
                lMaxNoImprovementUpperLevel = 5;
                numberOfGenerationSubPopulation = 10;
                lNumberOfGenerationSubPopulation = 10;
                p = 1;
                lP = 1;
            } else if (Problem.equals("VRP.p01")) {
                numberOfPlants = 4;
                numberOfDepots = 4;
                lowerBoundProductCost = 1.0;
                upperBoundProductCost = 4.0;
                lowerDeliveryCost = 0.36;
                upperDeliveryCost = 5.36;
                productionsAvailability = new int[numberOfPlants];
                for (int i = 0; i < productionsAvailability.length; i++) {
                    productionsAvailability[i] = 2000;
                }
                plantProductionsAvailability = 2000;
                upperSolutionType = "ArrayInt";
                lowerSolutionType = "Int";
                populationSize = 100;
                lPopulationSize = 100;
                maxFE = 1000;
                lMaxFE = 1000;
                maxNoImprovementUpperLevel = 5;
                lMaxNoImprovementUpperLevel = 5;
                numberOfGenerationSubPopulation = 10;
                lNumberOfGenerationSubPopulation = 10;
                p = 1;
                lP = 1;
            } else if (Problem.equals("VRP.p02")) {
                numberOfPlants = 4;
                numberOfDepots = 4;
                lowerBoundProductCost = 1.0;
                upperBoundProductCost = 4.0;
                lowerDeliveryCost = 0.36;
                upperDeliveryCost = 5.36;
                productionsAvailability = new int[numberOfPlants];
                for (int i = 0; i < productionsAvailability.length; i++) {
                    productionsAvailability[i] = 2000;
                }
                plantProductionsAvailability = 2000;
                upperSolutionType = "ArrayInt";
                lowerSolutionType = "Int";
                populationSize = 100;
                lPopulationSize = 100;
                maxFE = 1000;
                lMaxFE = 1000;
                maxNoImprovementUpperLevel = 5;
                lMaxNoImprovementUpperLevel = 5;
                numberOfGenerationSubPopulation = 10;
                lNumberOfGenerationSubPopulation = 10;
                p = 1;
                lP = 1;
            } else if (Problem.equals("VRP.p03")) {
                numberOfPlants = 5;
                numberOfDepots = 5;
                lowerBoundProductCost = 1.0;
                upperBoundProductCost = 4.0;
                lowerDeliveryCost = 0.36;
                upperDeliveryCost = 5.36;
                productionsAvailability = new int[numberOfPlants];
                for (int i = 0; i < productionsAvailability.length; i++) {
                    productionsAvailability[i] = 2000;
                }
                plantProductionsAvailability = 2000;
                upperSolutionType = "ArrayInt";
                lowerSolutionType = "Int";
                populationSize = 100;
                lPopulationSize = 100;
                maxFE = 1000;
                lMaxFE = 1000;
                maxNoImprovementUpperLevel = 5;
                lMaxNoImprovementUpperLevel = 5;
                numberOfGenerationSubPopulation = 10;
                lNumberOfGenerationSubPopulation = 10;
                p = 1;
                lP = 1;
            } else if (Problem.equals("VRP.p04")) {
                numberOfPlants = 2;
                numberOfDepots = 2;
                lowerBoundProductCost = 1.0;
                upperBoundProductCost = 4.0;
                lowerDeliveryCost = 0.36;
                upperDeliveryCost = 5.36;
                productionsAvailability = new int[numberOfPlants];
                for (int i = 0; i < productionsAvailability.length; i++) {
                    productionsAvailability[i] = 2000;
                }
                plantProductionsAvailability = 2000;
                upperSolutionType = "ArrayInt";
                lowerSolutionType = "Int";
                populationSize = 100;
                lPopulationSize = 100;
                maxFE = 1000;
                lMaxFE = 1000;
                maxNoImprovementUpperLevel = 5;
                lMaxNoImprovementUpperLevel = 5;
                numberOfGenerationSubPopulation = 10;
                lNumberOfGenerationSubPopulation = 10;
                p = 1;
                lP = 1;
            } else if (Problem.equals("VRP.p05")) {
                numberOfPlants = 2;
                numberOfDepots = 2;
                lowerBoundProductCost = 1.0;
                upperBoundProductCost = 4.0;
                lowerDeliveryCost = 0.36;
                upperDeliveryCost = 5.36;
                productionsAvailability = new int[numberOfPlants];
                for (int i = 0; i < productionsAvailability.length; i++) {
                    productionsAvailability[i] = 2000;
                }
                plantProductionsAvailability = 2000;
                upperSolutionType = "ArrayInt";
                lowerSolutionType = "Int";
                populationSize = 100;
                lPopulationSize = 100;
                maxFE = 1000;
                lMaxFE = 1000;
                maxNoImprovementUpperLevel = 5;
                lMaxNoImprovementUpperLevel = 5;
                numberOfGenerationSubPopulation = 10;
                lNumberOfGenerationSubPopulation = 10;
                p = 1;
                lP = 1;
            } else if (Problem.equals("VRP.p06")) {
                numberOfPlants = 3;
                numberOfDepots = 3;
                lowerBoundProductCost = 1.0;
                upperBoundProductCost = 4.0;
                lowerDeliveryCost = 0.36;
                upperDeliveryCost = 5.36;
                productionsAvailability = new int[numberOfPlants];
                for (int i = 0; i < productionsAvailability.length; i++) {
                    productionsAvailability[i] = 2000;
                }
                plantProductionsAvailability = 2000;
                upperSolutionType = "ArrayInt";
                lowerSolutionType = "Int";
                populationSize = 100;
                lPopulationSize = 100;
                maxFE = 1000;
                lMaxFE = 1000;
                maxNoImprovementUpperLevel = 5;
                lMaxNoImprovementUpperLevel = 5;
                numberOfGenerationSubPopulation = 10;
                lNumberOfGenerationSubPopulation = 10;
                p = 1;
                lP = 1;
            } else if (Problem.equals("VRP.p07")) {
                numberOfPlants = 4;
                numberOfDepots = 4;
                lowerBoundProductCost = 1.0;
                upperBoundProductCost = 4.0;
                lowerDeliveryCost = 0.36;
                upperDeliveryCost = 5.36;
                productionsAvailability = new int[numberOfPlants];
                for (int i = 0; i < productionsAvailability.length; i++) {
                    productionsAvailability[i] = 2000;
                }
                plantProductionsAvailability = 2000;
                upperSolutionType = "ArrayInt";
                lowerSolutionType = "Int";
                populationSize = 100;
                lPopulationSize = 100;
                maxFE = 1000;
                lMaxFE = 1000;
                maxNoImprovementUpperLevel = 5;
                lMaxNoImprovementUpperLevel = 5;
                numberOfGenerationSubPopulation = 10;
                lNumberOfGenerationSubPopulation = 10;
                p = 1;
                lP = 1;
            } else if (Problem.equals("VRP.p08")) {
                numberOfPlants = 2;
                numberOfDepots = 2;
                lowerBoundProductCost = 1.0;
                upperBoundProductCost = 4.0;
                lowerDeliveryCost = 0.36;
                upperDeliveryCost = 5.36;
                productionsAvailability = new int[numberOfPlants];
                for (int i = 0; i < productionsAvailability.length; i++) {
                    productionsAvailability[i] = 2000;
                }
                plantProductionsAvailability = 2000;
                upperSolutionType = "ArrayInt";
                lowerSolutionType = "Int";
                populationSize = 100;
                lPopulationSize = 100;
                maxFE = 1000;
                lMaxFE = 1000;
                maxNoImprovementUpperLevel = 5;
                lMaxNoImprovementUpperLevel = 5;
                numberOfGenerationSubPopulation = 10;
                lNumberOfGenerationSubPopulation = 10;
                p = 1;
                lP = 1;
            } else if (Problem.equals("VRP.p09")) {
                numberOfPlants = 3;
                numberOfDepots = 3;
                lowerBoundProductCost = 1.0;
                upperBoundProductCost = 4.0;
                lowerDeliveryCost = 0.36;
                upperDeliveryCost = 5.36;
                productionsAvailability = new int[numberOfPlants];
                for (int i = 0; i < productionsAvailability.length; i++) {
                    productionsAvailability[i] = 2000;
                }
                plantProductionsAvailability = 2000;
                upperSolutionType = "ArrayInt";
                lowerSolutionType = "Int";
                populationSize = 100;
                lPopulationSize = 100;
                maxFE = 1000;
                lMaxFE = 1000;
                maxNoImprovementUpperLevel = 5;
                lMaxNoImprovementUpperLevel = 5;
                numberOfGenerationSubPopulation = 10;
                lNumberOfGenerationSubPopulation = 10;
                p = 1;
                lP = 1;
            } else if (Problem.equals("VRP.p10")) {
                numberOfPlants = 4;
                numberOfDepots = 4;
                lowerBoundProductCost = 1.0;
                upperBoundProductCost = 4.0;
                lowerDeliveryCost = 0.36;
                upperDeliveryCost = 5.36;
                productionsAvailability = new int[numberOfPlants];
                for (int i = 0; i < productionsAvailability.length; i++) {
                    productionsAvailability[i] = 2000;
                }
                plantProductionsAvailability = 2000;
                upperSolutionType = "ArrayInt";
                lowerSolutionType = "Int";
                populationSize = 100;
                lPopulationSize = 100;
                maxFE = 1000;
                lMaxFE = 1000;
                maxNoImprovementUpperLevel = 5;
                lMaxNoImprovementUpperLevel = 5;
                numberOfGenerationSubPopulation = 10;
                lNumberOfGenerationSubPopulation = 10;
                p = 1;
                lP = 1;
            } else if (Problem.equals("VRP.p11")) {
                numberOfPlants = 5;
                numberOfDepots = 5;
                lowerBoundProductCost = 1.0;
                upperBoundProductCost = 4.0;
                lowerDeliveryCost = 0.36;
                upperDeliveryCost = 5.36;
                productionsAvailability = new int[numberOfPlants];
                for (int i = 0; i < productionsAvailability.length; i++) {
                    productionsAvailability[i] = 2000;
                }
                plantProductionsAvailability = 2000;
                upperSolutionType = "ArrayInt";
                lowerSolutionType = "Int";
                populationSize = 100;
                lPopulationSize = 100;
                maxFE = 1000;
                lMaxFE = 1000;
                maxNoImprovementUpperLevel = 5;
                lMaxNoImprovementUpperLevel = 5;
                numberOfGenerationSubPopulation = 10;
                lNumberOfGenerationSubPopulation = 10;
                p = 1;
                lP = 1;
            } else if (Problem.equals("VRP.p12")) {
                numberOfPlants = 2;
                numberOfDepots = 2;
                lowerBoundProductCost = 1.0;
                upperBoundProductCost = 4.0;
                lowerDeliveryCost = 0.36;
                upperDeliveryCost = 5.36;
                productionsAvailability = new int[numberOfPlants];
                for (int i = 0; i < productionsAvailability.length; i++) {
                    productionsAvailability[i] = 2000;
                }
                plantProductionsAvailability = 2000;
                upperSolutionType = "ArrayInt";
                lowerSolutionType = "Int";
                populationSize = 100;
                lPopulationSize = 100;
                maxFE = 1000;
                lMaxFE = 1000;
                maxNoImprovementUpperLevel = 5;
                lMaxNoImprovementUpperLevel = 5;
                numberOfGenerationSubPopulation = 10;
                lNumberOfGenerationSubPopulation = 10;
                p = 1;
                lP = 1;
            } else if (Problem.equals("VRP.p13")) {
                numberOfPlants = 2;
                numberOfDepots = 2;
                lowerBoundProductCost = 1.0;
                upperBoundProductCost = 4.0;
                lowerDeliveryCost = 0.36;
                upperDeliveryCost = 5.36;
                productionsAvailability = new int[numberOfPlants];
                for (int i = 0; i < productionsAvailability.length; i++) {
                    productionsAvailability[i] = 2000;
                }
                plantProductionsAvailability = 2000;
                upperSolutionType = "ArrayInt";
                lowerSolutionType = "Int";
                populationSize = 100;
                lPopulationSize = 100;
                maxFE = 2000;
                lMaxFE = 2000;
                maxNoImprovementUpperLevel = 5;
                lMaxNoImprovementUpperLevel = 5;
                numberOfGenerationSubPopulation = 10;
                lNumberOfGenerationSubPopulation = 10;
                p = 1;
                lP = 1;
            } else if (Problem.equals("VRP.p14")) {
                numberOfPlants = 2;
                numberOfDepots = 2;
                lowerBoundProductCost = 1.0;
                upperBoundProductCost = 4.0;
                lowerDeliveryCost = 0.36;
                upperDeliveryCost = 5.36;
                productionsAvailability = new int[numberOfPlants];
                for (int i = 0; i < productionsAvailability.length; i++) {
                    productionsAvailability[i] = 2000;
                }
                plantProductionsAvailability = 2000;
                upperSolutionType = "ArrayInt";
                lowerSolutionType = "Int";
                populationSize = 100;
                lPopulationSize = 100;
                maxFE = 1000;
                lMaxFE = 1000;
                maxNoImprovementUpperLevel = 5;
                lMaxNoImprovementUpperLevel = 5;
                numberOfGenerationSubPopulation = 10;
                lNumberOfGenerationSubPopulation = 10;
                p = 1;
                lP = 1;
            } else if (Problem.equals("VRP.p15")) {
                numberOfPlants = 4;
                numberOfDepots = 4;
                lowerBoundProductCost = 1.0;
                upperBoundProductCost = 4.0;
                lowerDeliveryCost = 0.36;
                upperDeliveryCost = 5.36;
                productionsAvailability = new int[numberOfPlants];
                for (int i = 0; i < productionsAvailability.length; i++) {
                    productionsAvailability[i] = 2000;
                }
                plantProductionsAvailability = 2000;
                upperSolutionType = "ArrayInt";
                lowerSolutionType = "Int";
                populationSize = 100;
                lPopulationSize = 100;
                maxFE = 1000;
                lMaxFE = 1000;
                maxNoImprovementUpperLevel = 5;
                lMaxNoImprovementUpperLevel = 5;
                numberOfGenerationSubPopulation = 10;
                lNumberOfGenerationSubPopulation = 10;
                p = 1;
                lP = 1;
            } else if (Problem.equals("VRP.p16")) {
                numberOfPlants = 4;
                numberOfDepots = 4;
                lowerBoundProductCost = 1.0;
                upperBoundProductCost = 4.0;
                lowerDeliveryCost = 0.36;
                upperDeliveryCost = 5.36;
                productionsAvailability = new int[numberOfPlants];
                for (int i = 0; i < productionsAvailability.length; i++) {
                    productionsAvailability[i] = 2000;
                }
                plantProductionsAvailability = 2000;
                upperSolutionType = "ArrayInt";
                lowerSolutionType = "Int";
                populationSize = 100;
                lPopulationSize = 100;
                maxFE = 1000;
                lMaxFE = 1000;
                maxNoImprovementUpperLevel = 5;
                lMaxNoImprovementUpperLevel = 5;
                numberOfGenerationSubPopulation = 10;
                lNumberOfGenerationSubPopulation = 10;
                p = 1;
                lP = 1;
            } else if (Problem.equals("VRP.p17")) {
                numberOfPlants = 4;
                numberOfDepots = 4;
                lowerBoundProductCost = 1.0;
                upperBoundProductCost = 4.0;
                lowerDeliveryCost = 0.36;
                upperDeliveryCost = 5.36;
                productionsAvailability = new int[numberOfPlants];
                for (int i = 0; i < productionsAvailability.length; i++) {
                    productionsAvailability[i] = 2000;
                }
                plantProductionsAvailability = 2000;
                upperSolutionType = "ArrayInt";
                lowerSolutionType = "Int";
                populationSize = 100;
                lPopulationSize = 100;
                maxFE = 1000;
                lMaxFE = 1000;
                maxNoImprovementUpperLevel = 5;
                lMaxNoImprovementUpperLevel = 5;
                numberOfGenerationSubPopulation = 10;
                lNumberOfGenerationSubPopulation = 10;
                p = 1;
                lP = 1;
            } else if (Problem.equals("VRP.p18")) {
                numberOfPlants = 6;
                numberOfDepots = 6;
                lowerBoundProductCost = 1.0;
                upperBoundProductCost = 4.0;
                lowerDeliveryCost = 0.36;
                upperDeliveryCost = 5.36;
                productionsAvailability = new int[numberOfPlants];
                for (int i = 0; i < productionsAvailability.length; i++) {
                    productionsAvailability[i] = 2000;
                }
                plantProductionsAvailability = 2000;
                upperSolutionType = "ArrayInt";
                lowerSolutionType = "Int";
                populationSize = 100;
                lPopulationSize = 100;
                maxFE = 1000;
                lMaxFE = 1000;
                maxNoImprovementUpperLevel = 5;
                lMaxNoImprovementUpperLevel = 5;
                numberOfGenerationSubPopulation = 10;
                lNumberOfGenerationSubPopulation = 10;
                p = 1;
                lP = 1;
            } else if (Problem.equals("VRP.p19")) {
                numberOfPlants = 6;
                numberOfDepots = 6;
                lowerBoundProductCost = 1.0;
                upperBoundProductCost = 4.0;
                lowerDeliveryCost = 0.36;
                upperDeliveryCost = 5.36;
                productionsAvailability = new int[numberOfPlants];
                for (int i = 0; i < productionsAvailability.length; i++) {
                    productionsAvailability[i] = 2000;
                }
                plantProductionsAvailability = 2000;
                upperSolutionType = "ArrayInt";
                lowerSolutionType = "Int";
                populationSize = 100;
                lPopulationSize = 100;
                maxFE = 1000;
                lMaxFE = 1000;
                maxNoImprovementUpperLevel = 5;
                lMaxNoImprovementUpperLevel = 5;
                numberOfGenerationSubPopulation = 10;
                lNumberOfGenerationSubPopulation = 10;
                p = 1;
                lP = 1;
            } else if (Problem.equals("VRP.p20")) {
                numberOfPlants = 6;
                numberOfDepots = 6;
                lowerBoundProductCost = 1.0;
                upperBoundProductCost = 4.0;
                lowerDeliveryCost = 0.36;
                upperDeliveryCost = 5.36;
                productionsAvailability = new int[numberOfPlants];
                for (int i = 0; i < productionsAvailability.length; i++) {
                    productionsAvailability[i] = 2000;
                }
                plantProductionsAvailability = 2000;
                upperSolutionType = "ArrayInt";
                lowerSolutionType = "Int";
                populationSize = 100;
                lPopulationSize = 100;
                maxFE = 1000;
                lMaxFE = 1000;
                maxNoImprovementUpperLevel = 5;
                lMaxNoImprovementUpperLevel = 5;
                numberOfGenerationSubPopulation = 10;
                lNumberOfGenerationSubPopulation = 10;
                p = 1;
                lP = 1;
            } else if (Problem.equals("VRP.p21")) {
                numberOfPlants = 9;
                numberOfDepots = 9;
                lowerBoundProductCost = 1.0;
                upperBoundProductCost = 4.0;
                lowerDeliveryCost = 0.36;
                upperDeliveryCost = 5.36;
                productionsAvailability = new int[numberOfPlants];
                for (int i = 0; i < productionsAvailability.length; i++) {
                    productionsAvailability[i] = 2000;
                }
                plantProductionsAvailability = 2000;
                upperSolutionType = "ArrayInt";
                lowerSolutionType = "Int";
                populationSize = 100;
                lPopulationSize = 100;
                maxFE = 1000;
                lMaxFE = 1000;
                maxNoImprovementUpperLevel = 1;
                lMaxNoImprovementUpperLevel = 1;
                numberOfGenerationSubPopulation = 5;
                lNumberOfGenerationSubPopulation = 5;
                p = 1;
                lP = 1;
            } else if (Problem.equals("VRP._A-n32-k5.vrp")) {
                numberOfPlants = 4;
                numberOfDepots = 4;
                lowerBoundProductCost = 1.0;
                upperBoundProductCost = 4.0;
                lowerDeliveryCost = 0.36;
                upperDeliveryCost = 5.36;
                productionsAvailability = new int[numberOfPlants];
                for (int i = 0; i < productionsAvailability.length; i++) {
                    productionsAvailability[i] = 2000;
                }
                plantProductionsAvailability = 2000;
                upperSolutionType = "ArrayInt";
                lowerSolutionType = "Int";
                populationSize = 100;
                lPopulationSize = 100;
                maxFE = 2500;
                lMaxFE = 2500;
                maxNoImprovementUpperLevel = 5;
                lMaxNoImprovementUpperLevel = 5;
                numberOfGenerationSubPopulation = 10;
                lNumberOfGenerationSubPopulation = 10;
                p = 1;
                lP = 1;
            } else if (Problem.equals("VRP._A-n45-k6.vrp")) {
                numberOfPlants = 4;
                numberOfDepots = 4;
                lowerBoundProductCost = 1.0;
                upperBoundProductCost = 4.0;
                lowerDeliveryCost = 0.36;
                upperDeliveryCost = 5.36;
                productionsAvailability = new int[numberOfPlants];
                for (int i = 0; i < productionsAvailability.length; i++) {
                    productionsAvailability[i] = 2000;
                }
                plantProductionsAvailability = 2000;
                upperSolutionType = "ArrayInt";
                lowerSolutionType = "Int";
                populationSize = 100;
                lPopulationSize = 100;
                maxFE = 2000;
                lMaxFE = 2000;
                maxNoImprovementUpperLevel = 5;
                lMaxNoImprovementUpperLevel = 5;
                numberOfGenerationSubPopulation = 10;
                lNumberOfGenerationSubPopulation = 10;
                p = 1;
                lP = 1;
            } else if (Problem.equals("VRP._A-n80-k10.vrp")) {
                numberOfPlants = 4;
                numberOfDepots = 4;
                lowerBoundProductCost = 1.0;
                upperBoundProductCost = 4.0;
                lowerDeliveryCost = 0.36;
                upperDeliveryCost = 5.36;
                productionsAvailability = new int[numberOfPlants];
                for (int i = 0; i < productionsAvailability.length; i++) {
                    productionsAvailability[i] = 2000;
                }
                plantProductionsAvailability = 2000;
                upperSolutionType = "ArrayInt";
                lowerSolutionType = "Int";
                populationSize = 100;
                lPopulationSize = 100;
                maxFE = 2000;
                lMaxFE = 2000;
                maxNoImprovementUpperLevel = 5;
                lMaxNoImprovementUpperLevel = 5;
                numberOfGenerationSubPopulation = 10;
                lNumberOfGenerationSubPopulation = 10;
                p = 1;
                lP = 1;
            } else if (Problem.equals("VRP._B-n57-k7.vrp")) {
                numberOfPlants = 4;
                numberOfDepots = 4;
                lowerBoundProductCost = 1.0;
                upperBoundProductCost = 4.0;
                lowerDeliveryCost = 0.36;
                upperDeliveryCost = 5.36;
                productionsAvailability = new int[numberOfPlants];
                for (int i = 0; i < productionsAvailability.length; i++) {
                    productionsAvailability[i] = 2000;
                }
                plantProductionsAvailability = 2000;
                upperSolutionType = "ArrayInt";
                lowerSolutionType = "Int";
                populationSize = 100;
                lPopulationSize = 100;
                maxFE = 2500;
                lMaxFE = 2500;
                maxNoImprovementUpperLevel = 5;
                lMaxNoImprovementUpperLevel = 5;
                numberOfGenerationSubPopulation = 10;
                lNumberOfGenerationSubPopulation = 10;
                p = 1;
                lP = 1;
            } else if (Problem.equals("VRP._B-n68-k9.vrp")) {
                numberOfPlants = 4;
                numberOfDepots = 4;
                lowerBoundProductCost = 1.0;
                upperBoundProductCost = 4.0;
                lowerDeliveryCost = 0.36;
                upperDeliveryCost = 5.36;
                productionsAvailability = new int[numberOfPlants];
                for (int i = 0; i < productionsAvailability.length; i++) {
                    productionsAvailability[i] = 2000;
                }
                plantProductionsAvailability = 2000;
                upperSolutionType = "ArrayInt";
                lowerSolutionType = "Int";
                populationSize = 100;
                lPopulationSize = 100;
                maxFE = 2000;
                lMaxFE = 2000;
                maxNoImprovementUpperLevel = 5;
                lMaxNoImprovementUpperLevel = 5;
                numberOfGenerationSubPopulation = 10;
                lNumberOfGenerationSubPopulation = 10;
                p = 1;
                lP = 1;
            }
        }

        public int getNumberOfPlants() {
            return numberOfPlants;
        }

        public void setNumberOfPlants(int numberOfPlants) {
            this.numberOfPlants = numberOfPlants;
        }

        public int getNumberOfDepots() {
            return numberOfDepots;
        }

        public void setNumberOfDepots(int numberOfDepots) {
            this.numberOfDepots = numberOfDepots;
        }

        public double getLowerBoundProductCost() {
            return lowerBoundProductCost;
        }

        public void setLowerBoundProductCost(double lowerBoundProductCost) {
            this.lowerBoundProductCost = lowerBoundProductCost;
        }

        public double getUpperBoundProductCost() {
            return upperBoundProductCost;
        }

        public void setUpperBoundProductCost(double upperBoundProductCost) {
            this.upperBoundProductCost = upperBoundProductCost;
        }

        public double getLowerDeliveryCost() {
            return lowerDeliveryCost;
        }

        public void setLowerDeliveryCost(double lowerDeliveryCost) {
            this.lowerDeliveryCost = lowerDeliveryCost;
        }

        public double getUpperDeliveryCost() {
            return upperDeliveryCost;
        }

        public void setUpperDeliveryCost(double upperDeliveryCost) {
            this.upperDeliveryCost = upperDeliveryCost;
        }

        public int[] getProductionsAvailability() {
            return productionsAvailability;
        }

        public void setProductionsAvailability(int[] productionsAvailability) {
            this.productionsAvailability = productionsAvailability;
        }

        public String getUpperSolutionType() {
            return upperSolutionType;
        }

        public void setUpperSolutionType(String upperSolutionType) {
            this.upperSolutionType = upperSolutionType;
        }

        public String getLowerSolutionType() {
            return lowerSolutionType;
        }

        public void setLowerSolutionType(String lowerSolutionType) {
            this.lowerSolutionType = lowerSolutionType;
        }

        public int getPopulationSize() {
            return populationSize;
        }

        public void setPopulationSize(int populationSize) {
            this.populationSize = populationSize;
        }

        public int getlPopulationSize() {
            return lPopulationSize;
        }

        public void setlPopulationSize(int lPopulationSize) {
            this.lPopulationSize = lPopulationSize;
        }

        public int getMaxFE() {
            return maxFE;
        }

        public void setMaxFE(int maxFE) {
            this.maxFE = maxFE;
        }

        public int getlMaxFE() {
            return lMaxFE;
        }

        public void setlMaxFE(int lMaxFE) {
            this.lMaxFE = lMaxFE;
        }

        public int getMaxNoImprovementUpperLevel() {
            return maxNoImprovementUpperLevel;
        }

        public void setMaxNoImprovementUpperLevel(int maxNoImprovementUpperLevel) {
            this.maxNoImprovementUpperLevel = maxNoImprovementUpperLevel;
        }

        public int getlMaxNoImprovementUpperLevel() {
            return lMaxNoImprovementUpperLevel;
        }

        public void setlMaxNoImprovementUpperLevel(int lMaxNoImprovementUpperLevel) {
            this.lMaxNoImprovementUpperLevel = lMaxNoImprovementUpperLevel;
        }

        public int getNumberOfGenerationSubPopulation() {
            return numberOfGenerationSubPopulation;
        }

        public void setNumberOfGenerationSubPopulation(int numberOfGenerationSubPopulation) {
            this.numberOfGenerationSubPopulation = numberOfGenerationSubPopulation;
        }

        public int getlNumberOfGenerationSubPopulation() {
            return lNumberOfGenerationSubPopulation;
        }

        public void setlNumberOfGenerationSubPopulation(int lNumberOfGenerationSubPopulation) {
            this.lNumberOfGenerationSubPopulation = lNumberOfGenerationSubPopulation;
        }

        public int getP() {
            return p;
        }

        public void setP(int p) {
            this.p = p;
        }

        public int getlP() {
            return lP;
        }

        public void setlP(int lP) {
            this.lP = lP;
        }

        private int getPlantProductionsAvailability() {
            return plantProductionsAvailability;
        }
    }
}
