package blp.core;

/**
 *
 * @author abir
 */
import java.io.Serializable;
import java.util.ArrayList;

/**
 * This abstract class is the base for defining new types of variables. Many
 * methods of <code>Variable</code> (<code>getValue</code>,  <code>setValue</code>,<code>
 * getLowerLimit</code>,<code>setLowerLimit</code>,<code>getUpperLimit</code>,
 * <code>setUpperLimit</code>) are not applicable to all the subclasses of
 * <code>Variable</code>. For this reason, they are defined by default as giving
 * a fatal error.
 */
public abstract class Variable implements Serializable {

    /**
     * Creates an exact copy of a <code>Variable</code> object.
     *
     * @return the copy of the object.
     */
    public abstract Variable copy();

    /**
     * Gets the double value representating the encodings.variable. It is used
     * in subclasses of <code>Variable</code> (i.e. <code>Real</code> and
     * <code>Int</code>). As not all objects belonging to a subclass of
     * <code>Variable</code> have a double value, a call to this method it is
     * considered a fatal error by default, and the program is terminated. Those
     * classes requiring this method must redefine it.
     * @return 
     */
    public Object generateValue(int index) throws Exception{
        Class cls = java.lang.String.class;
        String name = cls.getName();
        throw new Exception("Exception in " + name + ".getValue() => Class" + name + " does not implement method getValue()");
    } // getValue
    
    /**
     * Gets the double value representating the encodings.variable. It is used
     * in subclasses of <code>Variable</code> (i.e. <code>Real</code> and
     * <code>Int</code>). As not all objects belonging to a subclass of
     * <code>Variable</code> have a double value, a call to this method it is
     * considered a fatal error by default, and the program is terminated. Those
     * classes requiring this method must redefine it.
     * @return 
     */
    public int getLength() throws Exception{
        Class cls = java.lang.String.class;
        String name = cls.getName();
        throw new Exception("Exception in " + name + ".getValue() => Class" + name + " does not implement method getValue()");
    } // getLength
    
    /**
     * Gets the double value representating the encodings.variable. It is used
     * in subclasses of <code>Variable</code> (i.e. <code>Real</code> and
     * <code>Int</code>). As not all objects belonging to a subclass of
     * <code>Variable</code> have a double value, a call to this method it is
     * considered a fatal error by default, and the program is terminated. Those
     * classes requiring this method must redefine it.
     * @return 
     */
    public Object getValue() throws Exception{
        Class cls = java.lang.String.class;
        String name = cls.getName();
        throw new Exception("Exception in " + name + ".getValue() => Class" + name + " does not implement method getValue()");
    } // getValue
    
    /**
     * Gets the double value representating the encodings.variable. It is used
     * in subclasses of <code>Variable</code> (i.e. <code>Real</code> and
     * <code>Int</code>). As not all objects belonging to a subclass of
     * <code>Variable</code> have a double value, a call to this method it is
     * considered a fatal error by default, and the program is terminated. Those
     * classes requiring this method must redefine it.
     * @return 
     */
    public Object getValue(int i) throws Exception{
        Class cls = java.lang.String.class;
        String name = cls.getName();
        throw new Exception("Exception in " + name + ".getValue() => Class" + name + " does not implement method getValue()");
    } // getValue
    
    /**
     * Gets the double value representating the encodings.variable. It is used
     * in subclasses of <code>Variable</code> (i.e. <code>Real</code> and
     * <code>Int</code>). As not all objects belonging to a subclass of
     * <code>Variable</code> have a double value, a call to this method it is
     * considered a fatal error by default, and the program is terminated. Those
     * classes requiring this method must redefine it.
     * @return 
     */
    public Variable getVariable(int i) throws Exception{
        Class cls = java.lang.String.class;
        String name = cls.getName();
        throw new Exception("Exception in " + name + ".getVariable() => Class" + name + " does not implement method getValue()");
    } // getVariable

    /**
     * Sets a double value to a encodings.variable in subclasses of
     * <code>Variable</code>. As not all objects belonging to a subclass of
     * <code>Variable</code> have a double value, a call to this method it is
     * considered a fatal error by default, and the program is terminated. Those
     * classes requiring this method must redefine it.
     * @param value
     * @throws java.lang.Exception
     */
    public void setValue(Object value) throws Exception{
        Class cls = java.lang.String.class;
        String name = cls.getName();
        throw new Exception("Exception in " + name + ".setValue() => Class" + name + " does not implement method setValue()");
    } // setValue
    
