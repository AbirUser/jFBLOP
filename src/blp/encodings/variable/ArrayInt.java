/**
 *
 * @author abir
 */
package blp.encodings.variable;

import blp.core.Problem;
import blp.core.Variable;
import blp.problems.VRP;
import java.util.ArrayList;

/**
 * Class implementing a decision encodings.variable representing an array of
 * integers. The integer values of the array have their own bounds.
 */
public class ArrayInt extends Variable {

    /**
     * Problem using the type
     */
    private Problem problem_;

    /**
     * Stores an array of integer values
     */
    public Variable[] array_;

    /**
     * Stores the length of the array
     */
    private int size_;

    /**
     * Store the lower and upper bounds of each int value of the array in case
     * of having each one different limits
     */
    private int[] lowerBounds_;
    private int[] upperBounds_;

    /**
     * Constructor
     */
    public ArrayInt() {
        lowerBounds_ = null;
        upperBounds_ = null;
        size_ = 0;
        array_ = null;
        problem_ = null;
    } // Constructor

    /**
     * Constructor
     *
     * @param size Size of the array
     */
    public ArrayInt(int size) {
        size_ = size;
        array_ = new Variable[size_];

        lowerBounds_ = new int[size_];
        upperBounds_ = new int[size_];
    } // Constructor

    /**
     * Constructor
     *
     * @param size Size of the array
     * @param level
     * @param problem
     */
    public ArrayInt(int size, String level, Problem problem) {
        problem_ = problem;
        size_ = size;
        array_ = new Variable[size_];
        lowerBounds_ = new int[size_];
        upperBounds_ = new int[size_];
        if (level.equals("lower") || level.equals("upper")) {
            for (int i = 0; i < size_; i++) {
                if (level.equals("upper")) {
                    lowerBounds_[i] = (int) problem_.getUpperLevelLowerLimit(i);
                    upperBounds_[i] = (int) problem_.getUpperLevelUpperLimit(i);
                } else {
                    lowerBounds_[i] = (int) problem_.getLowerLevelLowerLimit(i);
                    upperBounds_[i] = (int) problem_.getLowerLevelUpperLimit(i);
                }
                array_[i] = new Int(lowerBounds_[i], upperBounds_[i]);
            }
        }
    } // Constructor

    /**
     * Constructor
     *
     * @param size The size of the array
     * @param lowerBounds Lower bounds
     * @param upperBounds Upper bounds
     */
    public ArrayInt(int size, double[] lowerBounds, double[] upperBounds) {
        size_ = size;
        array_ = new Variable[size_];

        lowerBounds_ = new int[size_];
        upperBounds_ = new int[size_];

        for (int i = 0; i < size_; i++) {
            lowerBounds_[i] = (int) lowerBounds[i];
            upperBounds_[i] = (int) upperBounds[i];
            array_[i] = new Int(lowerBounds_[i], upperBounds_[i]);
        } // for
    } // Constructor

    /**
     * Copy Constructor
     *
     * @param arrayInt The arrayInt to copy
     */
    private ArrayInt(ArrayInt arrayInt) {
        size_ = arrayInt.size_;
        array_ = new Variable[size_];

        lowerBounds_ = new int[size_];
        upperBounds_ = new int[size_];

        /*System.arraycopy(arrayInt.array_, 0, array_, 0, arrayInt.array_.length );
        System.arraycopy(arrayInt.lowerBounds_, 0, lowerBounds_, 0, arrayInt.lowerBounds_.length );
        System.arraycopy(arrayInt.upperBounds_, 0, upperBounds_, 0, arrayInt.upperBounds_.length );*/
        for (int i = 0; i < size_; i++) {
            array_[i] = arrayInt.array_[i].copy();
            lowerBounds_[i] = arrayInt.lowerBounds_[i];
            upperBounds_[i] = arrayInt.upperBounds_[i];
        }// for
    } // Copy Constructor

    /**
     * Returns the length of the arrayInt.
     *
     * @return The length
     */
    @Override
    public int getLength() {
        return size_;
    } // getLength

    @Override
    public Variable getVariable(int index) throws Exception {
        if ((index >= 0) && (index < size_)) {
            return array_[index];
        } else {
            throw new Exception(blp.encodings.variable.ArrayInt.class + ".getVariable: index value (" + index + ") invalid");
        } // if
    }

    /**
     * getValue
     *
     * @param index Index of value to be returned
     * @return the value in position index
     * @throws java.lang.Exception
     */
    @Override
    public Object getValue(int index) throws Exception {
        if ((index >= 0) && (index < size_)) {
            return array_[index].getValue();
        } else {
            throw new Exception(blp.encodings.variable.ArrayInt.class + ".getValue: index value (" + index + ") invalid");
        } // if
    } // getValue

    /**
     * setValue
     *
     * @param index Index of value to be returned
     * @param value The value to be set in position index
     * @throws java.lang.Exception
     */
    @Override
    public void setValue(Object value, int index) throws Exception {
        if ((index >= 0) && (index < size_)) {
            array_[index].setValue(value);
        } else {
            throw new Exception(blp.encodings.variable.ArrayInt.class + ".setValue: index value (" + index + ") invalid");
        } // else
    } // setValue

