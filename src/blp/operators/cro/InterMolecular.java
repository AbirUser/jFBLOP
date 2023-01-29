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
public class InterMolecular extends Operator {
    
    private ArrayList<Integer> parentIndex;
    private String level;

    public InterMolecular(HashMap<String, Object> parameters) {
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
        Object[] result = new Object[2];
        Solution solution1 = new Solution((Solution) solutions[0]);
        Solution solution2 = new Solution((Solution) solutions[1]);
        solution1.setParent1Index(parentIndex.get(0));
        solution1.setParent2Index(parentIndex.get(1));
        solution2.setParent1Index(parentIndex.get(0));
        solution2.setParent2Index(parentIndex.get(1));
        try {
            if (level.equals("upper")) {
                int ligne = PseudoRandom.randInt(0, solution1.getDecisionVariables().length - 1);
                int colne = PseudoRandom.randInt(0, solution1.getDecisionVariable(ligne).getLength() - 1);
                Object newValue = solution1.getDecisionVariable(ligne).getVariable(colne).generateValue(colne);
                solution1.getDecisionVariable(ligne).setValue(newValue, colne);
                ligne = PseudoRandom.randInt(0, solution2.getDecisionVariables().length - 1);
                colne = PseudoRandom.randInt(0, solution2.getDecisionVariable(ligne).getLength() - 1);
                newValue = solution2.getDecisionVariable(ligne).getVariable(colne).generateValue(colne);
                solution2.getDecisionVariable(ligne).setValue(newValue, colne);
            } else {
                int pos = PseudoRandom.randInt(0, solution1.getDecisionVariables().length - 1);
                Object newValue = solution1.getDecisionVariable(pos).generateValue(pos);
                solution1.getDecisionVariable(pos).setValue(newValue);
                pos = PseudoRandom.randInt(0, solution2.getDecisionVariables().length - 1);
                newValue = solution2.getDecisionVariable(pos).generateValue(pos);
                solution2.getDecisionVariable(pos).setValue(newValue);
            }
            solution1.addHit();
            solution2.addHit();
            solution1.setOperationType("inter");
            solution2.setOperationType("inter");
            result[0] = solution1;
            result[1] = solution2;
            return result;
        } catch (Exception e) {
            Logger.getLogger(OnWall.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }
    
}
