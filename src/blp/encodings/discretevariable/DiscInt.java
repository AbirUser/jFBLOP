package blp.encodings.discretevariable;

import blp.core.Variable;
import blp.util.PseudoRandom;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Abir
 */
/**
 * This class implements an integer decision encodings. discrete variable
 */
public class DiscInt extends Variable {

    private int value_;      //Stores the value of the encodings.variable
    private int[] domain_;  //Stores the domain of the desicon variable

    /**
     * Constructor
     */
    public DiscInt() {
        domain_ = new int[0];
        value_ = 0;
    }

    /**
     * Constructor
     *
     * @param domain
     */
    public DiscInt(int[] domain) {
        System.arraycopy(domain, 0, domain_, 0, domain.length);
        value_ = domain_[PseudoRandom.randInt(0, domain_.length - 1)];
    }

    /**
     * Constructor
     *
     * @param value Value of the encodings.variable
     * @param domain
     */
    public DiscInt(int value, int[] domain) {
        super();

        value_ = value;
        System.arraycopy(domain, 0, domain_, 0, domain.length);
    }
    
    /**
     * Copy constructor.
     *
     * @param variable Variable to be copied.
     * @throws java.lang.Exception
     */
    public DiscInt(Variable variable) throws Exception {
        System.arraycopy(variable.getDomain(), 0, domain_, 0, variable.getDomain().length);
        value_ = (int) variable.getValue();
    }

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
    }

    /**
     * Returns the value of the encodings.variable.
     *
     * @return the value.
     */
    @Override
    public Integer[] getDomain() {
        Integer[] domain = new Integer[domain_.length];
        System.arraycopy(domain_, 0, domain, 0, domain_.length);
        return domain;
    } // getValue

    /**
     * Assigns a value to the encodings.variable.
     *
     * @param domain
     */
    public void setDomain(int[] domain) {
        System.arraycopy(domain, 0, domain_, 0, domain.length);
    }
    
    /**
     * Creates an exact copy of the <code>Int</code> object.
     *
     * @return the copy.
     */
    @Override
    public Variable copy() {
        try {
            return new DiscInt(this);
        } catch (Exception e) {
            System.err.println("DiscInt.copy.execute: Exception");
            return null;
        }
    }
    
    /**
     * Returns a range of each decision variable
     *
     * @param p
     * @return The ArrayList
     */
    @Override
    public ArrayList getRanges(int p) {
        int maxi = (Integer) domain_[domain_.length - 1];
        int mini = (Integer) domain_[0];
        double spacing = (maxi / p);
        ArrayList<Integer> range = new ArrayList();
        range.add(mini);
        while (range.get(range.size() - 1) < maxi) {
            int r = closest(range.get(range.size() - 1) + spacing);
            if (range.indexOf(r) == -1) {
                range.add(r);
            }
        }
        return range;
    }
    
    private int closest(double get) {
        int rank = PositionClosest(get, domain_);
        if (rank > domain_.length - 1) {
            return domain_[domain_.length - 1];
        } else if (rank == 0) {
            return domain_[0];
        } else if (get == domain_[rank]) {// 2 5 8 11    7
            return domain_[rank];
        } else {
            double diff1 = Math.abs(domain_[rank] - get);
            double diff2 = Math.abs(domain_[rank - 1] - get);
            if (diff1 < diff2) {
                return domain_[rank];
            } else if (diff1 > diff2) {
                return domain_[rank - 1];
            } else {
                return (new Random().nextBoolean()) ? domain_[rank] : domain_[rank - 1];
            }
        }
    }

    public int PositionClosest(double key, int[] a) {// 2 5 8 11 
        int lo = 0;// 0
        int hi = a.length - 1;// 3
        int mid = lo + (hi - lo) / 2;// 3 / 2 = 1
        while (lo <= hi) {
            // Key is in a[lo..hi] or not present.
            if (key < a[mid]) {// 5 ? 7
                hi = mid - 1;
            } else if (key > a[mid]) {
                lo = mid + 1;//2
            } else {
                return mid;
            }
            mid = lo + (hi - lo) / 2;// 2+1= 3/2 = 1
        }
        return mid;
    }
    
    /**
     * Returns a string representing the object
     *
     * @return The string
     */
    @Override
    public String toString() {
        return value_ + "";
    }
}
