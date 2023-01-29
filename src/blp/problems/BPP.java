package blp.problems;

import blp.core.Problem;
import blp.core.Solution;

/**
 *
 * @author Abir
 */
public class BPP extends Problem {
    
    private String instance;
    private int numberOfBins;
    private int numberOfObjects;
    private double[] weightOfObjects;
    private double[] weightOfBins;
    
    /**
     * Creates a default BPP problem
     * @param instance
     * @param upperLevelSolutionType
     * @param lowerLevelSolutionType
     */
    public BPP(String instance, String upperLevelSolutionType, String lowerLevelSolutionType) {
        this.instance = instance;
        importInstanceSetting(instance);
    }

    @Override
    public void evaluate(Solution upperSolution, Solution lowerSolution) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void evaluateLowerLevel(Solution upperSolution, Solution lowerSolution) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void evaluateUpperLevelConstraints(Solution solution) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void evaluateLowerLevelConstraints(Solution solution) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    private void importInstanceSetting(String instance) {
        
    }
}
