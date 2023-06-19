package ServiceLayer.StateFileRunner;

import java.util.List;

public class Function {
    private String name;
    private List<Object> arguments;
    private String retVal;
    
    public Function(String name, List<Object> arguments, String retVal) {
        this.name = name;
        this.arguments = arguments;
        this.retVal = retVal;
    }
    
    public Function() {
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public List<Object> getArguments() {
        return arguments;
    }
    
    public void setArguments(List<Object> arguments) {
        this.arguments = arguments;
    }
    
    public String getRetVal() {
        return retVal;
    }
    
    public void setRetVal(String retVal) {
        this.retVal = retVal;
    }
}
