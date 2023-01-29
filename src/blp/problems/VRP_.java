package blp.problems;

import blp.core.Problem;
import blp.core.Solution;
import blp.core.Variable;
import blp.encodings.solutionType.ArrayIntSolutionType;
import blp.encodings.solutionType.IntSolutionType;
import blp.encodings.solutionType.MatrixIntSolutionType;
import blp.encodings.variable.Int;
import blp.problems.parameters.Parameters;
import blp.util.BLPException;
import blp.util.PseudoRandom;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Abir
 */
public class VRP_ extends Problem {

    private String instance;
    private static String type;
    private static int numberOfVehicles;
    private static int numberOfCustomers;
    private static int numberOfDepots;
    private static int numberOfPlants;
    private static int totalNumberOfDemand;
    private static int[] maximumDurationOfRoutes;
    private static int[] maximumLoadOfVehicles;
    private static Customer[] detailsCustomers;
    private static double[][] productionsCost;
    private static double[][] deliveryCost;
    private double[][] customerDistance;
    private int[] productionsAvailability;
    private int[][] minProductionsCost;
    private int[] minProductions;

    //public static boolean display = false;
    /**
     * Creates a default MDVRP problem
     *
     * @param upperLevelSolutionType
     * @param lowerLevelSolutionType
     * @param instance
     */
    public VRP_(String instance, String upperLevelSolutionType, String lowerLevelSolutionType) {
        this.instance = instance;
        importInstanceSetting(instance);

//        customerDistance = new double[numberOfCustomers + numberOfDepots][numberOfCustomers + numberOfDepots];
//        for (int i = 0; i < numberOfCustomers + numberOfDepots; i++) {
//            for (int j = 0; j < numberOfCustomers + numberOfDepots; j++) {
//                if (i == j) {
//                    customerDistance[i][j] = 0;
//                } else {
//                    customerDistance[i][j] = PseudoRandom.randDouble(5, 525);
//                }
//            }
//        }
        
        for (Customer detailsCustomer : detailsCustomers) {
            detailsCustomer.setDemand(PseudoRandom.randInt(2, 15));
        }

        numberOfPlants = numberOfDepots;

        productionsCost = new double[numberOfDepots][numberOfPlants];
        //System.out.println("Production Cost");
        for (int i = 0; i < numberOfDepots; i++) {
            for (int j = 0; j < numberOfPlants; j++) {
                productionsCost[i][j] = PseudoRandom.randDouble(Parameters.lowerBoundProductionCost, Parameters.upperBoundProductionCost);
                //System.out.print(Parameters.productionsCost[i][j]+"\t");
            }
            //System.out.println();
        }
        minProductionsCost = new int[numberOfPlants][numberOfDepots];
        double[][] copyProductionsCost = new double[numberOfPlants][numberOfDepots];
        for (int i = 0; i < productionsCost.length; i++) {
            copyProductionsCost[i] = productionsCost[i].clone();
        }
        for (int i = 0; i < numberOfPlants; i++) {
            int c = 0;
            while (c < numberOfDepots) {
                int posmin = 0;
                for (int j = 1; j < numberOfDepots; j++) {
                    if (copyProductionsCost[posmin][i] > copyProductionsCost[j][i]) {
                        posmin = j;
                    }
                }
                minProductionsCost[i][c++] = posmin;
                copyProductionsCost[posmin][i] = Double.POSITIVE_INFINITY;
            }
        }

        minProductions = new int[numberOfPlants];
        double[] minProdPlants = new double[numberOfPlants];
        for (int i = 0; i < minProdPlants.length; i++) {
            minProdPlants[i] = productionsCost[minProductionsCost[i][0]][i];
        }
        int c = 0;
        while (c < numberOfPlants) {
            int posmin = 0;
            for (int j = 1; j < minProdPlants.length; j++) {
                if (minProdPlants[posmin] > minProdPlants[j]) {
                    posmin = j;
                }
            }
            minProductions[c++] = posmin;
            minProdPlants[posmin] = Double.POSITIVE_INFINITY;
        }
        deliveryCost = new double[numberOfDepots][numberOfPlants];
        for (int i = 0; i < numberOfDepots; i++) {
            for (int j = 0; j < numberOfPlants; j++) {
                deliveryCost[i][j] = PseudoRandom.randDouble(Parameters.lowerBoundDeleveryCost, Parameters.upperBoundDeleveryCost);
            }
        }
        productionsAvailability = new int[numberOfPlants];
        for (int i = 0; i < productionsAvailability.length; i++) {
            productionsAvailability[i] = Parameters.plantProductionsAvailability;
        }

        if (upperLevelSolutionType.equals("ArrayInt")) {
            solutionType_ = new MatrixIntSolutionType(this, numberOfDepots);
        } else if (upperLevelSolutionType.equals("Int")) {
            solutionType_ = new IntSolutionType(this);
        } else {
            try {
                throw new BLPException("Error: lower level solution type " + upperLevelSolutionType + " invalid");
            } catch (BLPException ex) {
                Logger.getLogger(VRP_.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (lowerLevelSolutionType.equals("ArrayInt")) {
            lowerlevelSolutionType_ = new ArrayIntSolutionType(this);
        } else if (lowerLevelSolutionType.equals("Int")) {
            lowerlevelSolutionType_ = new IntSolutionType(this);
        } else {
            try {
                throw new BLPException("Error: lower level solution type " + lowerLevelSolutionType + " invalid");
            } catch (BLPException ex) {
                Logger.getLogger(VRP_.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        numberOfVariablesU_ = numberOfCustomers + 2;
        numberOfVariablesL_ = numberOfDepots * numberOfPlants;

        lowerLimitU_ = new Integer[numberOfCustomers + 2];
        upperLimitU_ = new Integer[numberOfCustomers + 2];
        lowerLimitU_[0] = numberOfCustomers;
        upperLimitU_[0] = numberOfCustomers + numberOfDepots - 1;
        for (int i = 1; i < lowerLimitU_.length - 1; i++) {
            lowerLimitU_[i] = -1;
            upperLimitU_[i] = numberOfCustomers - 1;
        }
        lowerLimitU_[lowerLimitU_.length - 1] = numberOfCustomers;
        upperLimitU_[lowerLimitU_.length - 1] = numberOfCustomers + numberOfDepots - 1;

        lowerLimitL_ = new Integer[numberOfVariablesL_];
        upperLimitL_ = new Integer[numberOfVariablesL_];
        decisionVariablesDomainLowerLevel_ = new ArrayList<>();
        int x = 0;
        for (int i = 0; i < lowerLimitL_.length; i++) {
            lowerLimitL_[i] = 0;
            if (x == numberOfPlants - 1) {
                upperLimitL_[i] = productionsAvailability[x];
                x = 0;
            } else {
                upperLimitL_[i] = productionsAvailability[x];
                x++;
            }
            decisionVariablesDomainLowerLevel_.add(new Int((int) lowerLimitL_[i], (int) lowerLimitL_[i], (int) upperLimitL_[i]));
        }
    }

    @Override
    public void evaluate(Solution upperSolution, Solution lowerSolution) {
        /*if (upperSolution.isMarked()) {
         System.out.println("Solution before correction:");
         System.out.println(upperSolution.toString());
         }*/
        evaluateUpperLevelConstraints(upperSolution);
        /*if (upperSolution.isMarked()) {
         System.out.println("Solution after correction:");
         System.out.println(upperSolution.toString());
         }*/
        double distributionCost = 0.0;
        double productsCost = 0.0;
        try {
            for (int i = 0; i < upperSolution.getDecisionVariables().length; i++) {
                int j = 0;
                while (j < upperSolution.getDecisionVariable(i).getLength() - 1) {
                    Variable var1 = upperSolution.getDecisionVariable(i).getVariable(j);
                    Variable var2 = null;
                    int c = j + 1;
                    while (var2 == null && c < upperSolution.getDecisionVariable(i).getLength()) {
                        if (upperSolution.getDecisionVariable(i).compare(-1, c) != 0) {
                            var2 = upperSolution.getDecisionVariable(i).getVariable(c);
                        } else {
                            c++;
                        }
                    }
                    if (var2 != null && var1.compare(-1) != 0) {
                        distributionCost += distanceBetweenPoints(var1, var2);
                    }
                    j = c;
                }
            }
            int[][] matrixSolution = new int[numberOfDepots][numberOfPlants];
            int i = 0, j = 0;
            for (int c = 0; c < lowerSolution.getDecisionVariables().length; c++) {
                if (j < numberOfPlants) {
                    matrixSolution[i][j] = (Integer) lowerSolution.getDecisionVariable(c).getValue();
                    j++;
                } else {
                    j = 0;
                    i++;
                    matrixSolution[i][j] = (Integer) lowerSolution.getDecisionVariable(c).getValue();
                }
            }
            for (i = 0; i < numberOfDepots; i++) {
                for (j = 0; j < numberOfPlants; j++) {
                    if (matrixSolution[i][j] > 0) {
                        productsCost += matrixSolution[i][j] * deliveryCost[i][j];
                    }
                }
            }
            //System.out.println(distributionCost);
            //System.err.println(productsCost);
            upperSolution.setFitness(distributionCost + productsCost);
            //System.err.println("# "+distributionCost + productsCost);
        } catch (Exception ex) {
            Logger.getLogger(VRP_.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void evaluateLowerLevel(Solution upperSolution, Solution solution) {
        //System.out.println("Before evaluation ==> "+solution.toString());
        evaluateLowerLevelConstraints(solution);
        //System.out.println("After evaluation ==> "+solution.toString());
        double productsCost = 0.0;
        int[][] matrixSolution = new int[numberOfDepots][numberOfPlants];
        int i = 0, j = -1;
        try {
            for (int c = 0; c < solution.getDecisionVariables().length; c++) {
                if (j == numberOfPlants - 1) {
                    j = 0;
                    i++;
                } else {
                    j++;
                }
                matrixSolution[i][j] = (Integer) solution.getDecisionVariable(c).getValue();
            }
            for (i = 0; i < numberOfDepots; i++) {
                for (j = 0; j < numberOfPlants; j++) {
                    productsCost += matrixSolution[i][j] * productionsCost[i][j];
                }
            }
            /*if (productsCost == 0.0) {
             System.out.println("Products Cost detail:");
             System.out.println("Solution ==> ");
             for (i = 0; i < numberOfDepots; i++) {
             for (j = 0; j < numberOfPlants; j++) {
             System.out.print(matrixSolution[i][j] + "\t");
             }
             System.out.println();
             }
             System.out.println("Calcul ==> ");
             for (i = 0; i < numberOfDepots; i++) {
             for (j = 0; j < numberOfPlants; j++) {
             System.out.print((matrixSolution[i][j] * productionsCost[i][j]) + "\t");
             }
             System.out.println();
             }
             }*/
            solution.setFitness(productsCost);
        } catch (Exception ex) {
            Logger.getLogger(VRPTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void evaluateUpperLevelConstraints(Solution solution) {
        try {
            ArrayList<Variable> notExistingVariables = new ArrayList<>();
            ArrayList<ArrayList<VariablePositions>> duplicateVariables = new ArrayList<>();
            ArrayList<Variable> allVariables = solution.getDecisionVariable(0).getVariable(1).getAllVariablesInDomainExcludeLowerBounds();
            ArrayList<Variable> allVariables_ = solution.getDecisionVariable(0).getVariable(1).getAllVariablesInDomain();
            /*if (solution.isMarked()) {
             System.out.println("allVariables_ => (" + allVariables_.size() + ")");
             for (int i = 0; i < allVariables_.size(); i++) {
             System.out.print(allVariables_.get(i).getValue() + "\t");
             }
             System.out.println();
             }*/
            for (int i = 0; i < solution.getDecisionVariables().length; i++) {
                int indexFirst = 0;
                int indexEnd = solution.getDecisionVariable(i).getLength() - 1;
                Variable depotDeb = solution.getDecisionVariable(i).getVariable(indexFirst);
                Variable depotEnd = solution.getDecisionVariable(i).getVariable(indexEnd);
                if (depotDeb.compare(depotEnd.getValue()) != 0) {
                    if (PseudoRandom.randInt(0, 1) == 1) {
                        solution.getDecisionVariable(i).setValue(depotDeb.getValue(), indexEnd);
                    } else {
                        solution.getDecisionVariable(i).setValue(depotEnd.getValue(), indexFirst);
                    }
                }
                ArrayList<Variable> notExistingVariables_i = solution.getDecisionVariable(i).getNotExistingVariablesInDomain(allVariables);
                if (notExistingVariables.isEmpty()) {
                    notExistingVariables.addAll(notExistingVariables_i);
                } else {
                    notExistingVariables = intersection(notExistingVariables_i, notExistingVariables);
                }
                ArrayList<ArrayList<Integer>> duplicateVariables_i = solution.getDecisionVariable(i).getPostionsOf(allVariables_);
                /*if (solution.isMarked()) {
                 display = true;
                 duplicateVariables_i = solution.getDecisionVariable(i).getPostionsOf(allVariables_);
                 System.out.println("ligne " + i + " > ");
                 for (int k = 0; k < solution.getDecisionVariable(i).getLength(); k++) {
                 System.out.print(solution.getDecisionVariable(i).getValue(k) + "\t");
                 }
                 System.out.println();
                 for (int j = 0; j < duplicateVariables_i.size(); j++) {
                 System.out.print((j - 1)+" |==> ");
                 for (int k = 0; k < duplicateVariables_i.get(j).size(); k++) {
                 System.out.print(duplicateVariables.get(j).get(k) + "\t");
                 }
                 System.out.println();
                 }
                 } else {
                 display = false;
                 duplicateVariables_i = solution.getDecisionVariable(i).getPostionsOf(allVariables_);
                 }*/
                for (int j = 0; j < duplicateVariables_i.size(); j++) {
                    ArrayList<VariablePositions> dupVar = new ArrayList<>();
                    for (Integer ind : duplicateVariables_i.get(j)) {
                        dupVar.add(new VariablePositions(i, ind));
                    }
                    if (duplicateVariables.size() < allVariables_.size()) {
                        duplicateVariables.add(dupVar);
                    } else {
                        duplicateVariables.get(j).addAll(dupVar);
                    }
                }
            }
            for (int j = 1; j < duplicateVariables.size(); j++) {
                if (duplicateVariables.get(j).size() > 0) {
                    int index = PseudoRandom.randInt(0, duplicateVariables.get(j).size() - 1);
                    duplicateVariables.get(j).remove(index);
                }
            }
            /*if (solution.isMarked()) {
             System.out.println("Not Existing Variables => (" + notExistingVariables.size() + ")");
             for (int i = 0; i < notExistingVariables.size(); i++) {
             System.out.print(notExistingVariables.get(i).getValue() + "\t");
             }
             System.out.println();
             System.out.println("Duplicate Variables => ");
             for (int i = 0; i < duplicateVariables.size(); i++) {
             System.out.print((i - 1) + " ==> ");
             for (int j = 0; j < duplicateVariables.get(i).size(); j++) {
             System.out.print(duplicateVariables.get(i).get(j) + "\t");
             }
             System.out.println();
             }
             System.out.println();
             }*/
            while (!notExistingVariables.isEmpty() && !duplicateVariables.isEmpty()) {
                int ligne = PseudoRandom.randInt(0, duplicateVariables.size() - 1);
                while (duplicateVariables.get(ligne).isEmpty()) {
                    ligne = PseudoRandom.randInt(0, duplicateVariables.size() - 1);
                }
                int colone = PseudoRandom.randInt(0, duplicateVariables.get(ligne).size() - 1);
                solution.getDecisionVariable(duplicateVariables.get(ligne).get(colone).getLigne()).setValue(notExistingVariables.get(0).getValue(), duplicateVariables.get(ligne).get(colone).getColonne());
                notExistingVariables.remove(0);
                duplicateVariables.get(ligne).remove(colone);
            }
            for (int i = 1; i < duplicateVariables.size(); i++) {
                if (duplicateVariables.get(i).size() > 0) {
                    for (int j = 0; j < duplicateVariables.get(i).size(); j++) {
                        int ligne = duplicateVariables.get(i).get(j).getLigne();
                        int colone = duplicateVariables.get(i).get(j).getColonne();
                        solution.getDecisionVariable(ligne).setValue(-1, colone);
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(VRP_.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void evaluateLowerLevelConstraints(Solution solution) {
        int numberOfViolatedConstraints = 0, production = 0;
        int[][] matrixSolution = new int[numberOfDepots][numberOfPlants];
        int i = 0, j = -1;
        try {
            for (int c = 0; c < solution.getDecisionVariables().length; c++) {
                if (j == numberOfPlants - 1) {
                    j = 0;
                    i++;
                } else {
                    j++;
                }
                matrixSolution[i][j] = (Integer) solution.getDecisionVariable(c).getValue();
            }
        } catch (Exception ex) {
            Logger.getLogger(VRPTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        int[] plantsProductions = new int[numberOfPlants];
        //System.out.println("Solution ==> ");
        for (i = 0; i < numberOfDepots; i++) {
            plantsProductions[i] = 0;
            for (j = 0; j < numberOfPlants; j++) {
                //System.out.print(matrixSolution[i][j] + "\t");
                production += matrixSolution[i][j];
                plantsProductions[i] += matrixSolution[j][i];
            }
            //System.out.println();
        }
        /*System.out.println("Total production : " + production);
         System.out.println("Plants production : ");*/
        for (i = 0; i < numberOfPlants; i++) {
            //System.out.print(plantsProductions[i] + "\t");
            if (plantsProductions[i] > productionsAvailability[i]) {
                int toDelete = plantsProductions[i] - productionsAvailability[i];
                production -= toDelete;
                plantsProductions[i] = productionsAvailability[i];
                int p = numberOfDepots - 1;
                //System.out.println("Plants ("+(i+1)+") toDelete : "+toDelete);
                while (toDelete > 0 && p >= 0) {
                    int deleted = 0;
                    if (matrixSolution[minProductionsCost[i][p]][i] > 0) {
                        //System.out.println(matrixSolution[minProductionsCost[i][p]][i]+" > "+toDelete+" ==> "+(matrixSolution[minProductionsCost[i][p]][i] > toDelete));
                        if (matrixSolution[minProductionsCost[i][p]][i] > toDelete) {
                            deleted = matrixSolution[minProductionsCost[i][p]][i] - toDelete;
                            matrixSolution[minProductionsCost[i][p]][i] -= toDelete;
                            toDelete = 0;
                            //System.out.println("condition 1");
                        } else if (matrixSolution[minProductionsCost[i][p]][i] == toDelete) {
                            deleted = matrixSolution[minProductionsCost[i][p]][i];
                            matrixSolution[minProductionsCost[i][p]][i] = 0;
                            toDelete = 0;
                            //System.out.println("condition 2");
                        } else if (matrixSolution[minProductionsCost[i][p]][i] < toDelete) {
                            deleted = matrixSolution[minProductionsCost[i][p]][i];
                            toDelete = toDelete - matrixSolution[minProductionsCost[i][p]][i];
                            matrixSolution[minProductionsCost[i][p]][i] = 0;
                            /*System.out.println("condition 3");
                             System.out.print(toDelete+" - "+matrixSolution[minProductionsCost[i][p]][i]);
                             System.out.println(" = "+toDelete);*/
                        }
                    }
                    //System.out.println("     ===>  delete from depot ("+minProductionsCost[i][p]+") : "+deleted+" | NEW VAL IN DEPOT : "+ matrixSolution[minProductionsCost[i][p]][i]+" | REST to delete : "+toDelete);
                    p--;
                }
                numberOfViolatedConstraints++;
            }
        }
        //System.out.println();
        if (production < totalNumberOfDemand) {
            numberOfViolatedConstraints++;
            int missedProductions = totalNumberOfDemand - production;
            int p = 0;
            while (missedProductions > 0 && p < minProductions.length) {
                if (plantsProductions[minProductions[p]] < productionsAvailability[p]) {
                    int x = productionsAvailability[p] - plantsProductions[minProductions[p]];
                    if (x >= missedProductions) {
                        x = missedProductions;
                    }
                    missedProductions -= x;
                    matrixSolution[minProductionsCost[minProductions[p]][0]][minProductions[p]] += x;
                }
                p++;
            }
        }
        /*System.out.println("Solution corrigé ==> ");
         for (i = 0; i < numberOfDepots; i++) {
         for (j = 0; j < numberOfPlants; j++) {
         System.out.print(matrixSolution[i][j] + "\t");
         }
         System.out.println();
         }
         System.out.println("============================================");*/
        i = 0;
        j = -1;
        //System.out.println("Solution corrigé ==> ");
        //System.out.println("============================================================================================================================================================================================================================");
        try {
            for (int c = 0; c < solution.getDecisionVariables().length; c++) {
                if (j == numberOfPlants - 1) {
                    j = 0;
                    i++;
                } else {
                    j++;
                }
                //System.out.print(matrixSolution[i][j]+"\t");
                solution.getDecisionVariable(c).setValue(matrixSolution[i][j]);
            }
        } catch (Exception ex) {
            Logger.getLogger(VRPTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        //System.out.println();
        //System.out.println("============================================================================================================================================================================================================================");
        solution.setNumberOfViolatedConstraintUpperLevel(numberOfViolatedConstraints);
    }

    private static void importInstanceSetting(String instance) {
        String path = System.getProperty("user.dir") + System.getProperty("file.separator") + "src" + System.getProperty("file.separator") + "blp" + System.getProperty("file.separator") + "problems" + System.getProperty("file.separator") + "instances" + System.getProperty("file.separator") + "vrp" + System.getProperty("file.separator") + instance;
        try {
            InputStream ips = new FileInputStream(path);
            InputStreamReader ipsr = new InputStreamReader(ips);
            BufferedReader br = new BufferedReader(ipsr);
            String ligne;
            int ligneNumber = 1, indexLigne2 = 0, indexLigne3 = 0;
            totalNumberOfDemand = 0;
            while ((ligne = br.readLine()) != null) {
                if (ligneNumber == 1) {
                    switch (Integer.parseInt(ligne.substring(0, ligne.indexOf(" ")))) {
                        case 0:
                            type = "VRP";
                            break;
                        case 1:
                            type = "PVRP";
                            break;
                        case 2:
                            type = "MDVRP";
                            break;
                        case 3:
                            type = "CVRP";
                            break;
                        case 4:
                            type = "VRPTW";
                            break;
                        case 5:
                            type = "PVRPTW";
                            break;
                        case 6:
                            type = "MDVRPTW";
                            break;
                        case 7:
                            type = "SDVRPTW";
                            break;
                    }
                    ligne = ligne.substring(ligne.indexOf(" ") + 1, ligne.length());
                    numberOfVehicles = Integer.parseInt(ligne.substring(0, ligne.indexOf(" ")));
                    ligne = ligne.substring(ligne.indexOf(" ") + 1, ligne.length());
                    numberOfCustomers = Integer.parseInt(ligne.substring(0, ligne.indexOf(" ")));
                    ligne = ligne.substring(ligne.indexOf(" ") + 1, ligne.length());
                    ligne = ligne.trim();
                    numberOfDepots = Integer.parseInt(ligne);
                    maximumDurationOfRoutes = new int[numberOfDepots];
                    maximumLoadOfVehicles = new int[numberOfDepots];
                    detailsCustomers = new Customer[numberOfCustomers + numberOfDepots];
                } else if (ligneNumber >= 2 && ligneNumber < 2 + numberOfDepots) {
                    maximumDurationOfRoutes[indexLigne2] = Integer.parseInt(ligne.substring(0, ligne.indexOf(" ")));
                    ligne = ligne.substring(ligne.indexOf(" ") + 1, ligne.length());
                    ligne = ligne.trim();
                    maximumLoadOfVehicles[indexLigne2] = Integer.parseInt(ligne);
                    indexLigne2++;
                } else if (ligneNumber >= 2 + numberOfDepots && ligneNumber < 2 + numberOfDepots + numberOfCustomers) {
                    ligne = ligne.trim();
                    int id = Integer.parseInt(ligne.substring(0, ligne.indexOf(" ")));
                    ligne = ligne.substring(ligne.indexOf(" ") + 1, ligne.length());
                    ligne = ligne.trim();
                    double coordinateX = Double.parseDouble(ligne.substring(0, ligne.indexOf(" ")));
                    ligne = ligne.substring(ligne.indexOf(" ") + 1, ligne.length());
                    ligne = ligne.trim();
                    double coordinateY = Double.parseDouble(ligne.substring(0, ligne.indexOf(" ")));
                    ligne = ligne.substring(ligne.indexOf(" ") + 1, ligne.length());
                    ligne = ligne.trim();
                    int serviceDuration = Integer.parseInt(ligne.substring(0, ligne.indexOf(" ")));
                    ligne = ligne.substring(ligne.indexOf(" ") + 1, ligne.length());
                    ligne = ligne.trim();
                    int demand = Integer.parseInt(ligne.substring(0, ligne.indexOf(" ")));
                    totalNumberOfDemand += demand;
                    ligne = ligne.substring(ligne.indexOf(" ") + 1, ligne.length());
                    ligne = ligne.trim();
                    int frequencyOfVisit = Integer.parseInt(ligne.substring(0, ligne.indexOf(" ")));
                    ligne = ligne.substring(ligne.indexOf(" ") + 1, ligne.length());
                    ligne = ligne.trim();
                    int numberOfPossibleVisitCombinations = Integer.parseInt(ligne.substring(0, ligne.indexOf(" ")));
                    ligne = ligne.substring(ligne.indexOf(" ") + 1, ligne.length());
                    ligne = ligne.trim();
                    int[] listOfPossibleVisitCombinations = new int[numberOfPossibleVisitCombinations];
                    for (int c = 0; c < numberOfPossibleVisitCombinations; c++) {
                        if (ligne.contains(" ")) {
                            listOfPossibleVisitCombinations[c] = Integer.parseInt(ligne.substring(0, ligne.indexOf(" ")));
                            ligne = ligne.substring(ligne.indexOf(" ") + 1, ligne.length());
                            ligne = ligne.trim();
                        } else {
                            listOfPossibleVisitCombinations[c] = Integer.parseInt(ligne.substring(0, ligne.length()));
                            ligne = ligne.trim();
                        }
                        //listOfPossibleVisitCombinations[c] = Integer.parseInt(ligne.substring(0, ligne.indexOf(" ")));
                        //ligne = ligne.substring(ligne.indexOf(" ") + 1, ligne.length());
                        //ligne = ligne.trim();
                    }
                    int beginningOfTime = 0;//Integer.parseInt(ligne.substring(0, ligne.indexOf(" ")));
                    /*ligne = ligne.substring(ligne.indexOf(" ") + 1, ligne.length());
                     ligne = ligne.trim();*/
                    int endOfTime = 0;//Integer.parseInt(ligne);
                    detailsCustomers[indexLigne3] = new Customer(id, "customer", coordinateX, coordinateY, serviceDuration, demand, frequencyOfVisit, numberOfPossibleVisitCombinations, listOfPossibleVisitCombinations, beginningOfTime, endOfTime);
                    indexLigne3++;
                } else if (ligneNumber >= 2 + numberOfDepots + numberOfCustomers && ligneNumber < 2 + numberOfDepots + numberOfCustomers + numberOfDepots) {
                    ligne = ligne.trim();
                    int id = Integer.parseInt(ligne.substring(0, ligne.indexOf(" ")));
                    ligne = ligne.substring(ligne.indexOf(" ") + 1, ligne.length());
                    ligne = ligne.trim();
                    double coordinateX = Double.parseDouble(ligne.substring(0, ligne.indexOf(" ")));
                    ligne = ligne.substring(ligne.indexOf(" ") + 1, ligne.length());
                    ligne = ligne.trim();
                    double coordinateY = Double.parseDouble(ligne.substring(0, ligne.indexOf(" ")));
                    ligne = ligne.substring(ligne.indexOf(" ") + 1, ligne.length());
                    ligne = ligne.trim();
                    detailsCustomers[indexLigne3] = new Customer(id, "depot", coordinateX, coordinateY, 0, 0, 0, 0, null, 0, 0);
                    indexLigne3++;
                }
                ligneNumber++;
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println(e.toString());
        }
    }

    private double distanceBetweenPoints(Variable p1, Variable p2) throws Exception {
        int pos1 = (Integer) p1.getValue();
        int pos2 = (Integer) p2.getValue();
        //return customerDistance[pos1][pos2];
        Customer c1 = detailsCustomers[pos1];
        Customer c2 = detailsCustomers[pos2];
        /*System.out.println(c1.toString());
         System.out.println(c1.toString());*/
        return Math.round(Math.sqrt(Math.pow((c2.getCoordinateX() - c1.getCoordinateX()), 2) + Math.pow((c2.getCoordinateY() - c1.getCoordinateY()), 2)));
    }

    public static int getNumberOfCustomers() {
        return numberOfCustomers;
    }

    public static int getNumberOfDepots() {
        return numberOfDepots;
    }

    public static int getNumberOfPlants() {
        return numberOfPlants;
    }

    private class VariablePositions {

        private int ligne;
        private int colonne;

        public VariablePositions(int ligne, int colonne) {
            this.ligne = ligne;
            this.colonne = colonne;
        }

        public int getLigne() {
            return ligne;
        }

        public void setLigne(int ligne) {
            this.ligne = ligne;
        }

        public int getColonne() {
            return colonne;
        }

        public void setColonne(int colonne) {
            this.colonne = colonne;
        }

        @Override
        public String toString() {
            return "(" + ligne + "," + colonne + ")";
        }
    }

    private static class Customer {

        private int id;
        private String type;
        private double coordinateX;
        private double coordinateY;
        private int serviceDuration;
        private int demand;
        private int frequencyOfVisit;
        private int numberOfPossibleVisitCombinations;
        private int[] listOfPossibleVisitCombinations;
        private int beginningOfTime;
        private int endOfTime;

        public Customer(int id, String type, double coordinateX, double coordinateY, int serviceDuration, int demand, int frequencyOfVisit, int numberOfPossibleVisitCombinations, int[] listOfPossibleVisitCombinations, int beginningOfTime, int endOfTime) {
            this.id = id;
            this.type = type;
            this.coordinateX = coordinateX;
            this.coordinateY = coordinateY;
            this.serviceDuration = serviceDuration;
            this.demand = demand;
            this.frequencyOfVisit = frequencyOfVisit;
            this.numberOfPossibleVisitCombinations = numberOfPossibleVisitCombinations;
            this.listOfPossibleVisitCombinations = listOfPossibleVisitCombinations;
            this.beginningOfTime = beginningOfTime;
            this.endOfTime = endOfTime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public double getCoordinateX() {
            return coordinateX;
        }

        public void setCoordinateX(double coordinateX) {
            this.coordinateX = coordinateX;
        }

        public double getCoordinateY() {
            return coordinateY;
        }

        public void setCoordinateY(double coordinateY) {
            this.coordinateY = coordinateY;
        }

        public int getServiceDuration() {
            return serviceDuration;
        }

        public void setServiceDuration(int serviceDuration) {
            this.serviceDuration = serviceDuration;
        }

        public int getDemand() {
            return demand;
        }

        public void setDemand(int demand) {
            this.demand = demand;
        }

        public int getFrequencyOfVisit() {
            return frequencyOfVisit;
        }

        public void setFrequencyOfVisit(int frequencyOfVisit) {
            this.frequencyOfVisit = frequencyOfVisit;
        }

        public int getNumberOfPossibleVisitCombinations() {
            return numberOfPossibleVisitCombinations;
        }

        public void setNumberOfPossibleVisitCombinations(int numberOfPossibleVisitCombinations) {
            this.numberOfPossibleVisitCombinations = numberOfPossibleVisitCombinations;
        }

        public int[] getListOfPossibleVisitCombinations() {
            return listOfPossibleVisitCombinations;
        }

        public void setListOfPossibleVisitCombinations(int[] listOfPossibleVisitCombinations) {
            this.listOfPossibleVisitCombinations = listOfPossibleVisitCombinations;
        }

        public int getBeginningOfTime() {
            return beginningOfTime;
        }

        public void setBeginningOfTime(int beginningOfTime) {
            this.beginningOfTime = beginningOfTime;
        }

        public int getEndOfTime() {
            return endOfTime;
        }

        public void setEndOfTime(int endOfTime) {
            this.endOfTime = endOfTime;
        }

        @Override
        public String toString() {
            return "Customer{" + "id=" + id + ", coordinateX=" + coordinateX + ", coordinateY=" + coordinateY + ", serviceDuration=" + serviceDuration + ", demand=" + demand + ", frequencyOfVisit=" + frequencyOfVisit + ", numberOfPossibleVisitCombinations=" + numberOfPossibleVisitCombinations + ", listOfPossibleVisitCombinations=" + listOfPossibleVisitCombinations + ", beginningOfTime=" + beginningOfTime + ", endOfTime=" + endOfTime + '}';
        }
    }

    public static void main(String[] args) {

    }

    public <T> List<T> union(List<T> list1, List<T> list2) {
        Set<T> set = new HashSet<T>();

        set.addAll(list1);
        set.addAll(list2);

        return new ArrayList<T>(set);
    }

    public ArrayList<Variable> intersection(ArrayList<Variable> list1, ArrayList<Variable> list2) throws Exception {
        ArrayList<Variable> list = new ArrayList<>();

        for (Variable t : list1) {
            boolean found = false;
            int i = 0;
            while (!found && i < list2.size()) {
                if (list2.get(i).compare(t.getValue()) == 0) {
                    found = true;
                }
                i++;
            }
            if (found) {
                list.add(t);
            }
        }

        return list;
    }
}