    /**
     * Sets a double value to a encodings.variable in subclasses of
     * <code>Variable</code>. As not all objects belonging to a subclass of
     * <code>Variable</code> have a double value, a call to this method it is
     * considered a fatal error by default, and the program is terminated. Those
     * classes requiring this method must redefine it.
     * @param value
     * @throws java.lang.Exception
     */
    public void setValue(Object value, int i) throws Exception{
        Class cls = java.lang.String.class;
        String name = cls.getName();
        throw new Exception("Exception in " + name + ".setValue() => Class" + name + " does not implement method setValue()");
    } // setValue
    
    /**
     * Sets a double value to a encodings.variable in subclasses of
     * <code>Variable</code>. As not all objects belonging to a subclass of
     * <code>Variable</code> have a double value, a call to this method it is
     * considered a fatal error by default, and the program is terminated. Those
     * classes requiring this method must redefine it.
     * @param value
     * @throws java.lang.Exception
     */
    public int compare(Object value) throws Exception{
        Class cls = java.lang.String.class;
        String name = cls.getName();
        throw new Exception("Exception in " + name + ".setValue() => Class" + name + " does not implement method setValue()");
    } // setValue
    
    /**
     * Sets a double value to a encodings.variable in subclasses of
     * <code>Variable</code>. As not all objects belonging to a subclass of
     * <code>Variable</code> have a double value, a call to this method it is
     * considered a fatal error by default, and the program is terminated. Those
     * classes requiring this method must redefine it.
     * @param value
     * @throws java.lang.Exception
     */
    public int compare(Object value, int index) throws Exception{
        Class cls = java.lang.String.class;
        String name = cls.getName();
        throw new Exception("Exception in " + name + ".setValue() => Class" + name + " does not implement method setValue()");
    } // setValue

    /**
     * Gets the lower bound value of a encodings.variable. As not all objects
     * belonging to a subclass of <code>Variable</code> have a lower bound, a
     * call to this method is considered a fatal error by default, and the
     * program is terminated. Those classes requiring this method must redefine
     * it.
     * @return 
     * @throws java.lang.Exception
     */
    public Object getLowerBound() throws Exception{
        Class cls = java.lang.String.class;
        String name = cls.getName();
        throw new Exception("Exception in " + name + ".getLowerBound() => Class" + name + " does not implement method getLowerBound()");
    } // getLowerBound
    
    /**
     * Gets the lower bound value of a encodings.variable. As not all objects
     * belonging to a subclass of <code>Variable</code> have a lower bound, a
     * call to this method is considered a fatal error by default, and the
     * program is terminated. Those classes requiring this method must redefine
     * it.
     * @return 
     * @throws java.lang.Exception
     */
    public Object getLowerBound(int index) throws Exception{
        Class cls = java.lang.String.class;
        String name = cls.getName();
        throw new Exception("Exception in " + name + ".getLowerBound() => Class" + name + " does not implement method getLowerBound()");
    } // getLowerBound

    /**
     * Gets the upper bound value of a encodings.variable. As not all objects
     * belonging to a subclass of <code>Variable</code> have an upper bound, a
     * call to this method is considered a fatal error by default, and the
     * program is terminated. Those classes requiring this method must redefine
     * it.
     * @return 
     * @throws java.lang.Exception
     */
    public Object getUpperBound() throws Exception{
        Class cls = java.lang.String.class;
        String name = cls.getName();
        throw new Exception("Exception in " + name + ".getUpperBound() => Class" + name + " does not implement method getUpperBound()");
    } // getUpperBound
    
    /**
     * Gets the upper bound value of a encodings.variable. As not all objects
     * belonging to a subclass of <code>Variable</code> have an upper bound, a
     * call to this method is considered a fatal error by default, and the
     * program is terminated. Those classes requiring this method must redefine
     * it.
     * @return 
     * @throws java.lang.Exception
     */
    public Object getUpperBound(int index) throws Exception{
        Class cls = java.lang.String.class;
        String name = cls.getName();
        throw new Exception("Exception in " + name + ".getUpperBound() => Class" + name + " does not implement method getUpperBound()");
    } // getUpperBound
    
    /**
     * Gets the domain of a encodings.variable. As not all objects
     * belonging to a subclass of <code>Variable</code> have an upper bound, a
     * call to this method is considered a fatal error by default, and the
     * program is terminated. Those classes requiring this method must redefine
     * it.
     * @return 
     * @throws java.lang.Exception
     */
    public Object[] getDomain() throws Exception{
        Class cls = java.lang.String.class;
        String name = cls.getName();
        throw new Exception("Exception in " + name + ".getDomain() => Class" + name + " does not implement method getDomain()");
    } // getUpperBound

