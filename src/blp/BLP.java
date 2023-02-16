/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blp;

import blp.core.Solution;
import blp.core.Variable;
import blp.encodings.variable.Int;
import blp.util.PseudoRandom;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Abir
 */
public class BLP {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        int[] vars1 = new int[15];//{8, 2, 5, 1, 8, 9, 7, 2, 4, 9, 10, 0, 2, 10};
        int numberOfVariable = vars1.length;
        int numberCustomer = 12;
        int j = 0, i = 0;
        for (i = 0; i < numberOfVariable; i++) {
            vars1[i] = PseudoRandom.randInt(0, numberCustomer);
        }
        
        Variable[] varsDecision1 = new Variable[numberOfVariable];
        for (i = 0; i < numberOfVariable; i++) {
            varsDecision1[i] = new Int(vars1[i], 1, numberOfVariable);
        }
        Solution solution1 = null;
        try {
            solution1 = new Solution(varsDecision1);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BLP.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(solution1.toString());
        ArrayList<Variable> notExisting = new ArrayList();
        ArrayList<ArrayList<Integer>> indexExisting = new ArrayList<>();

        for (i = 0; i < numberCustomer; i++) {
            try {
                ArrayList<Integer> index = new ArrayList<>();
                boolean found = false;
                j = 1;
                Variable var = solution1.getDecisionVariable(i).copy();
                var.setValue(i);
                for (j = 0; j < solution1.getDecisionVariables().length; j++) {
                    try {
                        if (solution1.getDecisionVariable(j).compare(numberCustomer) == -1 && solution1.getDecisionVariable(j).compare(i) == 0) {
                            if (found) {
                                index.add(j);
                            }
                            found = true;
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(BLP.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(BLP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        for (i = 0; i < notExisting.size(); i++) {
            System.out.print(notExisting.get(i) + "\t");
        }
        System.out.println();
        System.out.println();
        System.out.println();
        for (i = 0; i < indexExisting.size(); i++) {
            System.out.print(i+" ==> ");
            for (j = 0; j < indexExisting.get(i).size(); j++) {
                System.out.print(indexExisting.get(i).get(j) + "\t");
            }
            System.out.println();
        }
        System.out.println();
        i = 0;
        while (!notExisting.isEmpty()) {
            for (j = 0; j < indexExisting.size(); j++) {
                if (!indexExisting.get(j).isEmpty() && indexExisting.get(j).get(0) != -1) {
                    solution1.getDecisionVariable(indexExisting.get(j).get(0)).setValue(notExisting.get(0).getValue());
                    indexExisting.get(j).remove(0);
                    notExisting.remove(0);
                }
                if(notExisting.isEmpty()) break;
            }
        }
        System.out.println();
        System.out.println("New Solution ==> " + solution1.toString());
    }
}
