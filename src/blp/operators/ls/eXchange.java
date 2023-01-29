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
public class eXchange extends Operator {

    private static int numberOfDepots;
    private static int numberOfPlants;

    public eXchange(HashMap<String, Object> parameters) {
        super(parameters);
        if (super.getParametre("numberOfDepots") == null) {
            try {
                throw new BLPException("Number of depots `numberOfDepots` is not defined");
            } catch (BLPException ex) {
                Logger.getLogger(eXchange.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (super.getParametre("numberOfPlants") == null) {
            try {
                throw new BLPException("Number of plants `numberOfPlants` is not defined");
            } catch (BLPException ex) {
                Logger.getLogger(eXchange.class.getName()).log(Level.SEVERE, null, ex);
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
            int ligne1 = PseudoRandom.randInt(0, numberOfDepots - 1);
            int ligne2 = ligne1;
            while (ligne1 == ligne2) {
                ligne2 = PseudoRandom.randInt(0, numberOfDepots - 1);
            }
            int[] aux = matrixSolution[ligne1];
            matrixSolution[ligne1] = matrixSolution[ligne2];
            matrixSolution[ligne2] = aux;
            int c = 0;
            for (i = 0; i < numberOfDepots; i++) {
                for (j = 0; j < numberOfPlants; j++) {
                    solution.getDecisionVariable(c).setValue(matrixSolution[i][j]);
                    c++;
                }
            }
            result[0] = solution;
        } catch (Exception ex) {
            Logger.getLogger(OneOpt.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
