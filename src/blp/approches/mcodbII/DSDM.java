package blp.approches.mcodbII;

import blp.core.Problem;
import blp.core.Solution;
import blp.core.SolutionSet;
import blp.core.Variable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Abir
 */
public class DSDM {

    private SolutionSet referencePoints;
    private Problem problem;
    private String level;

    public DSDM(Problem problem, int p, String level) throws Exception {
        this.problem = problem;
        this.level = level;
        ArrayList<ArrayList> ranges = new ArrayList();
        int maxSizeRange = 0;
        Variable mini, maxi;
        double spacing;
        Solution solution;
        if (level.equals("upper")) {
            solution = new Solution(problem, "upper");
            int decisionVariablesNum = solution.getDecisionVariables().length * solution.getDecisionVariable(0).getLength();
            int c = 0, k = 0;
            for (int i = 0; i < decisionVariablesNum; i++) {
                if (k >= solution.getDecisionVariable(c).getLength()) {
                    k = 0;
                    c++;
                }
                spacing = solution.getDecisionVariable(c).calculateSpacing(k, p);
                ArrayList<Variable> rangei = new ArrayList();
                mini = solution.getDecisionVariable(c);
                mini.setValue(solution.getDecisionVariable(c).getLowerBound(k), k);
                mini.getVariable(k);
                rangei.add(mini.getVariable(k));
                while (rangei.get(rangei.size() - 1).isLowerUpperBound()) {
                    Variable r = rangei.get(rangei.size() - 1).closet(spacing);
                    if (!isInTheRanges(r, rangei)) {
                        rangei.add(r);
                    }
                }
                if (maxSizeRange < rangei.size()) {
                    maxSizeRange = rangei.size();
                }
                ranges.add(rangei);
                k++;
            }
            SolutionSet refPoints = combinaison(ranges, maxSizeRange);
            referencePoints = new SolutionSet();
            for (int i = 0; i < refPoints.size(); i++) {
                Solution s = new Solution(problem, "upper");
                c = 0;
                k = 0;
                for (int j = 0; j < refPoints.get(i).getDecisionVariables().length; j++) {
                    if (k >= s.getDecisionVariable(c).getLength()) {
                        k = 0;
                        c++;
                    }
                    s.getDecisionVariable(c).setValue(refPoints.get(i).getDecisionVariable(j).getValue(), k);
                    k++;
                }
                referencePoints.add(s);
            }
        } else {
            solution = new Solution(problem, "lower");
            for (int i = 0; i < solution.getDecisionVariables().length; i++) {
                spacing = solution.getDecisionVariable(i).calculateSpacing(p);
                ArrayList<Variable> rangei = new ArrayList();
                mini = solution.getDecisionVariable(i);
                mini.setValue(solution.getDecisionVariable(i).getLowerBound());
                rangei.add(mini);
                while (rangei.get(rangei.size() - 1).isLowerUpperBound()) {
                    Variable r = rangei.get(rangei.size() - 1).closet(spacing);
                    if (!isInTheRanges(r, rangei)) {
                        rangei.add(r);
                    }
                }
                if (maxSizeRange < rangei.size()) {
                    maxSizeRange = rangei.size();
                }
                ranges.add(rangei);
            }
            referencePoints = combinaison(ranges, maxSizeRange);
        }
    }

    private SolutionSet combinaison(ArrayList<ArrayList> ranges, int maxSizeRange) {
        int index = maxSizeRange;
        int[] compteurs = new int[ranges.size()];
        for (int i = 0; i < compteurs.length; i++) {
            compteurs[i] = 0;
        }
        SolutionSet refPts = new SolutionSet();
        while (index >= 1) {
            for (int i = 1; i < compteurs.length; i++) {
                compteurs[i] = 0;
            }
            for (int i = 0; i < index; i++) {
                Variable[] decisionVars = new Variable[ranges.size()];
                decisionVars[0] = (Variable) ranges.get(0).get(compteurs[0]);
                for (int j = 1; j < ranges.size(); j++) {
                    if (compteurs[j] >= ranges.get(j).size()) {
                        compteurs[j] = 0;
                    }
                    decisionVars[j] = (Variable) ranges.get(j).get(compteurs[j]++);
                }
                Solution newPtRef;
                if(level.equals("upper")) {
                    newPtRef = new Solution(problem, "upper", decisionVars);
                } else {
                    newPtRef = new Solution(problem, "lower", decisionVars);
                }
                refPts.add(newPtRef);
            }
            index--;
            compteurs[0]++;
            if (compteurs[0] >= ranges.get(0).size()) {
                compteurs[0] = 0;
            }
        }
        return refPts;
    }

    private boolean isInTheRanges(Variable var, ArrayList<Variable> ranges) {
        boolean found = false;
        int i = 0;
        while (!found && i < ranges.size()) {
            try {
                if (ranges.get(i).getValue().equals(var.getValue())) {
                    found = true;
                } else {
                    i++;
                }
            } catch (Exception ex) {
                Logger.getLogger(DSDM.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return found;
    }

    public SolutionSet getReferencePoints() {
        return referencePoints;
    }
}