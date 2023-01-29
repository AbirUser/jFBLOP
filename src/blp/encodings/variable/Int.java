/**
 *
 * @author abir
 */
package blp.encodings.variable;

import blp.core.Variable;
import blp.util.PseudoRandom;
import java.util.ArrayList;
import java.util.Random;

/**
 * This class implements an integer decision encodings.variable
 */
public class Int extends Variable {

    private Integer value_;       //Stores the value of the encodings.variable
    private Integer lowerBound_;  //Stores the lower limit of the encodings.variable
    private Integer upperBound_;  //Stores the upper limit of the encodings.variable

    /**
     * Constructor
     */
    public Int() {
        lowerBound_ = java.lang.Integer.MIN_VALUE;
        upperBound_ = java.lang.Integer.MAX_VALUE;
        value_ = 0;
    } // Int

    /**
     * Constructor
     *
     * @param lowerBound Variable lower bound
     * @param upperBound Variable upper bound
     */
    public Int(int lowerBound, int upperBound) {
        lowerBound_ = lowerBound;
        upperBound_ = upperBound;
        value_ = PseudoRandom.randInt(lowerBound, upperBound);
    } // Int

    /**
     * Constructor
     *
     * @param value Value of the encodings.variable
     * @param lowerBound Variable lower bound
     * @param upperBound Variable upper bound
     */
    public Int(int value, int lowerBound, int upperBound) {
        super();
        value_ = value;
        lowerBound_ = lowerBound;
        upperBound_ = upperBound;
    } // Int

    /**
     * Copy constructor.
     *
     * @param variable Variable to be copied.
     * @throws java.lang.Exception
     */
    public Int(Variable variable) throws Exception {
        lowerBound_ = (Integer) variable.getLowerBound();
        upperBound_ = (Integer) variable.getUpperBound();
        value_ = (Integer) variable.getValue();
    } // Int

    /**
     * Returns the value of the encodings.variable.
     *
     * @return the value.
     */
    @Override
    public Object getValue() {
        return value_;
    } // getValue

    /**
     * Assigns a value to the encodings.variable.
     *
     * @param value The value.
     */
    @Override
    public void setValue(Object value) {
        value_ = (int) value;
    } // setValue

    @Override
    public ArrayList<Variable> getAllVariablesInDomain() {
        ArrayList<Variable> allVariable = new ArrayList<>();
        for (int i = lowerBound_; i <= upperBound_; i++) {
            allVariable.add(new Int(i, lowerBound_, upperBound_));
        }
        return allVariable;
    }

    @Override
    public ArrayList<Variable> getAllVariablesInDomainExcludeBounds() {
        ArrayList<Variable> allVariable = new ArrayList<>();
        for (int i = lowerBound_ + 1; i < upperBound_; i++) {
            allVariable.add(new Int(i, lowerBound_, upperBound_));
        }
        return allVariable;
    }

    @Override
    public ArrayList<Variable> getAllVariablesInDomainExcludeLowerBounds() {
        ArrayList<Variable> allVariable = new ArrayList<>();
        for (int i = lowerBound_ + 1; i <= upperBound_; i++) {
            allVariable.add(new Int(i, lowerBound_, upperBound_));
        }
        return allVariable;
    }

    @Override
    public ArrayList<Variable> getAllVariablesInDomainExcludeUpperBounds() {
        ArrayList<Variable> allVariable = new ArrayList<>();
        for (int i = lowerBound_; i < upperBound_; i++) {
            allVariable.add(new Int(i, lowerBound_, upperBound_));
        }
        return allVariable;
    }

    /**
     * Creates an exact copy of the <code>Int</code> object.
     *
     * @return the copy.
     */
    @Override
    public Variable copy() {
        try {
            return new Int(this);
        } catch (Exception e) {
            System.err.println("Int.copy.execute: JMException");
            return null;
        }
    } // copy

    /**
     * Returns the lower bound of the encodings.variable.
     *
     * @return the lower bound.
     */
    @Override
    public Object getLowerBound() {
        return lowerBound_;
    } // getLowerBound