    /**
     * Sets the lower bound for a encodings.variable. As not all objects
     * belonging to a subclass of <code>Variable</code> have a lower bound, a
     * call to this method is considered a fatal error by default and the
     * program is terminated. Those classes requiring this method must to
     * redefine it.
     *
     * @param lowerBound
     * @throws java.lang.Exception
     */
    public void setLowerBound(Object lowerBound) throws Exception {
        Class cls = java.lang.String.class;
        String name = cls.getName();
        throw new Exception("Exception in " + name + ".setLowerBound() => Class" + name + " does not implement method setLowerBound()");
    } // setLowerBound
    
    /**
     * Sets the lower bound for a encodings.variable. As not all objects
     * belonging to a subclass of <code>Variable</code> have a lower bound, a
     * call to this method is considered a fatal error by default and the
     * program is terminated. Those classes requiring this method must to
     * redefine it.
     *
     * @param lowerBound
     * @throws java.lang.Exception
     */
    public void setLowerBound(Object lowerBound, int index) throws Exception {
        Class cls = java.lang.String.class;
        String name = cls.getName();
        throw new Exception("Exception in " + name + ".setLowerBound() => Class" + name + " does not implement method setLowerBound()");
    } // setLowerBound

    /**
     * Sets the upper bound for a encodings.variable. As not all objects
     * belonging to a subclass of <code>Variable</code> have an upper bound, a
     * call to this method is considered a fatal error by default, and the
     * program is terminated. Those classes requiring this method must redefine
     * it.
     *
     * @param upperBound
     * @throws java.lang.Exception
     */
    public void setUpperBound(Object upperBound) throws Exception {
        Class cls = java.lang.String.class;
        String name = cls.getName();
        throw new Exception("Exception in " + name + ".setUpperBound() => Class" + name + " does not implement method setUpperBound()");
    } // setUpperBound
    
    /**
     * Sets the upper bound for a encodings.variable. As not all objects
     * belonging to a subclass of <code>Variable</code> have an upper bound, a
     * call to this method is considered a fatal error by default, and the
     * program is terminated. Those classes requiring this method must redefine
     * it.
     *
     * @param upperBound
     * @throws java.lang.Exception
     */
    public void setUpperBound(Object upperBound, int index) throws Exception {
        Class cls = java.lang.String.class;
        String name = cls.getName();
        throw new Exception("Exception in " + name + ".setUpperBound() => Class" + name + " does not implement method setUpperBound()");
    } // setUpperBound
    
    /**
     * Gets the double value representating the encodings.variable. It is used
     * in subclasses of <code>Variable</code> (i.e. <code>Real</code> and
     * <code>Int</code>). As not all objects belonging to a subclass of
     * <code>Variable</code> have a double value, a call to this method it is
     * considered a fatal error by default, and the program is terminated. Those
     * classes requiring this method must redefine it.
     * @return 
     */
    public ArrayList getRanges(int p) throws Exception{
        Class cls = java.lang.String.class;
        String name = cls.getName();
        throw new Exception("Exception in " + name + ".getRanges() => Class" + name + " does not implement method getRanges()");
    } // getValue
    
    /**
     * Gets the double value representating the encodings.variable. It is used
     * in subclasses of <code>Variable</code> (i.e. <code>Real</code> and
     * <code>Int</code>). As not all objects belonging to a subclass of
     * <code>Variable</code> have a double value, a call to this method it is
     * considered a fatal error by default, and the program is terminated. Those
     * classes requiring this method must redefine it.
     * @return 
     */
    public Variable closet(double spacing) throws Exception{
        Class cls = java.lang.String.class;
        String name = cls.getName();
        throw new Exception("Exception in " + name + ".closet() => Class" + name + " does not implement method closet()");
    } // closet
    
    /**
     * Gets the double value representating the encodings.variable. It is used
     * in subclasses of <code>Variable</code> (i.e. <code>Real</code> and
     * <code>Int</code>). As not all objects belonging to a subclass of
     * <code>Variable</code> have a double value, a call to this method it is
     * considered a fatal error by default, and the program is terminated. Those
     * classes requiring this method must redefine it.
     * @return 
     */
    public Variable closet(double spacing, int index) throws Exception{
        Class cls = java.lang.String.class;
        String name = cls.getName();
        throw new Exception("Exception in " + name + ".closet() => Class" + name + " does not implement method closet()");
    } // closet
    
