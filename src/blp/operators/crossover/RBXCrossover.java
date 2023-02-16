/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blp.operators.crossover;

import blp.core.Operator;
import blp.core.Solution;
import blp.core.Variable;
import blp.encodings.variable.Int;
import blp.util.BLPException;
import blp.util.PseudoRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Abir
 */
public class RBXCrossover extends Operator {

    private static int numberCustomer;
    private String level;

    /*public RBXCrossover(HashMap<String, Object> parameters) {
        super(parameters);
        if (super.getParametre("customerNumber") == null) {
            try {
                throw new BLPException("Customer number `customerNumber` is not defined");
            } catch (BLPException ex) {
                Logger.getLogger(RBXCrossover.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        numberCustomer = (Integer) getParametre("customerNumber");
    }*/

    private static ArrayList<ArrayList<Variable>> toRoutes(Solution s) {
        ArrayList<ArrayList<Variable>> routes = new ArrayList();
        ArrayList<Variable> route = new ArrayList();
        int j = 0, c = 1, i = 0;
        while (i < s.getDecisionVariables().length) {
            //System.out.println("length of decision variable"+ s.getDecisionVariables().length);
            try {
                if ((Integer) s.getDecisionVariable(i).getValue() > numberCustomer) {
                    if (c < 2) {
                        route.add(s.getDecisionVariable(i));
                        c++;
                    } else if (c == 2) {
                        c = 1;
                        route.add(s.getDecisionVariable(i));
                        if (route.size() > 2) {
                            routes.add(route);
                        }
                        route = new ArrayList();
                        route.add(s.getDecisionVariable(i));
                        c++;
                    }
                } else {
                    route.add(s.getDecisionVariable(i));
                }
                i++;
            } catch (Exception ex) {
                Logger.getLogger(RBXCrossover.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return routes;
    }

    private static Variable[] routesToSolution(Solution solution, ArrayList<ArrayList<Variable>> routesSolution) {
        Variable[] var = new Variable[solution.getDecisionVariables().length];
        int j = 1;
        try {
            var[0] = solution.getDecisionVariable(0);
            var[0].setValue(routesSolution.get(0).get(0));
            for (ArrayList<Variable> route : routesSolution) {
                for (int i = 1; i < route.size(); i++) {
                    var[j] = solution.getDecisionVariable(j);
                    var[j++].setValue(route.get(i).getValue());
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(RBXCrossover.class.getName()).log(Level.SEVERE, null, ex);
        }
        return var;
    }

////    private Solution correction(ArrayList<ArrayList<Integer>> routesSolution, int newRouteIndex) {
////        for (ArrayList<Integer> route : routesSolution) {
////            for (int var : route) {
////                System.out.print(var + "\t");
////            }
////            System.out.println();
////        }
////        System.out.println();
////        for (int i = 0; i < routesSolution.size(); i++) {
////            if (i != newRouteIndex) {
////                for (int j = 0; j < routesSolution.get(i).size(); j++) {
////                    if (routesSolution.get(i).get(j) > 0) {
////                        int c = 0;
////                        boolean exist = false;
////                        while (c < routesSolution.get(newRouteIndex).size() && !exist) {
////                            if (Objects.equals(routesSolution.get(i).get(j), routesSolution.get(newRouteIndex).get(c))) {
////                                exist = true;
////                            } else {
////                                c++;
////                            }
////                        }
////                        if (exist) {
////                            routesSolution.get(i).set(j, 0);
////                        }
////                    }
////                }
////            }
////        }
//        for (ArrayList<Integer> route : routesSolution) {
//            for (int var : route) {
//                System.out.print(var + "\t");
//            }
//            System.out.println();
//        }
//        System.out.println();
//        return routesSolution;
//    }
    
    public RBXCrossover(HashMap<String, Object> parameters) {
        super(parameters);
        if (super.getParametre("level") == null) {
            try {
                throw new BLPException("Level `level` is not defined");
            } catch (BLPException ex) {
                Logger.getLogger(RBXCrossover.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        level = (String) getParametre("level");
    }
    
    @Override
    public Object[] execute(Object[] solutions) {
       Object[] result = new Object[1];
       Solution solution1 = (Solution) solutions[0];
       Solution solution2 = (Solution) solutions[1];
       try {
            if (level.equals("upper")) {
                int randomlyRoutSolution1 = PseudoRandom.randInt(0, solution1.getDecisionVariables().length - 1);
                int randomlyRoutSolution2 = PseudoRandom.randInt(0, solution2.getDecisionVariables().length - 1);
                for(int i=0;i < solution2.getDecisionVariable(randomlyRoutSolution2).getLength(); i++) {
                    solution2.getDecisionVariable(randomlyRoutSolution2).setValue(solution1.getDecisionVariable(randomlyRoutSolution1).getValue(i), i);
                }
                result[0] = solution2;
            } else {
                ArrayList<ArrayList<Variable>> routeS1 = toRoutes(solution1);
                ArrayList<ArrayList<Variable>> routeS2 = toRoutes(solution2);
                int randomlyRouteSolution1 = PseudoRandom.randInt(0, routeS1.size() - 1);
                int randomlyRouteSolution2 = PseudoRandom.randInt(0, routeS2.size() - 1);
                ArrayList<Variable> selectedVariable = routeS1.get(randomlyRouteSolution1);
                routeS2.set(randomlyRouteSolution2, selectedVariable);
                solution2.setDecisionVariables(routesToSolution(solution2, routeS2));
            }
        } catch (Exception e) {
            Logger.getLogger(RBXCrossover.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
       return result;
    }
    
    /*@Override
    public Object[] execute(Object[] solutions) {
        Object[] result = new Object[1];
        Solution solution1 = (Solution) solutions[0];
        Solution solution2 = (Solution) solutions[1];
        ArrayList<ArrayList<Variable>> routeS1 = toRoutes(solution1);
        ArrayList<ArrayList<Variable>> routeS2 = toRoutes(solution2);
        int randomlyRouteSolution1 = PseudoRandom.randInt(0, routeS1.size() - 1);
        int randomlyRouteSolution2 = PseudoRandom.randInt(0, routeS2.size() - 1);
        ArrayList<Variable> notExisting = new ArrayList<>();
        if(!routeS2.isEmpty() && !routeS1.isEmpty()) {
            for (int i = 1; i < routeS2.get(randomlyRouteSolution2).size() - 1; i++) {
                Variable var = routeS2.get(randomlyRouteSolution2).get(i);
                boolean found = false;
                int j = 1;
                while (!found && j < routeS1.get(randomlyRouteSolution1).size() - 1) {
                    try {
                        if (routeS1.get(randomlyRouteSolution1).get(j).compare(var.getValue()) == 0) {
                            found = true;
                        } else {
                            j++;
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(RBXCrossover.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (!found) {
                    notExisting.add(var);
                }
            }
            ArrayList<Variable> selectedVariable = routeS1.get(randomlyRouteSolution1);
            for (int i = 1; i < selectedVariable.size() - 1; i++) {
                for (int j = 0; j < routeS2.size(); j++) {
                    if (j != randomlyRouteSolution2) {
                        ArrayList<Integer> existPositions = new ArrayList<>();
                        try {
                            existPositions = indexInRoutes(routeS2.get(j), selectedVariable.get(i));
                        } catch (Exception ex) {
                            Logger.getLogger(RBXCrossover.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        if (existPositions.size() > 0 && notExisting.size() > 0) {
                            for (int k = 0; k < existPositions.size(); k++) {
                                int index = PseudoRandom.randInt(0, notExisting.size() - 1);
                                if (!notExisting.isEmpty()) {
                                    Variable var = notExisting.get(index);
                                    routeS2.get(j).set(existPositions.get(k), var);
                                    notExisting.remove(index);
                                }
                            }
                        }
                    }
                }
            }
            routeS2.set(randomlyRouteSolution2, selectedVariable);
            while (notExisting.size() > 0) {
                int index1 = PseudoRandom.randInt(0, routeS2.size() - 1);
                int index2 = PseudoRandom.randInt(1, routeS2.get(index1).size() - 2);
                routeS2.get(index1).add(index2, notExisting.get(0));
                notExisting.remove(0);
            }
            ArrayList<Variable> vars = new ArrayList<>();
            for (int i = 0; i < routeS2.size(); i++) {
                for (int j = 0; j < routeS2.get(i).size(); j++) {
                    Variable var = routeS2.get(i).get(j);
                    vars.add(var);

                }
            }
            solution2.setDecisionVariables(vars);
        }
        result[0] = solution2;
        return result;
    }*/

//     public static void main(String[] args) {
//        int[] decisionVars1 = {-3, 5, 3, 9, -1, 4, 1, 7, -2, 2, 6, 8, -2};
//        int[] decisionVars2 = {-3, 2, 5, 7, -1, 8, 1, 3, -2, 6, 4, 9, -2};
//        Object[] solutions = new Object[2];
//        solutions[0] = (Object) new Solution(decisionVars1);
//        solutions[1] = (Object) new Solution(decisionVars2);
//        Solution result = (Solution) new RBXCrossover(new HashMap<String, Object>()).execute(solutions)[0];
//        System.out.println("Parent 1: " + solutions[0].toString());
//        System.out.println("Parent 2: " + solutions[1].toString());
//        System.out.println("Child   : " + result.toString());
//    }
    private static ArrayList<Integer> indexInRoutes(ArrayList<Variable> routes, Variable var) throws Exception {
        ArrayList<Integer> index = new ArrayList<>();
        for (int i = 0; i < routes.size(); i++) {
            if (var.getValue().equals(routes.get(i).getValue())) {
                index.add(i);
            }
        }
        return index;
    }

    public static void main(String[] args) {
        int[] vars1 = new int[]{8, 2, 5, 1, 8, 9, 7, 4, 9, 10, 0, 10};
        int[] vars2 = new int[]{9, 4, 1, 9, 10, 3, 5, 7, 10, 8, 2, 6, 8};
        int numberOfVariable = vars1.length;
        numberCustomer = 7;
        Variable[] varsDecision1 = new Variable[numberOfVariable];
        Variable[] varsDecision2 = new Variable[numberOfVariable];
        for (int i = 0; i < numberOfVariable; i++) {
            varsDecision1[i] = new Int(vars1[i], 1, numberOfVariable);
            varsDecision2[i] = new Int(vars2[i], 1, numberOfVariable);
        }
        Solution solution1 = null;
        try {
            solution1 = new Solution(varsDecision1);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RBXCrossover.class.getName()).log(Level.SEVERE, null, ex);
        }
        ArrayList<Variable> solutionList = new ArrayList<>(Arrays.asList(solution1.getDecisionVariables()));
        ArrayList<ArrayList<Variable>> matrixSolution = new ArrayList();
        ArrayList<Variable> route = new ArrayList();
        ArrayList<Variable> notExisting = new ArrayList();
        int j = 0, c = 1, i = 0;
        for (i = 0; i < numberCustomer; i++) {
            try {
                System.out.println("Customer : " + i);
                boolean found = false;
                j = 1;
                Variable var = solution1.getDecisionVariable(i);
                var.setValue(i);
                while (!found && j < solution1.getDecisionVariables().length - 1) {
                    try {
                        if (solution1.getDecisionVariable(j).compare(numberCustomer) == -1 && solution1.getDecisionVariable(j).compare(i) == 0) {
                            found = true;

                        } else {
                            j++;
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(RBXCrossover.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                System.out.println("   ==> " + found);
                if (!found) {
                    notExisting.add(var);
                }
            } catch (Exception ex) {
                Logger.getLogger(RBXCrossover.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        for (int k = 0; k < notExisting.size(); k++) {
            System.out.print(notExisting.get(k) + "\t");
        }
        System.out.println();
        System.out.println();
        System.out.println();
        Solution solution2 = null;
        try {
            solution2 = new Solution(varsDecision2);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RBXCrossover.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(solution1.toString());
        System.out.println(solution2.toString());
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("customerNumber", numberCustomer);
        RBXCrossover operator = new RBXCrossover(parameters);
        Object[] solutions = new Object[]{solution1, solution2};
        Object[] result = operator.execute(solutions);
        System.out.println(((Solution) result[0]).toString());
        System.out.println();
        System.out.println();
        System.out.println();
        ArrayList<Integer> test = new ArrayList<>();
        test.add(3);
        test.add(5);
        test.add(7);
        test.add(8);
        for (i = 0; i < test.size(); i++) {
            System.out.print(test.get(i) + "\t");
        }
        test.add(2, 1);
        System.out.println();
        for (i = 0; i < test.size(); i++) {
            System.out.print(test.get(i) + "\t");
        }
        System.out.println();
    }
}
