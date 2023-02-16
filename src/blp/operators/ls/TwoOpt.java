/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blp.operators.ls;

import blp.core.Operator;
import blp.core.Solution;
import blp.core.Variable;
import blp.util.PseudoRandom;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Abir
 */
public class TwoOpt extends Operator {

    public TwoOpt(HashMap<String, Object> parameters) {
        super(parameters);
    }

    @Override
    public Object[] execute(Object[] solutions) {
        Object[] result = new Object[1];
        Solution solution = new Solution((Solution) solutions[0]);
        try {
            int index1 = PseudoRandom.randInt(0, solution.getDecisionVariables().length - 1);
            int index2 = PseudoRandom.randInt(0, solution.getDecisionVariables().length - 1);
            while (index1 == index2) {
                index2 = PseudoRandom.randInt(0, solution.getDecisionVariables().length - 1);
            }
            int number1 = (Integer) solution.getDecisionVariable(index1).getValue();
            while (number1 == (Integer) solution.getDecisionVariable(index1).getValue()) {
                number1 = (int) solution.getDecisionVariable(index1).generateValue(index1);
            }
            int number2 = (Integer) solution.getDecisionVariable(index2).getValue();
            while (number2 == (Integer) solution.getDecisionVariable(index2).getValue()) {
                number2 = (int) solution.getDecisionVariable(index2).generateValue(index2);
            }
            Variable[] var = solution.getDecisionVariables();
            var[index1].setValue(number1);
            var[index2].setValue(number2);
            solution.setDecisionVariables(var);
        } catch (Exception ex) {
            Logger.getLogger(OneOpt.class.getName()).log(Level.SEVERE, null, ex);
        }
        result[0] = solution;
        return result;
    }
}