    /**
     * Returns the upper bound of the encodings.variable.
     *
     * @return the upper bound.
     */
    @Override
    public Object getUpperBound() {
        return upperBound_;
    } // getUpperBound

    /**
     * Sets the lower bound of the encodings.variable.
     *
     * @param lowerBound The lower bound value.
     */
    @Override
    public void setLowerBound(Object lowerBound) {
        lowerBound_ = (int) lowerBound;
    } // setLowerBound

    /**
     * Sets the upper bound of the encodings.variable.
     *
     * @param upperBound The new upper bound value.
     */
    @Override
    public void setUpperBound(Object upperBound) {
        upperBound_ = (int) upperBound;
    } // setUpperBound

    /**
     * Returns a generated value randomly
     *
     * @param index
     * @return The string
     */
    @Override
    public Object generateValue(int index) {
        Integer newValue = value_;
        if (lowerBound_ < upperBound_) {
            while (newValue == value_) {
                newValue = PseudoRandom.randInt(lowerBound_, upperBound_);
            }
        } else {
            newValue = PseudoRandom.randInt(lowerBound_, upperBound_);
        }
        return newValue;
    } // generateValue  

    @Override
    public ArrayList getRanges(int p) throws Exception {
        ArrayList result = new ArrayList();
        for (int i = lowerBound_; i <= upperBound_; i++) {
            result.add(i);
        }
        return result;
    } // getRanges

    @Override
    public double calculateSpacing(int p) {
        return (double) upperBound_ / p;
    }

    @Override
    public double AbsDiff(Variable var) throws Exception {
        return (double) Math.abs(value_ - (int) var.getValue());
    }

    @Override
    public boolean isLowerUpperBound() {
        return value_ < upperBound_;
    }

    @Override
    public Variable closet(double spacing) throws Exception {
        Variable var = copy();
        double get = (int) var.getValue() + spacing;
        ArrayList<Integer> arrayArray = new ArrayList<>();
        for (int i = lowerBound_; i <= upperBound_; i++) {
            arrayArray.add(i);
        }
        int[] intArray = new int[arrayArray.size()];
        for (int i = 0; i < intArray.length; i++) {
            intArray[i] = arrayArray.get(i);
        }
        int rank = rank(get, intArray);
        if (rank > intArray.length - 1) {
            var.setValue(intArray[intArray.length - 1]);
            return var;
        } else if (rank == 0) {
            var.setValue(intArray[0]);
            return var;
        } else if (get == intArray[rank]) {
            var.setValue(intArray[rank]);
            return var;
        } else {
            double diff1 = Math.abs(intArray[rank] - get);
            double diff2 = Math.abs(intArray[rank - 1] - get);
            if (diff1 < diff2) {
                var.setValue(intArray[rank]);
                return var;
            } else if (diff1 > diff2) {
                var.setValue(intArray[rank - 1]);
                return var;
            } else {
                if (new Random().nextBoolean()) {
                    var.setValue(intArray[rank]);
                } else {
                    var.setValue(intArray[rank - 1]);
                }
                return var;
            }
        }
    }

    public int rank(double key, int[] a) {
        int lo = 0;
        int hi = a.length - 1;
        int mid = lo + (hi - lo) / 2;
        while (lo <= hi) {
            // Key is in a[lo..hi] or not present.
            if (key < a[mid]) {
                hi = mid - 1;
            } else if (key > a[mid]) {
                lo = mid + 1;
            } else {
                return mid;
            }
            mid = lo + (hi - lo) / 2;
        }
        return mid;
    }

    /**
     * Returns a string representing the object
     *
     * @return The string
     */
    @Override
    public int compare(Object value) {
        if (value_ == (Integer) value) {
            return 0;
        } else if (value_ < (Integer) value) {
            return -1;
        } else {
            return 1;
        }
    } // toString

    /**
     * Returns a string representing the object
     *
     * @return The string
     */
    @Override
    public String toString() {
        return value_ + "";
    } // toString
} // Int
