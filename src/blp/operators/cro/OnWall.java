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
public class OnWall extends Operator {

    private ArrayList<Integer> parentIndex;
    private String level;
    private double iniKE;

    public OnWall(HashMap<String, Object> parameters) {
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
        iniKE = (super.getParametre("iniKE") != null) ? (Double) getParametre("iniKE") : -1;
        level = (String) getParametre("level");
        parentIndex = (ArrayList<Integer>) getParametre("parentIndex");
    }

    @Override
    public Object[] execute(Object[] solutions) {
        Object[] result = new Object[1];
        Solution solution = new Solution((Solution) solutions[0]);
        try {
            if (level.equals("upper")) {
                int ligne = PseudoRandom.randInt(0, solution.getDecisionVariables().length - 1);
                int colne = PseudoRandom.randInt(0, solution.getDecisionVariable(ligne).getLength() - 1);
                Object newValue = solution.getDecisionVariable(ligne).getVariable(colne).generateValue(colne);
                solution.getDecisionVariable(ligne).setValue(newValue, colne);
            } else {
                int pos = PseudoRandom.randInt(0, solution.getDecisionVariables().length - 1);
                Object newValue = solution.getDecisionVariable(pos).generateValue(pos);
                solution.getDecisionVariable(pos).setValue(newValue);
            }
            if(parentIndex.isEmpty()) {
                solution.setNumHit(0);
                solution.setMinHit(0);
                solution.setKE(iniKE);
            } else if(parentIndex.get(0) == -1) {
                solution.setNumHit(0);
                solution.setMinHit(0);
                solution.setKE(iniKE);
            } else {
                solution.addHit();
                solution.setParent1Index(parentIndex.get(0));
            }
            solution.setOperationType("wall");
            result[0] = solution;
            return result;
        } catch (Exception ex) {
            Logger.getLogger(OnWall.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
