package blp.core;

import java.util.HashMap;

/**
 *
 * @author abir
 */
public abstract class Operator {
    
    protected HashMap<String,Object> parameters;
    
    public Operator(HashMap<String,Object> parameters) {
        this.parameters = parameters;
    }
    
    public abstract Object[] execute(Object[] solutions);

    public HashMap<String, Object> getParameters() {
        return parameters;
    }
    
    public Object getParametre(String key) {
        return parameters.get(key);
    }

    public void setParameters(HashMap<String, Object> parameters) {
        this.parameters = parameters;
    }
    
    public void setParameter(String key, Object parameter) {
        this.parameters.replace(key, this.parameters.get(key), parameter);
    }
}
