package blp.problems;

import blp.core.Problem;
import blp.core.Solution;
import blp.encodings.solutionType.IntSolutionType;
import blp.util.BLPException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Abir
 */
public class KP extends Problem {

    private String instance;
    private int numberOfObjects;
    private double[] weightOfObjects;
    private double[] profilsOfObjects;
    private int[] sortedProfilsOfObjects;
    private int[] sortedWeightOfObjects;
    private double weightOfSack;
    private static final int PENALIZE_COMMUN_OBJECT = -10;

    /**
     * Creates a default KP problem
     *
     * @param instance
     * @param upperLevelSolutionType
     * @param lowerLevelSolutionType
     */
    public KP(String instance, String upperLevelSolutionType, String lowerLevelSolutionType) {
        this.instance = instance;
        importInstanceSetting(instance);

        /*for (int i = 0; i < weightOfObjects.length; i++) {
            System.out.print(weightOfObjects[i] + "\t");
        }
        System.out.println();

        for (int i = 0; i < sortedWeightOfObjects.length; i++) {
            System.out.print(sortedWeightOfObjects[i] + "\t");
        }
        System.out.println();

        for (int i = 0; i < profilsOfObjects.length; i++) {
            System.out.print(profilsOfObjects[i] + "\t");
        }
        System.out.println();

        for (int i = 0; i < sortedProfilsOfObjects.length; i++) {
            System.out.print(sortedProfilsOfObjects[i] + "\t");
        }
        System.out.println();*/
        
        compareType_ = 1;

        if (upperLevelSolutionType.equals("Int")) {
            solutionType_ = new IntSolutionType(this);
        } else {
            try {
                throw new BLPException("Error: lower level solution type " + upperLevelSolutionType + " invalid");
            } catch (BLPException ex) {
                Logger.getLogger(KP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (lowerLevelSolutionType.equals("Int")) {
            lowerlevelSolutionType_ = new IntSolutionType(this);
        } else {
            try {
                throw new BLPException("Error: lower level solution type " + lowerLevelSolutionType + " invalid");
            } catch (BLPException ex) {
                Logger.getLogger(KP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        numberOfVariablesU_ = numberOfObjects;
        numberOfVariablesL_ = numberOfObjects;

        lowerLimitU_ = new Integer[numberOfVariablesU_];
        upperLimitU_ = new Integer[numberOfVariablesU_];
        lowerLimitL_ = new Integer[numberOfVariablesL_];
        upperLimitL_ = new Integer[numberOfVariablesL_];

        for (int i = 0; i < numberOfObjects; i++) {
            lowerLimitU_[i] = 0;
            upperLimitU_[i] = 1;
            lowerLimitL_[i] = 0;
            upperLimitL_[i] = 1;
        }
    }

    @Override
    public void evaluate(Solution upperSolution, Solution lowerSolution) {
        evaluateUpperLevelConstraints(upperSolution);
        double fitness = 0.0;
        for (int i = 0; i < upperSolution.getDecisionVariables().length; i++) {
            try {
                fitness += ((int) lowerSolution.getDecisionVariable(i).getValue() + (int) upperSolution.getDecisionVariable(i).getValue()) * profilsOfObjects[i] + 2 * PENALIZE_COMMUN_OBJECT * (int) upperSolution.getDecisionVariable(i).getValue() * (int) lowerSolution.getDecisionVariable(i).getValue();
            } catch (Exception ex) {
                Logger.getLogger(KP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        upperSolution.setFitness(fitness);
    }

    @Override
    public void evaluateLowerLevel(Solution upperSolution, Solution lowerSolution) {
        evaluateLowerLevelConstraints(lowerSolution);
        double fitness = 0.0;
        for (int i = 0; i < lowerSolution.getDecisionVariables().length; i++) {
            try {
                fitness += (int) lowerSolution.getDecisionVariable(i).getValue() * profilsOfObjects[i] + PENALIZE_COMMUN_OBJECT * (int) upperSolution.getDecisionVariable(i).getValue() * (int) lowerSolution.getDecisionVariable(i).getValue();
            } catch (Exception ex) {
                Logger.getLogger(KP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        lowerSolution.setFitness(fitness);
    }

    @Override
    public void evaluateUpperLevelConstraints(Solution solution) {
        try {
            double weight = 0.0;
            for (int i = 0; i < solution.getDecisionVariables().length; i++) {
                if (solution.getDecisionVariable(i).compare(1) == 0) {
                    weight += weightOfObjects[i];
                }

            }
            int i = 0;
            while (weight > weightOfSack) {
                int pos = -1;
                while (pos == -1 && i < sortedProfilsOfObjects.length) {
                    if (solution.getDecisionVariable(sortedProfilsOfObjects[i]).compare(1) == 0) {
                        pos = sortedProfilsOfObjects[i];
                    }
                    i++;
                }
                if (pos != -1) {
                    weight -= weightOfObjects[pos];
                    solution.getDecisionVariable(pos).setValue(0);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(KP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void evaluateLowerLevelConstraints(Solution solution) {
        try {
            double weight = 0.0;
            for (int i = 0; i < solution.getDecisionVariables().length; i++) {
                if (solution.getDecisionVariable(i).compare(1) == 0) {
                    weight += weightOfObjects[i];
                }

            }
            int i = 0;
            while (weight > weightOfSack) {
                int pos = -1;
                while (pos == -1 && i < sortedProfilsOfObjects.length) {
                    if (solution.getDecisionVariable(sortedProfilsOfObjects[i]).compare(1) == 0) {
                        pos = sortedProfilsOfObjects[i];
                    }
                    i++;
                }
                if (pos != -1) {
                    weight -= weightOfObjects[pos];
                    solution.getDecisionVariable(pos).setValue(0);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(KP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void importInstanceSetting(String instance) {
        String path = System.getProperty("user.dir") + System.getProperty("file.separator") + "src" + System.getProperty("file.separator") + "blp" + System.getProperty("file.separator") + "problems" + System.getProperty("file.separator") + "instances" + System.getProperty("file.separator") + "kp" + System.getProperty("file.separator") + instance;
        try {
            InputStream ips = new FileInputStream(path);
            InputStreamReader ipsr = new InputStreamReader(ips);
            BufferedReader br = new BufferedReader(ipsr);
            String ligne;
            int ligneNumber = 1;
            while ((ligne = br.readLine()) != null) {
                if (ligneNumber == 1) {
                    ligne = ligne.trim();
                    weightOfSack = Integer.parseInt(ligne);
                } else if (ligneNumber == 2) {
                    ligne = ligne.trim();
                    numberOfObjects = Integer.parseInt(ligne);
                } else if (ligneNumber == 3) {
                    weightOfObjects = new double[numberOfObjects];
                    int i = 0;
                    while (!ligne.isEmpty()) {
                        if (ligne.indexOf("\t") != -1) {
                            weightOfObjects[i++] = Double.parseDouble(ligne.substring(0, ligne.indexOf("\t")));
                            ligne = ligne.substring(ligne.indexOf("\t") + 1, ligne.length());
                            ligne = ligne.trim();
                        } else {
                            weightOfObjects[i] = Double.parseDouble(ligne);
                            ligne = ligne.trim();
                            ligne = "";
                        }
                    }
                } else if (ligneNumber == 4) {
                    profilsOfObjects = new double[numberOfObjects];
                    int i = 0;
                    while (!ligne.isEmpty()) {
                        if (ligne.indexOf("\t") != -1) {
                            profilsOfObjects[i++] = Double.parseDouble(ligne.substring(0, ligne.indexOf("\t")));
                            ligne = ligne.substring(ligne.indexOf("\t") + 1, ligne.length());
                            ligne = ligne.trim();
                        } else {
                            profilsOfObjects[i] = Double.parseDouble(ligne);
                            ligne = ligne.trim();
                            ligne = "";
                        }
                    }
                }
                ligneNumber++;
            }
            sortedWeightOfObjects = new int[numberOfObjects];
            sortedProfilsOfObjects = new int[numberOfObjects];
            double[] sortedWeight = new double[numberOfObjects];
            double[] sortedProfil = new double[numberOfObjects];
            System.arraycopy(weightOfObjects, 0, sortedWeight, 0, numberOfObjects);
            System.arraycopy(profilsOfObjects, 0, sortedProfil, 0, numberOfObjects);
            quickSort(sortedWeight, 0, sortedWeight.length - 1);

            /*for (int i = 0; i < sortedWeight.length; i++) {
                System.err.print(sortedWeight[i] + "\t");
            }
            System.err.println();*/
            quickSort(sortedProfil, 0, sortedProfil.length - 1);
            for (int i = 0; i < numberOfObjects; i++) {
                sortedWeightOfObjects[i] = findPosition(weightOfObjects, sortedWeight[i]);
                sortedProfilsOfObjects[i] = findPosition(profilsOfObjects, sortedProfil[i]);
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println(getStackTrace(e));
        }
    }

    public int findPosition(double[] arr, double var) {
        int pos = -1, i = 0;
        while (pos == -1 && i < arr.length) {
            if (var == arr[i]) {
                pos = i;
            }
            i++;
        }
        return pos;
    }

    private void quickSort(double[] arr, int low, int high) {
        if (arr == null || arr.length == 0) {
            return;
        }

        if (low >= high) {
            return;
        }

        // pick the pivot
        int middle = low + (high - low) / 2;
        double pivot = arr[middle];
        // make left < pivot and right > pivot
        int i = low, j = high;
        while (i <= j) {
            while (arr[i] < pivot) {
                i++;
            }
            while (arr[j] > pivot) {
                j--;
            }
            if (i <= j) {
                double temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
                i++;
                j--;
            }
        }
        // recursively sort two sub parts
        if (low < j) {
            quickSort(arr, low, j);
        }
        if (high > i) {
            quickSort(arr, i, high);
        }
    }

    public String getInstanceName() {
        return this.instance;
    }

    private String getStackTrace(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }

    public static void main(String[] args) throws ClassNotFoundException, Exception {
        KP kp = new KP("p01", "Int", "Int");
        System.out.println();
        System.out.println();
        System.out.println();
        Solution solutionU = new Solution(kp, "upper");
        Solution solutionL = new Solution(kp, "lower");
        for (int i = 0; i < solutionU.getDecisionVariables().length; i++) {
            System.out.print(solutionU.getDecisionVariable(i).getValue() + "\t");
        }
        System.out.println();
        for (int i = 0; i < solutionL.getDecisionVariables().length; i++) {
            System.out.print(solutionL.getDecisionVariable(i).getValue() + "\t");
        }
        System.out.println();
        System.out.println();
        kp.evaluate(solutionU, solutionL);
        for (int i = 0; i < solutionU.getDecisionVariables().length; i++) {
            System.out.print(solutionU.getDecisionVariable(i).getValue() + "\t");
        }
        System.out.print("\t (" + solutionU.getFitness()+")");
        System.out.println();
        kp.evaluateLowerLevel(solutionU, solutionL);
        for (int i = 0; i < solutionL.getDecisionVariables().length; i++) {
            System.out.print(solutionL.getDecisionVariable(i).getValue() + "\t");
        }
        System.out.print("\t (" + solutionL.getFitness()+")");
        System.out.println();
    }
}
