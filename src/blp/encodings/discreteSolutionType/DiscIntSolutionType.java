/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blp.encodings.discreteSolutionType;

import blp.core.Problem;
import blp.core.SolutionType;
import blp.core.Variable;
import blp.encodings.discretevariable.DiscInt;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Abir
 */
public class DiscIntSolutionType extends SolutionType {

    public DiscIntSolutionType(Problem problem) {
        super(problem);
    }

    @Override
    public Variable[] createVariables(String level) throws ClassNotFoundException {
        Variable[] variables = null;
        if(level.equals("upper") || level.equals("lower")) {
            if(level.equals("upper")) {
                variables = new Variable[problem_.getUpperLevelNumberOfVariables()];
            } else {
                variables = new Variable[problem_.getLowerLevelNumberOfVariables()];
            }
            for (int i = 0; i < variables.length; i++) {
                Variable var;
                if(level.equals("upper")) {
                    var = problem_.getDecisionVariablesAndDomainUpperLevel().get(i);
                } else {
                    var = problem_.getDecisionVariablesAndDomainLowerLevel().get(i);
                }
                try {
                    int [] discInt = new int[var.getDomain().length];
                    for (int j = 0; j < var.getDomain().length; j++) {
                        discInt[j] = (int) var.getDomain()[j];
                    }
                    variables[i] = new DiscInt(discInt);
                } catch (Exception ex) {
                    Logger.getLogger(DiscIntSolutionType.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return variables;
    }
    
}
