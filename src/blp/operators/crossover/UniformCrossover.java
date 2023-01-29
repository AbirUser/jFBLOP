/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
public class UniformCrossover extends Operator {

    private String level;

    public UniformCrossover(HashMap<String, Object> parameters) {
        super(parameters);
        if (super.getParametre("level") == null) {
            try {
                throw new BLPException("Level `level` is not defined");
            } catch (BLPException ex) {
                Logger.getLogger(UniformCrossover.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        level = (String) getParametre("level");
    }

    @Override
    public Object[] execute(Object[] solutions) {
        Solution parent1 = (Solution) solutions[0];
        Solution parent2 = (Solution) solutions[1];
        Solution offspring1 = new Solution(parent1), offspring2 = new Solution(parent2);
        try {
            if (level.equals("upper")) {
                if (ArrayIntSolutionType.class.isInstance(parent1.getProblem().getUpperLevelSolutionType())) {
                    for (int i = 0; i < parent1.getDecisionVariables().length; i++) {
                        for (int j = 0; j < parent1.getDecisionVariable(i).getLength(); j++) {
                            double rand = PseudoRandom.randDouble(0, 1);
                            if (rand < 0.5) {
                                Object aux = offspring1.getDecisionVariable(i).getValue(j);
                                offspring1.getDecisionVariable(i).setValue(offspring2.getDecisionVariable(i).getValue(j), i);
                                offspring2.getDecisionVariable(i).setValue(aux, i);
                            }
                        }
                    }
                } else if (IntSolutionType.class.isInstance(parent1.getProblem().getUpperLevelSolutionType())) {
                    for (int i = 0; i < parent1.getDecisionVariables().length; i++) {
                        double rand = PseudoRandom.randDouble(0, 1);
                        if (rand < 0.5) {
                            Variable[] var1 = parent1.getDecisionVariables();
                            Variable[] var2 = parent2.getDecisionVariables();
                            Variable aux = var1[i];
                            var1[i] = var2[i];
                            var2[i] = aux;
                            offspring1.setDecisionVariables(var1);
                            offspring2.setDecisionVariables(var2);
                        }
                    }
                }
            } else {
                for (int i = 0; i < parent1.getDecisionVariables().length; i++) {
                    double rand = PseudoRandom.randDouble(0, 1);
                    if (rand < 0.5) {
                        Variable[] var1 = parent1.getDecisionVariables();
                        Variable[] var2 = parent2.getDecisionVariables();
                        Variable aux = var1[i];
                        var1[i] = var2[i];
                        var2[i] = aux;
                        offspring1.setDecisionVariables(var1);
                        offspring2.setDecisionVariables(var2);
                    }
                }
            }
        } catch (Exception e) {
            Logger.getLogger(UniformCrossover.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        Object[] result = {offspring1, offspring2};
        return result;
    }

}
