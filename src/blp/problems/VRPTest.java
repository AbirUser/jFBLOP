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
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Abir
 */
public class VRPTest extends Problem {

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
    private int[] productionsAvailability;
    private int[][] minProductionsCost;
    private int[] minProductions;
    
    private boolean test = true;

    /**
     * Creates a default MDVRP problem
     *
     * @param upperLevelSolutionType
     * @param lowerLevelSolutionType
     * @param instance
     */
    public VRPTest(String instance, String upperLevelSolutionType, String lowerLevelSolutionType) {
        this.instance = instance;
        importInstanceSetting(instance);
        numberOfPlants = Parameters.numberOfPlants;
        productionsCost = Parameters.productionsCost;
        minProductionsCost = Parameters.minProductionsCost;
        minProductions = Parameters.minProductions;
        deliveryCost = Parameters.deliveryCost;
        productionsAvailability = Parameters.productionsAvailability;
        /*if (upperLevelSolutionType.equals("Int")) {
            solutionType_ = new IntSolutionType(this);
        } else {
            try {
                throw new BLPException("Error: upper level solution type " + upperLevelSolutionType + " invalid");
            } catch (BLPException ex) {
                Logger.getLogger(VRP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }*/
        
        switch (upperLevelSolutionType) {
            case "ArrayInt":
                solutionType_ = new MatrixIntSolutionType(this, numberOfDepots);
                break;
            case "Int":
                solutionType_ = new IntSolutionType(this);
                break;
            default:
                try {
                    throw new BLPException("Error: lower level solution type " + upperLevelSolutionType + " invalid");
                } catch (BLPException ex) {
                    Logger.getLogger(VRPTest.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
        }

        switch (lowerLevelSolutionType) {
            case "ArrayInt":
                lowerlevelSolutionType_ = new ArrayIntSolutionType(this);
                break;
            case "Int":
                lowerlevelSolutionType_ = new IntSolutionType(this);
                break;
            default:
                try {
                    throw new BLPException("Error: lower level solution type " + lowerLevelSolutionType + " invalid");
                } catch (BLPException ex) {
                    Logger.getLogger(VRPTest.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
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
        /*for (Object lowerLimitU_1 : lowerLimitU_) {
         System.out.print(lowerLimitU_1 + "\t");
         }
         System.out.println();
         for (Object upperLimitU_1 : upperLimitU_) {
         System.out.print(upperLimitU_1 + "\t");
         }
         System.out.println();*/
        //System.exit(0);
        lowerLimitL_ = new Integer[numberOfVariablesL_];
        upperLimitL_ = new Integer[numberOfVariablesL_];
        decisionVariablesDomainLowerLevel_ = new ArrayList<>();
        int j = 0;
        for (int i = 0; i < lowerLimitL_.length; i++) {
            lowerLimitL_[i] = 0;
            if (j == numberOfPlants - 1) {
                upperLimitL_[i] = productionsAvailability[j];
                j = 0;
            } else {
                upperLimitL_[i] = productionsAvailability[j];
                j++;
            }
            decisionVariablesDomainLowerLevel_.add(new Int((int) lowerLimitL_[i], (int) lowerLimitL_[i], (int) upperLimitL_[i]));
        }
        //this(solutionType, );
    }

    @Override
    public void evaluate(Solution upperSolution, Solution lowerSolution) {
        evaluateUpperLevelConstraints(upperSolution);
        double distributionCost = 0.0;
        double productsCost = 0.0;
        for (int i = 0; i < upperSolution.getDecisionVariables().length - 1; i++) {
            try {
                distributionCost += distanceBetweenPoints(upperSolution.getDecisionVariable(i), upperSolution.getDecisionVariable(i + 1));
            } catch (Exception ex) {
                Logger.getLogger(VRPTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        int[][] matrixSolution = new int[numberOfDepots][numberOfPlants];
        int i = 0, j = 0;
        try {
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
                    productsCost += matrixSolution[i][j] * productionsCost[i][j];
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(VRPTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        upperSolution.setFitness(distributionCost + productsCost);
        System.exit(0);
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
                    //System.out.print(matrixSolution[i][j] + "\t");
                }
                //System.out.println();
            }
            if (productsCost == 0.0) {
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
            }
        } catch (Exception ex) {
            Logger.getLogger(VRPTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        solution.setFitness(productsCost);
    }

    @Override
    public void evaluateUpperLevelConstraints(Solution solution) {
        System.out.println("Correction ==> ");
        int numberOfViolatedConstraints = 0, i, j, c;
        ArrayList<Variable> solutionList = new ArrayList<>(Arrays.asList(solution.getDecisionVariables()));
        ArrayList<Variable> notExisting = new ArrayList();
        ArrayList<ArrayList<Integer>> indexExisting = new ArrayList<>();
        ArrayList<Integer> indexExistingVector = new ArrayList<>();
        for (i = 0; i < numberOfCustomers; i++) {
            try {
                ArrayList<Integer> index = new ArrayList<>();
                boolean found = false;
                j = 1;
                Variable var;
                if (i < solution.getDecisionVariables().length) {
                    var = solution.getDecisionVariable(i).copy();
                } else {
                    var = solution.getDecisionVariable(solution.getDecisionVariables().length - 1).copy();
                }
                var.setValue(i);
                for (j = 0; j < solution.getDecisionVariables().length; j++) {
                    try {
                        if (solution.getDecisionVariable(j).compare(numberOfCustomers) == -1 && solution.getDecisionVariable(j).compare(i) == 0) {
                            if (found) {
                                index.add(j);
                                indexExistingVector.add(j);
                            }
                            found = true;
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(VRPTest.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (index.isEmpty() && !found) {
                    index.add(-1);
                }
                indexExisting.add(index);
                if (!found) {
                    notExisting.add(var);
                }
            } catch (Exception ex) {
                Logger.getLogger(VRPTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        for (i = 0; i < indexExisting.size(); i++) {
            System.out.print(i + " => ");
            for (j = 0; j < indexExisting.get(i).size(); j++) {
                System.out.print(indexExisting.get(i).get(j) + "\t");
            }
            System.out.println();
        }
        System.out.println();
        for (i = 0; i < indexExistingVector.size(); i++) {
            System.out.print(indexExistingVector.get(i) + "\t");
        }
        System.out.println();
        String solutionBefore = solution.toString();
        System.out.println("SOLUTION ==> " + solutionBefore);
        System.out.print("INDEX    ==> ");
        for (i = 0; i < solution.getDecisionVariables().length; i++) {
            System.out.print(i + "\t");
        }
        System.out.println();
        System.out.print("NOT EXISTING CUSTOMER ==> ");
        for (i = 0; i < notExisting.size(); i++) {
            System.out.print(notExisting.get(i) + "\t");
        }
        System.out.println();
        i = 0;
        System.out.println("DELETING REPETATION AND ADDING NOT EXISTING CUSTOMER...");
        //while (!notExisting.isEmpty()) {
        for (j = 0; j < indexExisting.size(); j++) {
            if (notExisting.isEmpty()) {
                System.out.println("empty");
                break;
            }
            System.out.print("CUSTOMER (" + j + ") ==> ");
            if (!indexExisting.get(j).isEmpty()) {
                if (indexExisting.get(j).get(0) != -1) {
                    if (!notExisting.isEmpty()) {
                        try {
                            System.out.println("REPLACE " + solution.getDecisionVariable(indexExisting.get(j).get(0)).getValue() + " BY " + notExisting.get(0).getValue() + " IN INDEX : " + indexExisting.get(j).get(0));
                            indexExistingVector.remove(indexExisting.get(j).get(0));
                            solution.getDecisionVariable(indexExisting.get(j).get(0)).setValue(notExisting.get(0).getValue());
                            System.out.println("SOLUTION :|> "+solution.toString());
                            System.out.print("INDEX    ==> ");
                            for (i = 0; i < solution.getDecisionVariables().length; i++) {
                                System.out.print(i + "\t");
                            }
                            System.out.println();
                            if (indexExisting.get((int) notExisting.get(0).getValue()).size() == 1) {
                                if (indexExisting.get((int) notExisting.get(0).getValue()).get(0) == -1) {
                                    indexExisting.get((int) notExisting.get(0).getValue()).clear();
                                }
                            }
                        } catch (Exception ex) {
                            Logger.getLogger(VRPTest.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        indexExisting.get(j).remove(0);
                        notExisting.remove(0);
                        if (notExisting.isEmpty()) {
                            break;
                        }
                        System.out.print("NOT EXISTING CUSTOMER ==> ");
                        for (i = 0; i < notExisting.size(); i++) {
                            System.out.print(notExisting.get(i) + "\t");
                        }
                        System.out.println();
                        System.out.print("EXISTING INDEX ==> ");
                        for (i = 0; i < indexExistingVector.size(); i++) {
                            System.out.print(indexExistingVector.get(i) + "\t");
                        }
                        System.out.println();
                    }
                } else {
                    System.out.print("NOT EXISTING !!");
                    if (!indexExistingVector.isEmpty()) {
                        int indCustomer = findPosVariableInList(notExisting, j);
                        System.out.println();
                        System.out.println(notExisting + " ==> " + indCustomer);
                        try {
                            System.out.println("REPLACE " + solution.getDecisionVariable(indexExistingVector.get(0)).getValue() + " BY " + j + " IN INDEX : " + indexExistingVector.get(0));
                            solution.setDecisionVariables(indexExistingVector.get(0), notExisting.get(indCustomer));
                            System.out.println("SOLUTION :|> "+solution.toString());
                        } catch (Exception ex) {
                            Logger.getLogger(VRPTest.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        indexExistingVector.remove(0);
                        indexExisting.get(j).remove(0);
                        notExisting.remove(indCustomer);
                        if (notExisting.isEmpty()) {
                            break;
                        }
                        System.out.print("NOT EXISTING CUSTOMER ==> ");
                        for (i = 0; i < notExisting.size(); i++) {
                            System.out.print(notExisting.get(i) + "\t");
                        }
                        System.out.println();
                        System.out.print("EXISTING INDEX ==> ");
                        for (i = 0; i < indexExistingVector.size(); i++) {
                            System.out.print(indexExistingVector.get(i) + "\t");
                        }
                        System.out.println();
                    } else {
                        System.out.println();
                    }
                }
            } else {
                System.out.println("EXISTING WHITHOUT PROBLEM ");
            }
        }

        //}
        //System.out.println("Begin evaluateUpperLevelConstraints : generate matrixSolution....");
        /*ArrayList<ArrayList<Variable>> matrixSolution = new ArrayList();
        ArrayList<Variable> route = new ArrayList();
        i = 0;
        j = 0;
        c = 1;
        while (i < solution.getDecisionVariables().length) {
            System.out.println("length of decision variable"+ solution.getDecisionVariables().length);
            try {
                while (solutionList.lastIndexOf(solution.getDecisionVariable(i)) != i) {
                    solutionList.lastIndexOf(i);
                }
                if (solution.getDecisionVariable(i).compare(numberOfCustomers) == 1) {
                    if (c < 2) {
                        route.add(solution.getDecisionVariable(i));
                        c++;
                    } else if (c == 2) {
                        c = 1;
                        route.add(solution.getDecisionVariable(i));
                        if (route.size() > 2) {
                            matrixSolution.add(route);
                        }
                        route = new ArrayList();
                        route.add(solution.getDecisionVariable(i));
                        c++;
                    }
                } else {
                    route.add(solution.getDecisionVariable(i));
                }
                i++;
            } catch (Exception ex) {
                Logger.getLogger(VRP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        for (i = 0; i < matrixSolution.size(); i++) {
            try {
                if (matrixSolution.get(i).get(0).compare(matrixSolution.get(i).get(matrixSolution.get(i).size() - 1).getValue()) != 0) {
                    matrixSolution.get(i).get(matrixSolution.get(i).size() - 1).setValue(matrixSolution.get(i).get(0).getValue());
                }
            } catch (Exception ex) {
                Logger.getLogger(VRP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        solution.setNumberOfViolatedConstraintLowerLevel(numberOfViolatedConstraints);*/
        
        System.out.println("Solution Before change : ");
        System.out.println(solutionBefore);
        System.out.println("Solution After change : ");
        System.out.println(solution.toString());
        if(test) {
            test = false;
            evaluateUpperLevelConstraints(solution);
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
    
    private int findPosVariableInList(ArrayList<Variable> list, int var) {
        int pos = -1;
        int i = 0;
        boolean found = false;
        while (!found && i < list.size()) {
            try {
                if((int) list.get(i).getValue() == var) {
                    pos = i;
                    found = true;
                } else {
                    i++;
                }
            } catch (Exception ex) {
                Logger.getLogger(VRPTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return pos;
    }

    private boolean findInSolution(Solution solution, Variable var) {
        boolean found = false;
        int i = 0;
        while (!found && i < solution.getDecisionVariables().length) {
            try {
                if (solution.getDecisionVariable(i).getValue().equals(var.getValue())) {
                    found = true;
                } else {
                    i++;
                }
            } catch (Exception ex) {
                Logger.getLogger(VRPTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return found;
    }

    private double distanceBetweenPoints(Variable p1, Variable p2) throws Exception {
        int pos1 = (Integer) p1.getValue();
        int pos2 = (Integer) p2.getValue();
        Customer c1 = detailsCustomers[pos1];
        Customer c2 = detailsCustomers[pos2];
        return Math.round(Math.sqrt(Math.pow((c2.getCoordinateX() - c1.getCoordinateX()), 2) + Math.pow((c2.getCoordinateY() - c1.getCoordinateY()), 2)));
    }

    private static void importInstanceSetting(String instance) {
        String path = System.getProperty("user.dir") + System.getProperty("file.separator") +"src" + System.getProperty("file.separator") + "blp" + System.getProperty("file.separator") + "problems" + System.getProperty("file.separator") + "instances" + System.getProperty("file.separator") + "vrp" + System.getProperty("file.separator") + instance;
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

    public static int getNumberOfCustomers() {
        return numberOfCustomers;
    }

    public static void main(String[] args) {
        VRPTest.importInstanceSetting("pr01");
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
}
