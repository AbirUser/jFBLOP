/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blp.problems;

import blp.core.Problem;
import blp.core.Solution;
import blp.encodings.solutionType.ArrayIntSolutionType;
import blp.encodings.solutionType.IntSolutionType;
import blp.encodings.variable.Int;
import blp.problems.parameters.Parameters;
import blp.util.BLPException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Abir
 */
public class CVRP extends Problem {
    
    private String instance;
    private int numberOfCustomers;
    private int numberOfPlants;
    private static int numberOfVehicles;
    private static int totalNumberOfDemand;
    private static int[] maximumDurationOfRoutes;
    private static int[] maximumLoadOfVehicles;
    private static Customer[] detailsCustomers;
    private static double[][] productionsCost;
    private static double[][] deliveryCost;
    private int[] productionsAvailability;
    private int[][] minProductionsCost;
    private int[] minProductions;
    
    public CVRP(String instance, String upperLevelSolutionType, String lowerLevelSolutionType) {
        this.instance = instance;
        importInstanceSetting(instance);
        numberOfPlants = Parameters.numberOfPlants;
        productionsCost = Parameters.productionsCost;
        minProductionsCost = Parameters.minProductionsCost;
        minProductions = Parameters.minProductions;
        deliveryCost = Parameters.deliveryCost;
        productionsAvailability = Parameters.productionsAvailability;
        if (upperLevelSolutionType.equals("Int")) {
            solutionType_ = new IntSolutionType(this);
        } else {
            try {
                throw new BLPException("Error: upper level solution type " + upperLevelSolutionType + " invalid");
            } catch (BLPException ex) {
                Logger.getLogger(VRPTest.class.getName()).log(Level.SEVERE, null, ex);
            }
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

        numberOfVariablesU_ = numberOfCustomers * 2 + 1;
        numberOfVariablesL_ = numberOfPlants;

        lowerLimitU_ = new Integer[numberOfCustomers * 2 + 1];
        upperLimitU_ = new Integer[numberOfCustomers * 2 + 1];
        lowerLimitU_[0] = 0;
        upperLimitU_[0] = 0;
        for (int i = 1; i < lowerLimitU_.length - 1; i++) {
            lowerLimitU_[i] = -1;
            upperLimitU_[i] = numberOfCustomers;
        }
        lowerLimitU_[lowerLimitU_.length - 1] = 0;
        upperLimitU_[lowerLimitU_.length - 1] = 0;
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
    
    private static void importInstanceSetting(String instance) {
    
    }

    @Override
    public void evaluate(Solution upperSolution, Solution lowerSolution) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void evaluateLowerLevel(Solution upperSolution, Solution lowerSolution) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void evaluateUpperLevelConstraints(Solution solution) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void evaluateLowerLevelConstraints(Solution solution) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    private class Customer {
        
        private int id;
        private String type;
        private double coordinateX;
        private double coordinateY;

        public Customer(int id, String type, double coordinateX, double coordinateY) {
            this.id = id;
            this.type = type;
            this.coordinateX = coordinateX;
            this.coordinateY = coordinateY;
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
        
        
    }
}