    /**
     * Gets the double value representating the encodings.variable. It is used
     * in subclasses of <code>Variable</code> (i.e. <code>Real</code> and
     * <code>Int</code>). As not all objects belonging to a subclass of
     * <code>Variable</code> have a double value, a call to this method it is
     * considered a fatal error by default, and the program is terminated. Those
     * classes requiring this method must redefine it.
     * @return 
     */
    public Variable closet(int index, double spacing) throws Exception{
        Class cls = java.lang.String.class;
        String name = cls.getName();
        throw new Exception("Exception in " + name + ".closet() => Class" + name + " does not implement method closet()");
    } // getValue

    /**
     * Gets the double value representating the encodings.variable. It is used
     * in subclasses of <code>Variable</code> (i.e. <code>Real</code> and
     * <code>Int</code>). As not all objects belonging to a subclass of
     * <code>Variable</code> have a double value, a call to this method it is
     * considered a fatal error by default, and the program is terminated. Those
     * classes requiring this method must redefine it.
     * @return 
     */
    public double calculateSpacing(int p) throws Exception{
        Class cls = java.lang.String.class;
        String name = cls.getName();
        throw new Exception("Exception in " + name + ".calculateSpacing() => Class" + name + " does not implement method calculateSpacing()");
    } // calculateSpacing
    
    /**
     * Gets the double value representating the encodings.variable. It is used
     * in subclasses of <code>Variable</code> (i.e. <code>Real</code> and
     * <code>Int</code>). As not all objects belonging to a subclass of
     * <code>Variable</code> have a double value, a call to this method it is
     * considered a fatal error by default, and the program is terminated. Those
     * classes requiring this method must redefine it.
     * @return 
     */
    public double calculateSpacing(int index, int p) throws Exception{
        Class cls = java.lang.String.class;
        String name = cls.getName();
        throw new Exception("Exception in " + name + ".calculateSpacing() => Class" + name + " does not implement method calculateSpacing()");
    } // calculateSpacing
    
    /**
     * Gets the double value representating the encodings.variable. It is used
     * in subclasses of <code>Variable</code> (i.e. <code>Real</code> and
     * <code>Int</code>). As not all objects belonging to a subclass of
     * <code>Variable</code> have a double value, a call to this method it is
     * considered a fatal error by default, and the program is terminated. Those
     * classes requiring this method must redefine it.
     * @return 
     */
    public double AbsDiff(Variable var) throws Exception{
        Class cls = java.lang.String.class;
        String name = cls.getName();
        throw new Exception("Exception in " + name + ".AbsDiff(variable) => Class" + name + " does not implement method AbsDiff(variable)");
    } // AbsDiff
    
    /**
     * Gets the double value representating the encodings.variable. It is used
     * in subclasses of <code>Variable</code> (i.e. <code>Real</code> and
     * <code>Int</code>). As not all objects belonging to a subclass of
     * <code>Variable</code> have a double value, a call to this method it is
     * considered a fatal error by default, and the program is terminated. Those
     * classes requiring this method must redefine it.
     * @return 
     */
    public boolean isLowerUpperBound() throws Exception{
        Class cls = java.lang.String.class;
        String name = cls.getName();
        throw new Exception("Exception in " + name + ".isLowerUpperBound() => Class" + name + " does not implement method isLowerUpperBound()");
    } // isLowerUpperBound
    
    /**
     * Gets the double value representating the encodings.variable. It is used
     * in subclasses of <code>Variable</code> (i.e. <code>Real</code> and
     * <code>Int</code>). As not all objects belonging to a subclass of
     * <code>Variable</code> have a double value, a call to this method it is
     * considered a fatal error by default, and the program is terminated. Those
     * classes requiring this method must redefine it.
     * @return 
     */
    public boolean isLowerUpperBound(int index) throws Exception{
        Class cls = java.lang.String.class;
        String name = cls.getName();
        throw new Exception("Exception in " + name + ".isLowerUpperBound() => Class" + name + " does not implement method isLowerUpperBound()");
    } // isLowerUpperBound
    
    /**
     * Gets the double value representating the encodings.variable. It is used
     * in subclasses of <code>Variable</code> (i.e. <code>Real</code> and
     * <code>Int</code>). As not all objects belonging to a subclass of
     * <code>Variable</code> have a double value, a call to this method it is
     * considered a fatal error by default, and the program is terminated. Those
     * classes requiring this method must redefine it.
     * @return 
     */
    public ArrayList<ArrayList<Integer>> getPostionsOf(ArrayList<Variable> variable) throws Exception{
        Class cls = java.lang.String.class;
        String name = cls.getName();
        throw new Exception("Exception in " + name + ".getPostionsOf() => Class" + name + " does not implement method getNotExistingVariablesInDomain()");
    } // getPostionsOf
    
