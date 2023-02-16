package blp;

import blp.util.PseudoRandom;
import java.util.ArrayList;

/**
 *
 * @author marouen
 */
public class TEST {

    public static int sumDiff(int[] pR, int[] mM) {
        int sum = 0;
        for (int i = 0; i < pR.length; i++) {
            sum += Math.abs(pR[i] - mM[i]);
        }
        return sum;
    }

    public static void main(String[] args) {
        ArrayList<int[]> solutions = new ArrayList<>();
        ArrayList<int[]> memes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            int[] solution = new int[5];
            int[] meme = new int[5];
            for (int j = 0; j < solution.length; j++) {
                solution[j] = PseudoRandom.randInt(0, 9);
                meme[j] = PseudoRandom.randInt(0, 9);
            }
            if (i < 3) {
                solutions.add(solution);
            }
            memes.add(meme);
        }
        for (int i = 0; i < solutions.size(); i++) {
            System.out.print((i + 1) + "\t=>\t");
            for (int j = 0; j < solutions.get(i).length; j++) {
                System.out.print(solutions.get(i)[j] + "\t");
            }
            System.out.println();
        }
        System.out.println("####################################################");
        for (int i = 0; i < memes.size(); i++) {
            System.out.print((i + 1) + "\t=>\t");
            for (int j = 0; j < memes.get(i).length; j++) {
                System.out.print(memes.get(i)[j] + "\t");
            }
            System.out.println();
        }
        ArrayList<ArrayList> sums = new ArrayList<>();
        ArrayList<Integer> mins = new ArrayList<>();
        ArrayList<Integer> poss = new ArrayList<>();
        for (int i = 0; i < memes.size(); i++) {
            ArrayList<Integer> sum = new ArrayList<>();
            int min = Integer.MAX_VALUE;
            int pos = 0;
            for (int j = 0; j < solutions.size(); j++) {
                int sumDiff = sumDiff(solutions.get(j), memes.get(i));
                sum.add(sumDiff);
                if (sumDiff < min) {
                    min = sumDiff;
                    pos = j;
                }
            }
            sums.add(sum);
            mins.add(min);
            poss.add(pos);
        }
        System.out.println("====================================================");
        for (int i = 0; i < sums.size(); i++) {
            for (int j = 0; j < sums.get(i).size(); j++) {
                System.out.print(sums.get(i).get(j) + "\t");
            }
            System.out.println();
        }
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++");
        for (int i = 0; i < mins.size(); i++) {
            System.out.print(mins.get(i) + "\t");
        }
        System.out.println();
        System.out.println("****************************************************");
        for (int i = 0; i < poss.size(); i++) {
            System.out.print((poss.get(i) + 1) + "\t");
        }
        System.out.println();
    }

}
