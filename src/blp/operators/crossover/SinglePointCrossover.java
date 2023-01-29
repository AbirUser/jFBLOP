package blp.operators.crossover;

import blp.core.Operator;
import blp.core.Solution;
import blp.core.Variable;
import blp.encodings.solutionType.ArrayIntSolutionType;
import blp.encodings.solutionType.IntSolutionType;
import blp.util.BLPException;
import blp.util.PseudoRandom;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Abir
 */
public class SinglePointCrossover extends Operator {

    private String level;

    public SinglePointCrossover(HashMap<String, Object> parameters) {
        super(parameters);
        if (super.getParametre("level") == null) {
            try {
                throw new BLPException("Level `level` is not defined");
            } catch (BLPException ex) {
                Logger.getLogger(SinglePointCrossover.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        level = (String) getParametre("level");
    }

    @Override
    public Object[] execute(Object[] solutions) {
        Solution parent1 = (Solution) solutions[0];
        Solution parent2 = (Solution) solutions[1];
        Solution offspring1 = new Solution(parent1), offspring2 = new Solution(parent2);

        if (level.equals("upper")) {
            if (ArrayIntSolutionType.class.isInstance(parent1.getProblem().getUpperLevelSolutionType())) {
                try {
                    int ligne = PseudoRandom.randInt(0, parent1.getDecisionVariables().length - 1);
                    int index = PseudoRandom.randInt(1, parent1.getDecisionVariable(ligne).getLength() - 2);
                    for (int i = index; i < parent1.getDecisionVariable(ligne).getLength(); i++) {
                        Variable[] var1 = parent1.getDecisionVariables();
                        Variable[] var2 = parent2.getDecisionVariables();
                        var1[i] = parent2.getDecisionVariable(i);
                        var2[i] = parent1.getDecisionVariable(i);
                        offspring1.setDecisionVariables(var1);
                        offspring2.setDecisionVariables(var2);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(SinglePointCrossover.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (IntSolutionType.class.isInstance(parent1.getProblem().getUpperLevelSolutionType())) {
                int index = PseudoRandom.randInt(1, parent1.getDecisionVariables().length - 2);
                for (int i = index; i < parent1.getDecisionVariables().length; i++) {
                    Variable[] var1 = parent1.getDecisionVariables();
                    Variable[] var2 = parent2.getDecisionVariables();
                    var1[i] = parent2.getDecisionVariable(i);
                    var2[i] = parent1.getDecisionVariable(i);
                    offspring1.setDecisionVariables(var1);
                    offspring2.setDecisionVariables(var2);
                }
            }
        } else {
            if (ArrayIntSolutionType.class.isInstance(parent1.getProblem().getUpperLevelSolutionType())) {
                try {
                    int ligne = PseudoRandom.randInt(0, parent1.getDecisionVariables().length - 1);
                    int index = PseudoRandom.randInt(1, parent1.getDecisionVariable(ligne).getLength() - 2);
                    for (int i = index; i < parent1.getDecisionVariable(ligne).getLength(); i++) {
                        Variable[] var1 = parent1.getDecisionVariables();
                        Variable[] var2 = parent2.getDecisionVariables();
                        var1[i] = parent2.getDecisionVariable(i);
                        var2[i] = parent1.getDecisionVariable(i);
                        offspring1.setDecisionVariables(var1);
                        offspring2.setDecisionVariables(var2);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(SinglePointCrossover.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (IntSolutionType.class.isInstance(parent1.getProblem().getUpperLevelSolutionType())) {
                int index = PseudoRandom.randInt(1, parent1.getDecisionVariables().length - 2);
                for (int i = index; i < parent1.getDecisionVariables().length; i++) {
                    Variable[] var1 = parent1.getDecisionVariables();
                    Variable[] var2 = parent2.getDecisionVariables();
                    var1[i] = parent2.getDecisionVariable(i);
                    var2[i] = parent1.getDecisionVariable(i);
                    offspring1.setDecisionVariables(var1);
                    offspring2.setDecisionVariables(var2);
                }
            }
        }
        Object[] result = {offspring1, offspring2};
        return result;
    }

}
