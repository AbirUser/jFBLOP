package blp.core;

import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @author abir
 */
public abstract class Algorithm implements Serializable {

    private Problem problem;
    private HashMap<String, Operator> operators = null;
    private HashMap<String, Object> inputParameters = null;
    private HashMap<String, Object> outPutParameters = null;

    public Algorithm(Problem getget) {
        this.problem = problem;
    }

    public abstract SolutionSet execute();

    public void addOperator(String name, Operator operator) {
        if (operators == null) {
            operators = new HashMap();
        }
        operators.put(name, operator);
    }

    public Operator getOperator(String name) {
        return operators.get(name);
    }
    
    public void setOperator(String name, Operator operator) {
        this.operators.replace(name, operator);
    }
    
    public void setOperator(HashMap<String, Operator> operators) {
        this.operators = operators;
    }
    
    public void setInputParameter(HashMap<String, Object> inputParameters) {
        this.inputParameters = inputParameters;
    }

    public void setInputParameter(String name, Object object) {
        if (inputParameters == null) {
            inputParameters = new HashMap();
        }
        inputParameters.put(name, object);
    }

    public Object getInputParameter(String name) {
        return inputParameters.get(name);
    }
    
    public void setOutputParameter(HashMap<String, Object> outPutParameters) {
        this.outPutParameters = outPutParameters;
    }

    public void setOutputParameter(String name, Object object) {
        if (outPutParameters == null) {
            outPutParameters = new HashMap();
        }
        outPutParameters.put(name, object);
    }

    public Object getOutputParameter(String name) {
        if (outPutParameters != null) {
            return outPutParameters.get(name);
        } else {
            return null;
        }
    }
    
    public Problem getProblem() {
        return problem;
    }
}
