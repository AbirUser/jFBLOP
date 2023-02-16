/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blp.operators.selection;

import blp.core.Operator;
import blp.core.Solution;
import java.util.HashMap;

/**
 *
 * @author Abir
 */
public class BinaryTournament extends Operator {

    public BinaryTournament(HashMap<String, Object> parameters) {
        super(parameters);
    }

    @Override
    public Object[] execute(Object[] solutions) {
        Solution solution1 = (Solution) solutions[0];
        Solution solution2 = (Solution) solutions[1];
        Object[] result = new Integer[1];
        if (solution1.getProblem().isMaximize()) {
            if (solution1.getFitness() >= solution2.getFitness()) {
                result[0] = 0;
            } else {
                result[0] = 1;
            }
        } else {
            if (solution1.getFitness() <= solution2.getFitness()) {
                result[0] = 0;
            } else {
                result[0] = 1;
            }
        }

        //System.out.println("BinaryTournament result : " + (((Integer[]) result) [0]));
        return result;
    }

}