    @Override
    public Variable copy() {
        return new ArrayInt(this);
    } // copy

    /**
     * Get the lower bound of a value
     *
     * @param index The index of the value
     * @return the lower bound
     * @throws java.lang.Exception
     */
    @Override
    public Object getLowerBound(int index) throws Exception {
        if ((index >= 0) && (index < size_)) {
            return lowerBounds_[index];
        } else {
            throw new Exception(blp.encodings.variable.ArrayInt.class + ".getLowerBound: index value (" + index + ") invalid");
        } // else	
    } // getLowerBound

    /**
     * Get the upper bound of a value
     *
     * @param index The index of the value
     * @return the upper bound
     * @throws java.lang.Exception
     */
    @Override
    public Object getUpperBound(int index) throws Exception {
        if ((index >= 0) && (index < size_)) {
            return upperBounds_[index];
        } else {
            throw new Exception(blp.encodings.variable.ArrayInt.class + ".getUpperBound: index value (" + index + ") invalid");
        } // else
    } // getLowerBound

    /**
     * Get the lower bound of a value
     *
     * @param index The index of the value
     * @return the lower bound
     * @throws java.lang.Exception
     */
    @Override
    public void setLowerBound(Object value, int index) throws Exception {
        if ((index >= 0) && (index < size_)) {
            lowerBounds_[index] = (int) value;
            array_[index].setLowerBound(value);
        } else {
            throw new Exception(blp.encodings.variable.ArrayInt.class + ".setLowerBound: index value (" + index + ") invalid");
        } // else	
    } // getLowerBound

    /**
     * Get the upper bound of a value
     *
     * @param index The index of the value
     * @return the upper bound
     * @throws java.lang.Exception
     */
    @Override
    public void setUpperBound(Object value, int index) throws Exception {
        if ((index >= 0) && (index < size_)) {
            upperBounds_[index] = (int) value;
            array_[index].setUpperBound(value);
        } else {
            throw new Exception(blp.encodings.variable.ArrayInt.class + ".setUpperBound: index value (" + index + ") invalid");
        } // else
    } // getLowerBound

    @Override
    public Variable closet(double spacing, int index) throws Exception {
        if ((index >= 0) && (index < size_)) {
            array_[index] = array_[index].closet(spacing);
            return array_[index];
        } else {
            throw new Exception(blp.encodings.variable.ArrayInt.class + ".closet: index value (" + index + ") invalid");
        }
    }

    @Override
    public ArrayList<ArrayList<Integer>> getPostionsOf(ArrayList<Variable> variables) throws Exception {
        ArrayList<ArrayList<Integer>> positions = new ArrayList<>();
        for (Variable variable : variables) {
            positions.add(allIndexOf(variable));
        }
        return positions;
    }

    @Override
    public ArrayList<Variable> getNotExistingVariablesInDomain(ArrayList<Variable> variables) throws Exception {
        ArrayList<Variable> notExisting = new ArrayList<>();
        for (int i = 0; i < variables.size(); i++) {
            Variable var = variables.get(i);
            int index = indexOf(var);
            if (index == -1) {
                notExisting.add(var);
            }
        }
        return notExisting;
    }

    @Override
    public ArrayList<Integer> getDuplicateVariablesIndex() throws Exception {
        ArrayList<Integer> duplicated = new ArrayList<>();
        for (Variable var : array_) {
            ArrayList<Integer> allIndex = allIndexOf(var);
            if (!allIndex.isEmpty() && allIndex.size() > 1) {
                for (int j = 1; j < allIndex.size(); j++) {
                    if (duplicated.indexOf(allIndex.get(j)) == -1) {
                        duplicated.add(allIndex.get(j));
                    }
                }
            }
        }
        return duplicated;
    }

    @Override
    public ArrayList<Integer> getDuplicateLowerBoundsIndex(int index) throws Exception {
        if ((index >= 0) && (index < size_)) {
            ArrayList<Integer> duplicated = new ArrayList<>();
            Variable var = new Int(lowerBounds_[index], lowerBounds_[index], upperBounds_[index]);
            ArrayList<Integer> allIndex = allIndexOf(var);
            if (!allIndex.isEmpty() && allIndex.size() > 1) {
                for (int j = 1; j < allIndex.size(); j++) {
                    if (duplicated.indexOf(allIndex.get(j)) == -1) {
                        duplicated.add(allIndex.get(j));
                    }
                }
            }
            return duplicated;
        } else {
            throw new Exception(blp.encodings.variable.ArrayInt.class + ".getDuplicateLowerBoundsIndex: index value (" + index + ") invalid");
        }
    }

