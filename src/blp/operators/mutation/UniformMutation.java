/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blp.operators.mutation;

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
public class UniformMutation extends Operator {

    private double mutationProbability;
    private String level;

    public UniformMutation(HashMap<String, Object> parameters) {
        super(parameters);
        if (super.getParametre("level") == null) {
            try {
                throw new BLPException("Level `level` is not defined");
            } catch (BLPException ex) {
                Logger.getLogger(UniformMutation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (super.getParametre("mutationProbability") == null) {
            try {
                throw new BLPException("Mutation probability `mutationProbability` is not defined");
            } catch (BLPException ex) {
                Logger.getLogger(UniformMutation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        mutationProbability = (double) getParametre("mutationProbability");
        level = (String) getParametre("level");
    }

    @Override
    public Object[] execute(Object[] solutions) {
        Solution solution = new Solution((Solution) solutions[0]);
        if (PseudoRandom.randDouble(0, 1) < (double) getParametre("mutationProbability")) {
            try {
                if (level.equals("upper")) {
                    if (ArrayIntSolutionType.class.isInstance(solution.getProblem().getUpperLevelSolutionType())) {
                        int ligne = PseudoRandom.randInt(0, solution.getDecisionVariables().length - 1);
                        int index = PseudoRandom.randInt(0, solution.getDecisionVariable(ligne).getLength() - 1);
                        Object number = solution.getDecisionVariable(ligne).getValue(index);
                        if (solution.getDecisionVariable(ligne).getVariable(index).getLowerBound().equals(solution.getDecisionVariable(ligne).getVariable(index).getUpperBound())) {
                            number = solution.getDecisionVariable(ligne).getVariable(index).generateValue(index);
                        } else {
                            while (number.equals(solution.getDecisionVariable(ligne).getValue(index))) {
                                number = solution.getDecisionVariable(ligne).getVariable(index).generateValue(index);
                            }
                        }
                        solution.getDecisionVariable(ligne).setValue(number, index);
                    } else if (IntSolutionType.class.isInstance(solution.getProblem().getUpperLevelSolutionType())) {
                        int index = PseudoRandom.randInt(0, solution.getDecisionVariables().length - 1);
                        Object number = solution.getDecisionVariable(index).getValue();
                        if (solution.getDecisionVariable(index).getLowerBound().equals(solution.getDecisionVariable(index).getUpperBound())) {
                            number = solution.getDecisionVariable(index).generateValue(index);
                        } else {
                            while (number.equals(solution.getDecisionVariable(index).getValue())) {
                                number = solution.getDecisionVariable(index).generateValue(index);
                            }
                        }
                        Variable[] var = solution.getDecisionVariables();
                        var[index].setValue(number);
                        solution.setDecisionVariables(var);
                    }
                } else {
                    int index = PseudoRandom.randInt(0, solution.getDecisionVariables().length - 1);
                    Object number = solution.getDecisionVariable(index).getValue();
                    if (solution.getDecisionVariable(index).getLowerBound().equals(solution.getDecisionVariable(index).getUpperBound())) {
                        number = solution.getDecisionVariable(index).generateValue(index);
                    } else {
                        while (number.equals(solution.getDecisionVariable(index).getValue())) {
                            number = solution.getDecisionVariable(index).generateValue(index);
                        }
                    }
                    Variable[] var = solution.getDecisionVariables();
                    var[index].setValue(number);
                    solution.setDecisionVariables(var);
                }
            } catch (Exception ex) {
                Logger.getLogger(UniformMutation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Object[] result = {solution};
        return result;
    }

}