    /**
     * Gets the double value representating the encodings.variable. It is used
     * in subclasses of <code>Variable</code> (i.e. <code>Real</code> and
     * <code>Int</code>). As not all objects belonging to a subclass of
     * <code>Variable</code> have a double value, a call to this method it is
     * considered a fatal error by default, and the program is terminated. Those
     * classes requiring this method must redefine it.
     * @return 
     */
    public ArrayList<Variable> getNotExistingVariablesInDomain(ArrayList<Variable> variables) throws Exception{
        Class cls = java.lang.String.class;
        String name = cls.getName();
        throw new Exception("Exception in " + name + ".getNotExistingVariablesInDomain() => Class" + name + " does not implement method getNotExistingVariablesInDomain()");
    } // getNotExistingVariablesInDomain
    
    
    /**
     * Gets the double value representating the encodings.variable. It is used
     * in subclasses of <code>Variable</code> (i.e. <code>Real</code> and
     * <code>Int</code>). As not all objects belonging to a subclass of
     * <code>Variable</code> have a double value, a call to this method it is
     * considered a fatal error by default, and the program is terminated. Those
     * classes requiring this method must redefine it.
     * @return 
     */
    public ArrayList<Variable> getAllVariablesInDomain() throws Exception{
        Class cls = java.lang.String.class;
        String name = cls.getName();
        throw new Exception("Exception in " + name + ".getAllVariablesInDomain() => Class" + name + " does not implement method getNotExistingVariablesInDomain()");
    } // getAllVariablesInDomain
    
    /**
     * Gets the double value representating the encodings.variable. It is used
     * in subclasses of <code>Variable</code> (i.e. <code>Real</code> and
     * <code>Int</code>). As not all objects belonging to a subclass of
     * <code>Variable</code> have a double value, a call to this method it is
     * considered a fatal error by default, and the program is terminated. Those
     * classes requiring this method must redefine it.
     * @return 
     */
    public ArrayList<Variable> getAllVariablesInDomainExcludeBounds() throws Exception{
        Class cls = java.lang.String.class;
        String name = cls.getName();
        throw new Exception("Exception in " + name + ".getAllVariablesInDomainExcludeBounds() => Class" + name + " does not implement method getNotExistingVariablesInDomain()");
    } // getAllVariablesInDomainExcludeBounds
    
    /**
     * Gets the double value representating the encodings.variable. It is used
     * in subclasses of <code>Variable</code> (i.e. <code>Real</code> and
     * <code>Int</code>). As not all objects belonging to a subclass of
     * <code>Variable</code> have a double value, a call to this method it is
     * considered a fatal error by default, and the program is terminated. Those
     * classes requiring this method must redefine it.
     * @return 
     */
    public ArrayList<Variable> getAllVariablesInDomainExcludeLowerBounds() throws Exception{
        Class cls = java.lang.String.class;
        String name = cls.getName();
        throw new Exception("Exception in " + name + ".getAllVariablesInDomainExcludeLowerBounds() => Class" + name + " does not implement method getNotExistingVariablesInDomain()");
    } // getAllVariablesInDomainExcludeLowerBounds
    
    /**
     * Gets the double value representating the encodings.variable. It is used
     * in subclasses of <code>Variable</code> (i.e. <code>Real</code> and
     * <code>Int</code>). As not all objects belonging to a subclass of
     * <code>Variable</code> have a double value, a call to this method it is
     * considered a fatal error by default, and the program is terminated. Those
     * classes requiring this method must redefine it.
     * @return 
     */
    public ArrayList<Variable> getAllVariablesInDomainExcludeUpperBounds() throws Exception{
        Class cls = java.lang.String.class;
        String name = cls.getName();
        throw new Exception("Exception in " + name + ".getAllVariablesInDomainExcludeUpperBounds() => Class" + name + " does not implement method getNotExistingVariablesInDomain()");
    } // getAllVariablesInDomainExcludeUpperBounds
    
