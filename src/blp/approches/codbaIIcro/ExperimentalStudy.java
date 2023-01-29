package blp.approches.codbaIIcro;

import blp.core.Operator;
import blp.core.Problem;
import blp.core.Solution;
import blp.operators.cro.Decomposition;
import blp.operators.cro.InterMolecular;
import blp.operators.cro.OnWall;
import blp.operators.cro.Synthesis;
import blp.problems.VRP;
import blp.problems.parameters.Parameters;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Abir
 */
public class ExperimentalStudy {

    public static void main(String[] args) {
        String[] problems = {"VRP"};
        String[] instances = {/*"pr01",*/ "pr02"/*, "pr03","pr04","pr05", "pr06"/*,"pr07", "pr08", "pr09", "pr10","p01", "p02", "p03", "p04", "p05", "p06", "p07", "p08", "p09", "p10","p11", "p12", "p13", "p14", "p15", "p16", "p17", "p18", "p19", "p20""p21"*/};
        String experimentName = "CODBAIICRO";
        String experimentBaseDirectory = System.getProperty("user.dir") + System.getProperty("file.separator") + "ExprimentalStudy" + System.getProperty("file.separator") + experimentName;
        int independentRuns = 8;
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
                        CODBAIICRO codbaiicro = initProblem(problem, instance, prb);
                        System.out.print("Running : " + problem + "/" + instance + " run : " + (i + 1) + " Please wait ...");
                        long initTime = System.currentTimeMillis();
                        Solution upperLevelSolution = codbaiicro.execute().get(0);
                        long estimatedTime = System.currentTimeMillis() - initTime;
                        Solution lowerLevelSolution = codbaiicro.getLowerSolution();
                        int directRationality = codbaiicro.getDirectRationality();
                        double weightedRationality = codbaiicro.getWeightedRationality();
                        int upperFE = codbaiicro.getUpperFE();
                        int lowerFE = codbaiicro.getLowerFE();
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

    private static CODBAIICRO initProblem(String problem, String instance, ProblemInit prb) {
        // Problem initialization
        Parameters.lowerBoundProductionCost = prb.getLowerBoundProductCost();
        Parameters.upperBoundProductionCost = prb.getUpperBoundProductCost();
        Parameters.lowerBoundDeleveryCost = prb.getLowerDeliveryCost();
        Parameters.upperBoundDeleveryCost = prb.getUpperDeliveryCost();
        Parameters.plantProductionsAvailability = prb.getPlantProductionsAvailability();
        Problem prob = new VRP(instance, prb.getUpperSolutionType(), prb.getLowerSolutionType());
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("populationSize", prb.getPopulationSize());
        parameters.put("lPopulationSize", prb.getlPopulationSize());
        parameters.put("maxNoImprovementUpperLevel", prb.getMaxNoImprovementUpperLevel());
        parameters.put("lMaxNoImprovementLowerLevel", prb.getlMaxNoImprovementUpperLevel());
        parameters.put("numberOfGenerationSubPopulation", prb.getNumberOfGenerationSubPopulation());
        parameters.put("lNumberOfGenerationSubPopulation", prb.getlNumberOfGenerationSubPopulation());
        parameters.put("p", prb.getP());
        parameters.put("lP", prb.getlP());
        parameters.put("collR", prb.getCollR());
        parameters.put("lCollR", prb.getlCollR());
        parameters.put("decThres", prb.getDecThres());
        parameters.put("lDecThres", prb.getlDecThres());
        parameters.put("synThres", prb.getSynThres());
        parameters.put("lSynThres", prb.getlSynThres());
        parameters.put("enBuff", prb.getEnBuff());
        parameters.put("lEnBuff", prb.getlEnBuff());
        parameters.put("lossR", prb.getLossR());
        parameters.put("lLossR", prb.getlLossR());

        HashMap<String, Object> uOnWallParameters = new HashMap<>();
        uOnWallParameters.put("parentIndex", new ArrayList<>());
        uOnWallParameters.put("level", "upper");
        uOnWallParameters.put("iniKE", prb.getIniKE());
        HashMap<String, Object> uDecParameters = new HashMap<>();
        uDecParameters.put("parentIndex", new ArrayList<>());
        uDecParameters.put("level", "upper");
        HashMap<String, Object> uInterParameters = new HashMap<>();
        uInterParameters.put("parentIndex", new ArrayList<>());
        uInterParameters.put("level", "upper");
        HashMap<String, Object> uSynthParameters = new HashMap<>();
        uSynthParameters.put("parentIndex", new ArrayList<>());
        uSynthParameters.put("level", "upper");

        HashMap<String, Object> lOnWallParameters = new HashMap<>();
        lOnWallParameters.put("parentIndex", new ArrayList<>());
        lOnWallParameters.put("level", "lower");
        lOnWallParameters.put("iniKE", prb.getlIniKE());
        HashMap<String, Object> lDecParameters = new HashMap<>();
        lDecParameters.put("parentIndex", new ArrayList<>());
        lDecParameters.put("level", "lower");
        HashMap<String, Object> lInterParameters = new HashMap<>();
        lInterParameters.put("parentIndex", new ArrayList<>());
        lInterParameters.put("level", "lower");
        HashMap<String, Object> lSynthParameters = new HashMap<>();
        lSynthParameters.put("parentIndex", new ArrayList<>());
        lSynthParameters.put("level", "lower");

        HashMap<String, Operator> operators = new HashMap<>();
        operators.put("decomposition", new Decomposition(uDecParameters));
        operators.put("onWall", new OnWall(uOnWallParameters));
        operators.put("synthesis", new Synthesis(uSynthParameters));
        operators.put("interMolecular", new InterMolecular(uInterParameters));
        operators.put("neighborhood", new OnWall(uOnWallParameters));
        operators.put("coEvolutionCrossover", new Synthesis(uSynthParameters));

        operators.put("lDecomposition", new Decomposition(lDecParameters));
        operators.put("lOnWall", new OnWall(lOnWallParameters));
        operators.put("lSynthesis", new Synthesis(lSynthParameters));
        operators.put("lInterMolecular", new InterMolecular(lInterParameters));
        operators.put("lNeighborhood", new OnWall(lOnWallParameters));
        operators.put("lCoEvolutionCrossover", new Synthesis(lSynthParameters));

        CODBAIICRO algorithm = new CODBAIICRO(prob);
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
        private int maxNoImprovementUpperLevel;
        private int lMaxNoImprovementUpperLevel;
        private int numberOfGenerationSubPopulation;
        private int lNumberOfGenerationSubPopulation;
        private int p;
        private int lP;
        private double collR;
        private double lCollR;
        private int decThres;
        private int lDecThres;
        private double synThres;
        private double lSynThres;
        private double enBuff;
        private double lEnBuff;
        private double lossR;
        private double lLossR;
        private double iniKE;
        private double lIniKE;

        public ProblemInit(String Problem) {
            if (Problem.equals("VRP.pr01")) {
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
                maxNoImprovementUpperLevel = 1;
                lMaxNoImprovementUpperLevel = 1;
                numberOfGenerationSubPopulation = 5;
                lNumberOfGenerationSubPopulation = 5;
                p = 1;
                lP = 1;
                collR = 0.7;
                lCollR = 0.7;
                decThres = 10;
                lDecThres = 10;
                synThres = 10.0;
                lSynThres = 10.0;
                enBuff = 0.0;
                lEnBuff = 0.0;
                lossR = 0.4;
                lLossR = 0.4;
                iniKE = 10000;
                lIniKE = 10000;
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
                maxNoImprovementUpperLevel = 1;
                lMaxNoImprovementUpperLevel = 1;
                numberOfGenerationSubPopulation = 7;
                lNumberOfGenerationSubPopulation = 7;
                p = 1;
                lP = 1;
                collR = 0.7;
                lCollR = 0.7;
                decThres = 10;
                lDecThres = 10;
                synThres = 10.0;
                lSynThres = 10.0;
                enBuff = 0.0;
                lEnBuff = 0.0;
                lossR = 0.4;
                lLossR = 0.4;
                iniKE = 10000;
                lIniKE = 10000;
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
                maxNoImprovementUpperLevel = 1;
                lMaxNoImprovementUpperLevel = 1;
                numberOfGenerationSubPopulation = 5;
                lNumberOfGenerationSubPopulation = 5;
                p = 1;
                lP = 1;
                collR = 0.7;
                lCollR = 0.7;
                decThres = 10;
                lDecThres = 10;
                synThres = 10.0;
                lSynThres = 10.0;
                enBuff = 0.0;
                lEnBuff = 0.0;
                lossR = 0.4;
                lLossR = 0.4;
                iniKE = 10000;
                lIniKE = 10000;
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
                maxNoImprovementUpperLevel = 1;
                lMaxNoImprovementUpperLevel = 1;
                numberOfGenerationSubPopulation = 5;
                lNumberOfGenerationSubPopulation = 5;
                p = 1;
                lP = 1;
                collR = 0.7;
                lCollR = 0.7;
                decThres = 10;
                lDecThres = 10;
                synThres = 10.0;
                lSynThres = 10.0;
                enBuff = 0.0;
                lEnBuff = 0.0;
                lossR = 0.4;
                lLossR = 0.4;
                iniKE = 10000;
                lIniKE = 10000;
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
                maxNoImprovementUpperLevel = 1;
                lMaxNoImprovementUpperLevel = 1;
                numberOfGenerationSubPopulation = 5;
                lNumberOfGenerationSubPopulation = 5;
                p = 1;
                lP = 1;
                collR = 0.7;
                lCollR = 0.7;
                decThres = 10;
                lDecThres = 10;
                synThres = 10.0;
                lSynThres = 10.0;
                enBuff = 0.0;
                lEnBuff = 0.0;
                lossR = 0.4;
                lLossR = 0.4;
                iniKE = 10000;
                lIniKE = 10000;
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
                maxNoImprovementUpperLevel = 1;
                lMaxNoImprovementUpperLevel = 1;
                numberOfGenerationSubPopulation = 7;
                lNumberOfGenerationSubPopulation = 7;
                p = 1;
                lP = 1;
                collR = 0.7;
                lCollR = 0.7;
                decThres = 10;
                lDecThres = 10;
                synThres = 10.0;
                lSynThres = 10.0;
                enBuff = 0.0;
                lEnBuff = 0.0;
                lossR = 0.4;
                lLossR = 0.4;
                iniKE = 10000;
                lIniKE = 10000;
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
                maxNoImprovementUpperLevel = 1;
                lMaxNoImprovementUpperLevel = 1;
                numberOfGenerationSubPopulation = 5;
                lNumberOfGenerationSubPopulation = 5;
                p = 1;
                lP = 1;
                collR = 0.7;
                lCollR = 0.7;
                decThres = 10;
                lDecThres = 10;
                synThres = 10.0;
                lSynThres = 10.0;
                enBuff = 0.0;
                lEnBuff = 0.0;
                lossR = 0.4;
                lLossR = 0.4;
                iniKE = 10000;
                lIniKE = 10000;
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
                maxNoImprovementUpperLevel = 1;
                lMaxNoImprovementUpperLevel = 1;
                numberOfGenerationSubPopulation = 5;
                lNumberOfGenerationSubPopulation = 5;
                p = 1;
                lP = 1;
                collR = 0.7;
                lCollR = 0.7;
                decThres = 10;
                lDecThres = 10;
                synThres = 10.0;
                lSynThres = 10.0;
                enBuff = 0.0;
                lEnBuff = 0.0;
                lossR = 0.4;
                lLossR = 0.4;
                iniKE = 10000;
                lIniKE = 10000;
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
                maxNoImprovementUpperLevel = 1;
                lMaxNoImprovementUpperLevel = 1;
                numberOfGenerationSubPopulation = 5;
                lNumberOfGenerationSubPopulation = 5;
                p = 1;
                lP = 1;
                collR = 0.7;
                lCollR = 0.7;
                decThres = 10;
                lDecThres = 10;
                synThres = 10.0;
                lSynThres = 10.0;
                enBuff = 0.0;
                lEnBuff = 0.0;
                lossR = 0.4;
                lLossR = 0.4;
                iniKE = 10000;
                lIniKE = 10000;
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
                maxNoImprovementUpperLevel = 1;
                lMaxNoImprovementUpperLevel = 1;
                numberOfGenerationSubPopulation = 5;
                lNumberOfGenerationSubPopulation = 5;
                p = 1;
                lP = 1;
                collR = 0.7;
                lCollR = 0.7;
                decThres = 10;
                lDecThres = 10;
                synThres = 10.0;
                lSynThres = 10.0;
                enBuff = 0.0;
                lEnBuff = 0.0;
                lossR = 0.4;
                lLossR = 0.4;
                iniKE = 10000;
                lIniKE = 10000;
            }
            else if (Problem.equals("VRP.p01")) {
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
                maxNoImprovementUpperLevel = 1;
                lMaxNoImprovementUpperLevel = 1;
                numberOfGenerationSubPopulation = 5;
                lNumberOfGenerationSubPopulation = 5;
                p = 1;
                lP = 1;
                collR = 0.7;
                lCollR = 0.7;
                decThres = 10;
                lDecThres = 10;
                synThres = 10.0;
                lSynThres = 10.0;
                enBuff = 0.0;
                lEnBuff = 0.0;
                lossR = 0.4;
                lLossR = 0.4;
                iniKE = 10000;
                lIniKE = 10000;
            }
            else if (Problem.equals("VRP.p02")) {
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
                maxNoImprovementUpperLevel = 1;
                lMaxNoImprovementUpperLevel = 1;
                numberOfGenerationSubPopulation = 5;
                lNumberOfGenerationSubPopulation = 5;
                p = 1;
                lP = 1;
                collR = 0.7;
                lCollR = 0.7;
                decThres = 10;
                lDecThres = 10;
                synThres = 10.0;
                lSynThres = 10.0;
                enBuff = 0.0;
                lEnBuff = 0.0;
                lossR = 0.4;
                lLossR = 0.4;
                iniKE = 10000;
                lIniKE = 10000;
            }
            else if (Problem.equals("VRP.p03")) {
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
                maxNoImprovementUpperLevel = 1;
                lMaxNoImprovementUpperLevel = 1;
                numberOfGenerationSubPopulation = 5;
                lNumberOfGenerationSubPopulation = 5;
                p = 1;
                lP = 1;
                collR = 0.7;
                lCollR = 0.7;
                decThres = 10;
                lDecThres = 10;
                synThres = 10.0;
                lSynThres = 10.0;
                enBuff = 0.0;
                lEnBuff = 0.0;
                lossR = 0.4;
                lLossR = 0.4;
                iniKE = 10000;
                lIniKE = 10000;
            }
             else if (Problem.equals("VRP.p04")) {
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
                maxNoImprovementUpperLevel = 1;
                lMaxNoImprovementUpperLevel = 1;
                numberOfGenerationSubPopulation = 5;
                lNumberOfGenerationSubPopulation = 5;
                p = 1;
                lP = 1;
                collR = 0.7;
                lCollR = 0.7;
                decThres = 10;
                lDecThres = 10;
                synThres = 10.0;
                lSynThres = 10.0;
                enBuff = 0.0;
                lEnBuff = 0.0;
                lossR = 0.4;
                lLossR = 0.4;
                iniKE = 10000;
                lIniKE = 10000;
            }
            else if (Problem.equals("VRP.p05")) {
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
                maxNoImprovementUpperLevel = 1;
                lMaxNoImprovementUpperLevel = 1;
                numberOfGenerationSubPopulation = 5;
                lNumberOfGenerationSubPopulation = 5;
                p = 1;
                lP = 1;
                collR = 0.7;
                lCollR = 0.7;
                decThres = 10;
                lDecThres = 10;
                synThres = 10.0;
                lSynThres = 10.0;
                enBuff = 0.0;
                lEnBuff = 0.0;
                lossR = 0.4;
                lLossR = 0.4;
                iniKE = 10000;
                lIniKE = 10000;
            }
             else if (Problem.equals("VRP.p06")) {
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
                maxNoImprovementUpperLevel = 1;
                lMaxNoImprovementUpperLevel = 1;
                numberOfGenerationSubPopulation = 5;
                lNumberOfGenerationSubPopulation = 5;
                p = 1;
                lP = 1;
                collR = 0.7;
                lCollR = 0.7;
                decThres = 10;
                lDecThres = 10;
                synThres = 10.0;
                lSynThres = 10.0;
                enBuff = 0.0;
                lEnBuff = 0.0;
                lossR = 0.4;
                lLossR = 0.4;
                iniKE = 10000;
                lIniKE = 10000;
            }
             else if (Problem.equals("VRP.p07")) {
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
                maxNoImprovementUpperLevel = 1;
                lMaxNoImprovementUpperLevel = 1;
                numberOfGenerationSubPopulation = 5;
                lNumberOfGenerationSubPopulation = 5;
                p = 1;
                lP = 1;
                collR = 0.7;
                lCollR = 0.7;
                decThres = 10;
                lDecThres = 10;
                synThres = 10.0;
                lSynThres = 10.0;
                enBuff = 0.0;
                lEnBuff = 0.0;
                lossR = 0.4;
                lLossR = 0.4;
                iniKE = 10000;
                lIniKE = 10000;
            }
             else if (Problem.equals("VRP.p08")) {
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
                maxNoImprovementUpperLevel = 1;
                lMaxNoImprovementUpperLevel = 1;
                numberOfGenerationSubPopulation = 5;
                lNumberOfGenerationSubPopulation = 5;
                p = 1;
                lP = 1;
                collR = 0.7;
                lCollR = 0.7;
                decThres = 10;
                lDecThres = 10;
                synThres = 10.0;
                lSynThres = 10.0;
                enBuff = 0.0;
                lEnBuff = 0.0;
                lossR = 0.4;
                lLossR = 0.4;
                iniKE = 10000;
                lIniKE = 10000;
            }
             else if (Problem.equals("VRP.p09")) {
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
                maxNoImprovementUpperLevel = 1;
                lMaxNoImprovementUpperLevel = 1;
                numberOfGenerationSubPopulation = 5;
                lNumberOfGenerationSubPopulation = 5;
                p = 1;
                lP = 1;
                collR = 0.7;
                lCollR = 0.7;
                decThres = 10;
                lDecThres = 10;
                synThres = 10.0;
                lSynThres = 10.0;
                enBuff = 0.0;
                lEnBuff = 0.0;
                lossR = 0.4;
                lLossR = 0.4;
                iniKE = 10000;
                lIniKE = 10000;
            }
            else if (Problem.equals("VRP.p10")) {
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
                maxNoImprovementUpperLevel = 1;
                lMaxNoImprovementUpperLevel = 1;
                numberOfGenerationSubPopulation = 5;
                lNumberOfGenerationSubPopulation = 5;
                p = 1;
                lP = 1;
                collR = 0.8;
                lCollR = 0.8;
                decThres = 10;
                lDecThres = 10;
                synThres = 10.0;
                lSynThres = 10.0;
                enBuff = 0.0;
                lEnBuff = 0.0;
                lossR = 0.6;
                lLossR = 0.6;
                iniKE = 10000;
                lIniKE = 10000;
            }
             else if (Problem.equals("VRP.p11")) {
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
                maxNoImprovementUpperLevel = 1;
                lMaxNoImprovementUpperLevel = 1;
                numberOfGenerationSubPopulation = 5;
                lNumberOfGenerationSubPopulation = 5;
                p = 1;
                lP = 1;
                collR = 0.8;
                lCollR = 0.8;
                decThres = 10;
                lDecThres = 10;
                synThres = 10.0;
                lSynThres = 10.0;
                enBuff = 0.0;
                lEnBuff = 0.0;
                lossR = 0.6;
                lLossR = 0.6;
                iniKE = 10000;
                lIniKE = 10000;
            }
             else if (Problem.equals("VRP.p12")) {
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
                maxNoImprovementUpperLevel = 1;
                lMaxNoImprovementUpperLevel = 1;
                numberOfGenerationSubPopulation = 5;
                lNumberOfGenerationSubPopulation = 5;
                p = 1;
                lP = 1;
                collR = 0.8;
                lCollR = 0.8;
                decThres = 10;
                lDecThres = 10;
                synThres = 10.0;
                lSynThres = 10.0;
                enBuff = 0.0;
                lEnBuff = 0.0;
                lossR = 0.6;
                lLossR = 0.6;
                iniKE = 10000;
                lIniKE = 10000;
            }
             else if (Problem.equals("VRP.p13")) {
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
                maxNoImprovementUpperLevel = 1;
                lMaxNoImprovementUpperLevel = 1;
                numberOfGenerationSubPopulation = 5;
                lNumberOfGenerationSubPopulation = 5;
                p = 1;
                lP = 1;
                collR = 0.8;
                lCollR = 0.8;
                decThres = 10;
                lDecThres = 10;
                synThres = 10.0;
                lSynThres = 10.0;
                enBuff = 0.0;
                lEnBuff = 0.0;
                lossR = 0.6;
                lLossR = 0.6;
                iniKE = 10000;
                lIniKE = 10000;
            }
             else if (Problem.equals("VRP.p14")) {
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
                maxNoImprovementUpperLevel = 1;
                lMaxNoImprovementUpperLevel = 1;
                numberOfGenerationSubPopulation = 5;
                lNumberOfGenerationSubPopulation = 5;
                p = 1;
                lP = 1;
                collR = 0.8;
                lCollR = 0.8;
                decThres = 10;
                lDecThres = 10;
                synThres = 10.0;
                lSynThres = 10.0;
                enBuff = 0.0;
                lEnBuff = 0.0;
                lossR = 0.6;
                lLossR = 0.6;
                iniKE = 10000;
                lIniKE = 10000;
            }
             else if (Problem.equals("VRP.p15")) {
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
                maxNoImprovementUpperLevel = 1;
                lMaxNoImprovementUpperLevel = 1;
                numberOfGenerationSubPopulation = 5;
                lNumberOfGenerationSubPopulation = 5;
                p = 1;
                lP = 1;
                collR = 0.8;
                lCollR = 0.8;
                decThres = 10;
                lDecThres = 10;
                synThres = 10.0;
                lSynThres = 10.0;
                enBuff = 0.0;
                lEnBuff = 0.0;
                lossR = 0.6;
                lLossR = 0.6;
                iniKE = 10000;
                lIniKE = 10000;
            }
             else if (Problem.equals("VRP.p16")) {
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
                maxNoImprovementUpperLevel = 1;
                lMaxNoImprovementUpperLevel = 1;
                numberOfGenerationSubPopulation = 5;
                lNumberOfGenerationSubPopulation = 5;
                p = 1;
                lP = 1;
                collR = 0.8;
                lCollR = 0.8;
                decThres = 10;
                lDecThres = 10;
                synThres = 10.0;
                lSynThres = 10.0;
                enBuff = 0.0;
                lEnBuff = 0.0;
                lossR = 0.6;
                lLossR = 0.6;
                iniKE = 10000;
                lIniKE = 10000;
            }
             else if (Problem.equals("VRP.p17")) {
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
                maxNoImprovementUpperLevel = 1;
                lMaxNoImprovementUpperLevel = 1;
                numberOfGenerationSubPopulation = 5;
                lNumberOfGenerationSubPopulation = 5;
                p = 1;
                lP = 1;
                collR = 0.8;
                lCollR = 0.8;
                decThres = 10;
                lDecThres = 10;
                synThres = 10.0;
                lSynThres = 10.0;
                enBuff = 0.0;
                lEnBuff = 0.0;
                lossR = 0.6;
                lLossR = 0.6;
                iniKE = 10000;
                lIniKE = 10000;
            }
             else if (Problem.equals("VRP.p18")) {
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
                maxNoImprovementUpperLevel = 1;
                lMaxNoImprovementUpperLevel = 1;
                numberOfGenerationSubPopulation = 5;
                lNumberOfGenerationSubPopulation = 5;
                p = 1;
                lP = 1;
                collR = 0.8;
                lCollR = 0.8;
                decThres = 10;
                lDecThres = 10;
                synThres = 10.0;
                lSynThres = 10.0;
                enBuff = 0.0;
                lEnBuff = 0.0;
                lossR = 0.6;
                lLossR = 0.6;
                iniKE = 10000;
                lIniKE = 10000;
            }
             else if (Problem.equals("VRP.p19")) {
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
                maxNoImprovementUpperLevel = 1;
                lMaxNoImprovementUpperLevel = 1;
                numberOfGenerationSubPopulation = 5;
                lNumberOfGenerationSubPopulation = 5;
                p = 1;
                lP = 1;
                collR = 0.8;
                lCollR = 0.8;
                decThres = 10;
                lDecThres = 10;
                synThres = 10.0;
                lSynThres = 10.0;
                enBuff = 0.0;
                lEnBuff = 0.0;
                lossR = 0.6;
                lLossR = 0.6;
                iniKE = 10000;
                lIniKE = 10000;
            }
             else if (Problem.equals("VRP.p20")) {
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
                maxNoImprovementUpperLevel = 1;
                lMaxNoImprovementUpperLevel = 1;
                numberOfGenerationSubPopulation = 5;
                lNumberOfGenerationSubPopulation = 5;
                p = 1;
                lP = 1;
                collR = 0.8;
                lCollR = 0.8;
                decThres = 10;
                lDecThres = 10;
                synThres = 10.0;
                lSynThres = 10.0;
                enBuff = 0.0;
                lEnBuff = 0.0;
                lossR = 0.6;
                lLossR = 0.6;
                iniKE = 10000;
                lIniKE = 10000;
            }
            else if (Problem.equals("VRP.p21")) {
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
                maxNoImprovementUpperLevel = 1;
                lMaxNoImprovementUpperLevel = 1;
                numberOfGenerationSubPopulation = 5;
                lNumberOfGenerationSubPopulation = 5;
                p = 1;
                lP = 1;
                collR = 0.8;
                lCollR = 0.8;
                decThres = 10;
                lDecThres = 10;
                synThres = 10.0;
                lSynThres = 10.0;
                enBuff = 0.0;
                lEnBuff = 0.0;
                lossR = 0.6;
                lLossR = 0.6;
                iniKE = 10000;
                lIniKE = 10000;
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

        public double getCollR() {
            return collR;
        }

        public void setCollR(double collR) {
            this.collR = collR;
        }

        public double getlCollR() {
            return lCollR;
        }

        public void setlCollR(double lCollR) {
            this.lCollR = lCollR;
        }

        public int getDecThres() {
            return decThres;
        }

        public void setDecThres(int decThres) {
            this.decThres = decThres;
        }

        public int getlDecThres() {
            return lDecThres;
        }

        public void setlDecThres(int lDecThres) {
            this.lDecThres = lDecThres;
        }

        public double getSynThres() {
            return synThres;
        }

        public void setSynThres(double synThres) {
            this.synThres = synThres;
        }

        public double getlSynThres() {
            return lSynThres;
        }

        public void setlSynThres(double lSynThres) {
            this.lSynThres = lSynThres;
        }

        public double getEnBuff() {
            return enBuff;
        }

        public void setEnBuff(double enBuff) {
            this.enBuff = enBuff;
        }

        public double getlEnBuff() {
            return lEnBuff;
        }

        public void setlEnBuff(double lEnBuff) {
            this.lEnBuff = lEnBuff;
        }

        public double getLossR() {
            return lossR;
        }

        public void setLossR(double lossR) {
            this.lossR = lossR;
        }

        public double getlLossR() {
            return lLossR;
        }

        public void setlLossR(double lLossR) {
            this.lLossR = lLossR;
        }

        public double getIniKE() {
            return iniKE;
        }

        public void setIniKE(double iniKE) {
            this.iniKE = iniKE;
        }

        public double getlIniKE() {
            return lIniKE;
        }

        public void setlIniKE(double lIniKE) {
            this.lIniKE = lIniKE;
        }

        private int getPlantProductionsAvailability() {
            return plantProductionsAvailability;
        }
    }
}
