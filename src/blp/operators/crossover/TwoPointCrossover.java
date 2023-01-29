/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blp.operators.crossover;

import blp.core.Operator;
import blp.core.Solution;
import blp.core.Variable;
import blp.util.PseudoRandom;
import java.util.HashMap;

/**
 *
 * @author Abir
 */
public class TwoPointCrossover extends Operator {

    public TwoPointCrossover(HashMap<String, Object> parameters) {
        super(parameters);
    }

    @Override
    public Object[] execute(Object[] solutions) {
        Solution parent1 = (Solution) solutions[0];
        Solution parent2 = (Solution) solutions[1];
        Solution offspring1 = new Solution(parent1);
        Solution offspring2 = new Solution(parent2);
        int index1 = PseudoRandom.randInt(0, parent1.getDecisionVariables().length);
        int index2 = index1;
        while(index1 == index2) {
            index2 = PseudoRandom.randInt(0, parent1.getDecisionVariables().length);
        }
        if(index2 < index1) {
            int aux = index1;
            index1 = index2;
            index2 = aux;
        }
        for (int i = index1; i < index2; i++) {
            Variable[] var1 = parent1.getDecisionVariables();
            Variable[] var2 = parent2.getDecisionVariables();
            var1[i] = parent2.getDecisionVariable(i);
            var2[i] = parent1.getDecisionVariable(i);
            offspring1.setDecisionVariables(var1);
            offspring2.setDecisionVariables(var2);
        }
        Object[] result = {offspring1, offspring2};
        return result;
    }
}