    /**
     * Gets the double value representating the encodings.variable. It is used
     * in subclasses of <code>Variable</code> (i.e. <code>Real</code> and
     * <code>Int</code>). As not all objects belonging to a subclass of
     * <code>Variable</code> have a double value, a call to this method it is
     * considered a fatal error by default, and the program is terminated. Those
     * classes requiring this method must redefine it.
     * @return 
     */
    public ArrayList<Variable> getDuplicateVariables() throws Exception{
        Class cls = java.lang.String.class;
        String name = cls.getName();
        throw new Exception("Exception in " + name + ".getDuplicateVariables() => Class" + name + " does not implement method getNotExistingVariablesInDomain()");
    } // getDuplicateVariables
    
    /**
     * Gets the double value representating the encodings.variable. It is used
     * in subclasses of <code>Variable</code> (i.e. <code>Real</code> and
     * <code>Int</code>). As not all objects belonging to a subclass of
     * <code>Variable</code> have a double value, a call to this method it is
     * considered a fatal error by default, and the program is terminated. Those
     * classes requiring this method must redefine it.
     * @return 
     */
    public ArrayList<Integer> getDuplicateVariablesIndex() throws Exception{
        Class cls = java.lang.String.class;
        String name = cls.getName();
        throw new Exception("Exception in " + name + ".getDuplicateVariablesIndex() => Class" + name + " does not implement method getNotExistingVariablesInDomain()");
    } // getDuplicateVariablesIndex
    
    /**
     * Gets the double value representating the encodings.variable. It is used
     * in subclasses of <code>Variable</code> (i.e. <code>Real</code> and
     * <code>Int</code>). As not all objects belonging to a subclass of
     * <code>Variable</code> have a double value, a call to this method it is
     * considered a fatal error by default, and the program is terminated. Those
     * classes requiring this method must redefine it.
     * @return 
     */
    public ArrayList<Integer> getDuplicateLowerBoundsIndex(int index) throws Exception{
        Class cls = java.lang.String.class;
        String name = cls.getName();
        throw new Exception("Exception in " + name + ".getDuplicateLowerBoundsIndex() => Class" + name + " does not implement method getNotExistingVariablesInDomain()");
    } // getDuplicateLowerBoundsIndex
    
    /**
     * Gets the double value representating the encodings.variable. It is used
     * in subclasses of <code>Variable</code> (i.e. <code>Real</code> and
     * <code>Int</code>). As not all objects belonging to a subclass of
     * <code>Variable</code> have a double value, a call to this method it is
     * considered a fatal error by default, and the program is terminated. Those
     * classes requiring this method must redefine it.
     * @return 
     */
    public ArrayList<Integer> getDuplicateUpperBoundsIndex(int index) throws Exception{
        Class cls = java.lang.String.class;
        String name = cls.getName();
        throw new Exception("Exception in " + name + ".getDuplicateUpperBoundsIndex() => Class" + name + " does not implement method getNotExistingVariablesInDomain()");
    } // getDuplicateUpperBoundsIndex
    
    /**
     * Gets the double value representating the encodings.variable. It is used
     * in subclasses of <code>Variable</code> (i.e. <code>Real</code> and
     * <code>Int</code>). As not all objects belonging to a subclass of
     * <code>Variable</code> have a double value, a call to this method it is
     * considered a fatal error by default, and the program is terminated. Those
     * classes requiring this method must redefine it.
     * @return 
     */
    public ArrayList<Integer> getLowerBoundsIndex(int index) throws Exception{
        Class cls = java.lang.String.class;
        String name = cls.getName();
        throw new Exception("Exception in " + name + ".getLowerBoundsIndex() => Class" + name + " does not implement method getNotExistingVariablesInDomain()");
    } // getDuplicateLowerBoundsIndex
    
    /**
     * Gets the double value representating the encodings.variable. It is used
     * in subclasses of <code>Variable</code> (i.e. <code>Real</code> and
     * <code>Int</code>). As not all objects belonging to a subclass of
     * <code>Variable</code> have a double value, a call to this method it is
     * considered a fatal error by default, and the program is terminated. Those
     * classes requiring this method must redefine it.
     * @return 
     */
    public ArrayList<Integer> getUpperBoundsIndex(int index) throws Exception{
        Class cls = java.lang.String.class;
        String name = cls.getName();
        throw new Exception("Exception in " + name + ".getUpperBoundsIndex() => Class" + name + " does not implement method getNotExistingVariablesInDomain()");
    } // getDuplicateUpperBoundsIndex
    
    /**
     * Gets the type of the encodings.variable. The types are defined in class
     * Problem.
     *
     * @return The type of the encodings.variable
     */
    public Class getVariableType() {
        return this.getClass();
    } // getVariableType
} // Variable
