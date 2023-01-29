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
public class OneOpt extends Operator {

    public OneOpt(HashMap<String, Object> parameters) {
        super(parameters);
    }

    @Override
    public Object[] execute(Object[] solutions) {
        Object[] result = new Object[1];
        Solution solution = new Solution((Solution) solutions[0]);
        try {
            int index = PseudoRandom.randInt(0, solution.getDecisionVariables().length - 1);
            int number = (Integer) solution.getDecisionVariable(index).getValue();
            while (number == (Integer) solution.getDecisionVariable(index).getValue()) {
                number = (int) solution.getDecisionVariable(index).generateValue(index);
            }
            Variable[] var = solution.getDecisionVariables();
            var[index].setValue(number);
            solution.setDecisionVariables(var);
        } catch (Exception ex) {
            Logger.getLogger(OneOpt.class.getName()).log(Level.SEVERE, null, ex);
        }
        result[0] = solution;
        return result;
    }

}
