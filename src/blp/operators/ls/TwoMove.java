package blp.operators.ls;

import blp.core.Operator;
import blp.core.Solution;
import blp.util.BLPException;
import blp.util.PseudoRandom;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Abir
 */
public class TwoMove extends Operator {

    private static int numberOfDepots;
    private static int numberOfPlants;
    
    public TwoMove(HashMap<String, Object> parameters) {
        super(parameters);
        if (super.getParametre("numberOfDepots") == null) {
            try {
                throw new BLPException("Number of depots `numberOfDepots` is not defined");
            } catch (BLPException ex) {
                Logger.getLogger(TwoMove.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (super.getParametre("numberOfPlants") == null) {
            try {
                throw new BLPException("Number of plants `numberOfPlants` is not defined");
            } catch (BLPException ex) {
                Logger.getLogger(TwoMove.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        numberOfDepots = (Integer) getParametre("numberOfDepots");
        numberOfPlants = (Integer) getParametre("numberOfPlants");
    }

    @Override
    public Object[] execute(Object[] solutions) {
        Object[] result = new Object[1];
        Solution solution = new Solution((Solution) solutions[0]);
        try {
            int[][] matrixSolution = new int[numberOfDepots][numberOfPlants];
            int i = 0, j = -1;
            for (int c = 0; c < solution.getDecisionVariables().length; c++) {
                if (j == numberOfPlants - 1) {
                    j = 0;
                    i++;
                } else {
                    j++;
                }
                matrixSolution[i][j] = (Integer) solution.getDecisionVariable(c).getValue();
            }
            int col1 = PseudoRandom.randInt(0, numberOfPlants - 1);
            int col2 = col1;
            while (col1 == col2) {
                col2 = PseudoRandom.randInt(0, numberOfPlants - 1);
            }
            for (int k = 0; k < numberOfDepots; k++) {
                int aux = matrixSolution[k][col1];
                matrixSolution[k][col1] = matrixSolution[k][col2];
                matrixSolution[k][col2] = aux;
            }
            int c = 0;
            for (i = 0; i < numberOfDepots; i++) {
                for (j = 0; j < numberOfPlants; j++) {
                    solution.getDecisionVariable(c).setValue(matrixSolution[i][j]);
                    c++;
                }
            }
            result[0] = solution;
        } catch (Exception ex) {
            Logger.getLogger(TwoMove.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
