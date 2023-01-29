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
public class TwoInterchange extends Operator {

    public TwoInterchange(HashMap<String, Object> parameters) {
        super(parameters);
    }

    @Override
    public Object[] execute(Object[] solutions) {
        Object[] result = new Object[1];
        Solution solution = new Solution((Solution) solutions[0]);
        try {
            int index1 = PseudoRandom.randInt(0, solution.getDecisionVariables().length - 1);
            int index2 = index1;
            while (index1 == index2) {
                index2 = PseudoRandom.randInt(0, solution.getDecisionVariables().length - 1);
            }
            Object aux = solution.getDecisionVariable(index1).getValue();
            solution.getDecisionVariable(index1).setValue(solution.getDecisionVariable(index2).getValue());
            solution.getDecisionVariable(index2).setValue(aux);
        } catch (Exception ex) {
            Logger.getLogger(TwoInterchange.class.getName()).log(Level.SEVERE, null, ex);
        }
        result[0] = solution;
        return result;
    }
    
}
