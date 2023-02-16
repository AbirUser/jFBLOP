package blp.operators.ls;

import blp.core.Operator;
import blp.core.Solution;
import blp.core.Variable;
import blp.util.BLPException;
import blp.util.PseudoRandom;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Abir
 */
public class UniformOpt extends Operator {

    private double optProbability;

    public UniformOpt(HashMap<String, Object> parameters) {
        super(parameters);
        if (super.getParametre("optProbability") == null) {
            try {
                throw new BLPException("Mutation probability `optProbability` is not defined");
            } catch (BLPException ex) {
                Logger.getLogger(UniformOpt.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        optProbability = (double) getParametre("optProbability");
    }

    @Override
    public Object[] execute(Object[] solutions) {
        Object[] result = new Object[1];
        Solution solution = new Solution((Solution) solutions[0]);
        Variable[] var = solution.getDecisionVariables();
        for (int i = 0; i < solution.getDecisionVariables().length; i++) {
            if (PseudoRandom.randDouble(0, 1) < optProbability) {
                try {
                    int number = (Integer) solution.getDecisionVariable(i).getValue();
                    while (number == (Integer) solution.getDecisionVariable(i).getValue()) {
                        number = (int) solution.getDecisionVariable(i).generateValue(i);
                    }
                    var[i].setValue(number);
                } catch (Exception ex) {
                    Logger.getLogger(UniformOpt.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        solution.setDecisionVariables(var);
        result[0] = solution;
        return result;
    }

}
