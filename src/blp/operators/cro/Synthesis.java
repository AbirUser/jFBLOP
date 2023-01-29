package blp.operators.cro;

import blp.core.Operator;
import blp.core.Solution;
import blp.util.BLPException;
import blp.util.PseudoRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Abir
 */
public class Synthesis extends Operator {

    private ArrayList<Integer> parentIndex;
    private String level;

    public Synthesis(HashMap<String, Object> parameters) {
        super(parameters);
        if (super.getParametre("parentIndex") == null) {
            try {
                throw new BLPException("Parent index `parentIndex` is not defined");
            } catch (BLPException ex) {
                Logger.getLogger(OnWall.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (super.getParametre("level") == null) {
            try {
                throw new BLPException("Parent index `level` is not defined");
            } catch (BLPException ex) {
                Logger.getLogger(OnWall.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        level = (String) getParametre("level");
        parentIndex = (ArrayList<Integer>) getParametre("parentIndex");
    }

    @Override
    public Object[] execute(Object[] solutions) {
        Object[] result = new Object[1];
        Solution solution1 = new Solution((Solution) solutions[0]);
        Solution solution2 = new Solution((Solution) solutions[1]);
        if (parentIndex.size() == 2) {
            solution1.setParent1Index(parentIndex.get(0));
            solution1.setParent2Index(parentIndex.get(1));
            solution2.setParent1Index(parentIndex.get(0));
            solution2.setParent2Index(parentIndex.get(1));
        }
        try {
            if (level.equals("upper")) {
                for (int i = 0; i < solution1.getDecisionVariables().length; i++) {
                    for (int j = 0; j < solution1.getDecisionVariable(i).getLength(); j++) {
                        if (PseudoRandom.randDouble() > 0.5) {
                            solution1.getDecisionVariable(i).setValue(solution2.getDecisionVariable(i).getValue(j), j);
                        }
                    }
                }
            } else {
                for (int i = 0; i < solution1.getDecisionVariables().length; i++) {
                    if (PseudoRandom.randDouble() > 0.5) {
                        solution1.getDecisionVariable(i).setValue(solution2.getDecisionVariable(i).getValue());
                    }
                }
            }
            solution1.addHit();
            solution1.setOperationType("syn");
            solution2.setOperationType("syn");
            result[0] = solution1;
            return result;
        } catch (Exception e) {
            Logger.getLogger(OnWall.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

}