    @Override
    public ArrayList<Integer> getDuplicateUpperBoundsIndex(int index) throws Exception {
        if ((index >= 0) && (index < size_)) {
            ArrayList<Integer> duplicated = new ArrayList<>();
            Variable var = new Int(upperBounds_[index], lowerBounds_[index], upperBounds_[index]);
            ArrayList<Integer> allIndex = allIndexOf(var);
            if (!allIndex.isEmpty() && allIndex.size() > 1) {
                for (int j = 1; j < allIndex.size(); j++) {
                    if (duplicated.indexOf(allIndex.get(j)) == -1) {
                        duplicated.add(allIndex.get(j));
                    }
                }
            }
            return duplicated;
        } else {
            throw new Exception(blp.encodings.variable.ArrayInt.class + ".getDuplicateLowerBoundsIndex: index value (" + index + ") invalid");
        }
    }

    @Override
    public ArrayList<Integer> getLowerBoundsIndex(int index) throws Exception {
        if ((index >= 0) && (index < size_)) {
            ArrayList<Integer> duplicated = new ArrayList<>();
            Variable var = new Int(lowerBounds_[index], lowerBounds_[index], upperBounds_[index]);
            ArrayList<Integer> allIndex = allIndexOf(var);
            if (!allIndex.isEmpty()) {
                for (int j = 0; j < allIndex.size(); j++) {
                    if (duplicated.indexOf(allIndex.get(j)) == -1) {
                        duplicated.add(allIndex.get(j));
                    }
                }
            }
            return duplicated;
        } else {
            throw new Exception(blp.encodings.variable.ArrayInt.class + ".getLowerBoundsIndex: index value (" + index + ") invalid");
        }
    }

    @Override
    public ArrayList<Integer> getUpperBoundsIndex(int index) throws Exception {
        if ((index >= 0) && (index < size_)) {
            ArrayList<Integer> duplicated = new ArrayList<>();
            Variable var = new Int(upperBounds_[index], lowerBounds_[index], upperBounds_[index]);
            ArrayList<Integer> allIndex = allIndexOf(var);
            if (!allIndex.isEmpty()) {
                for (int j = 0; j < allIndex.size(); j++) {
                    if (duplicated.indexOf(allIndex.get(j)) == -1) {
                        duplicated.add(allIndex.get(j));
                    }
                }
            }
            return duplicated;
        } else {
            throw new Exception(blp.encodings.variable.ArrayInt.class + ".getUpperBoundsIndex: index value (" + index + ") invalid");
        }
    }

    private int indexOf(Variable var) throws Exception {
        int index = -1;
        int j = 0;
        while (index == -1 && j < array_.length) {
            if (array_[j].compare(var.getValue()) == 0) {
                index = j;
            }
            j++;
        }
        return index;
    }

    private ArrayList<Integer> allIndexOf(Variable var) throws Exception {
        ArrayList<Integer> index = new ArrayList<>();
        for (int i = 0; i < array_.length; i++) {
            /*if (array_[i].compare(var.getValue()) == 0) {
                index.add(i);
            }*/
            int x = (Integer) array_[i].getValue();
            int y = (Integer) var.getValue();
            if (x == y) {
                index.add(i);
            }
        }
        return index;
    }

    /**
     * Returns a generated value randomly
     *
     * @param index
     * @return The string
     */
    @Override
    public Object generateValue(int index) throws Exception {
        if ((index >= 0) && (index < size_)) {
            return array_[index].generateValue(index);
        } else {
            throw new Exception(blp.encodings.variable.ArrayInt.class + ".generateValue: index value (" + index + ") invalid");
        }
    } // generateValue

    @Override
    public ArrayList getRanges(int p) throws Exception {
        if ((p >= 0) && (p < size_)) {
            return array_[p].getRanges(p);
        } else {
            throw new Exception(blp.encodings.variable.ArrayInt.class + ".getRanges: index value (" + p + ") invalid");
        }
    } // getRanges

    @Override
    public double calculateSpacing(int index, int p) throws Exception {
        if ((index >= 0) && (index < size_)) {
            return array_[p].calculateSpacing(p);
        } else {
            throw new Exception(blp.encodings.variable.ArrayInt.class + ".calculateSpacing: index value (" + index + ") invalid");
        }
    }
    
    @Override
    public double AbsDiff(Variable var) throws Exception {
        double absDiff = 0.0;
        for (int i = 0; i < array_.length; i++) {
            absDiff += Math.abs((int) array_[i].getValue() - (int) var.getValue(i));
        }
        return absDiff;
    }

    @Override
    public boolean isLowerUpperBound(int index) throws Exception {
        if ((index >= 0) && (index < size_)) {
            return array_[index].isLowerUpperBound();
        } else {
            throw new Exception(blp.encodings.variable.ArrayInt.class + ".isLowerUpperBound: index value (" + index + ") invalid");
        }
    }

    /**
     * Returns a string representing the object
     *
     * @return The string
     */
    @Override
    public int compare(Object value, int index) throws Exception {
        if ((index >= 0) && (index < size_)) {
            return array_[index].compare(value);
        } else {
            throw new Exception(blp.encodings.variable.ArrayInt.class + ".compare: index value (" + index + ") invalid");
        }
    } // toString

    /**
     * Returns a string representing the object
     *
     * @return The string
     */
    @Override
    public String toString() {
        String string;

        string = "";
        for (int i = 0; i < size_; i++) {
            string += array_[i] + " ";
        }

        return string;
    } // toString  
} // ArrayInt
