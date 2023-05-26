package ServiceLayer.ServiceObjects;

public class ServicePurchaseTerm {
    private ServicePurchaseRule rule;
    private boolean atLeast;
    private int quantity;
    
    public ServicePurchaseTerm(ServicePurchaseRule rule, boolean atLeast, int quantity) {
        this.rule = rule;
        this.atLeast = atLeast;
        this.quantity = quantity;
    }
    
    public ServicePurchaseRule getRule() {
        return rule;
    }
    
    public void setRule(ServicePurchaseRule rule) {
        this.rule = rule;
    }
    
    public boolean getAtLeast() {
        return atLeast;
    }
    
    public void setAtLeast(boolean atLeast) {
        this.atLeast = atLeast;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
