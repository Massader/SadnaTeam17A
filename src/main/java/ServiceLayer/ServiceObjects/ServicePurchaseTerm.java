package ServiceLayer.ServiceObjects;

import DomainLayer.Market.Stores.PurchaseRule.AtLeastPurchaseTerm;
import DomainLayer.Market.Stores.PurchaseRule.AtMostPurchaseTerm;
import DomainLayer.Market.Stores.PurchaseRule.PurchaseTerm;

public class ServicePurchaseTerm {
    private ServicePurchaseRule rule;
    private boolean atLeast;
    private int quantity;
    private String termType = "SIMPLE";
    
    
    public ServicePurchaseTerm(ServicePurchaseRule rule, boolean atLeast, int quantity) {
        this.rule = rule;
        this.atLeast = atLeast;
        this.quantity = quantity;
    }
    
    public ServicePurchaseTerm(PurchaseTerm term) {
        if (term instanceof AtLeastPurchaseTerm) {
            this.rule = new ServicePurchaseRule(term.getPurchaseRule());
            this.atLeast = true;
            this.quantity = ((AtLeastPurchaseTerm) term).getQuantity();
        }
        else if (term instanceof AtMostPurchaseTerm) {
            this.rule = new ServicePurchaseRule(term.getPurchaseRule());
            this.atLeast = false;
            this.quantity = ((AtMostPurchaseTerm) term).getQuantity();
        }
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
    
    public String getTermType() {
        return termType;
    }
    
    public void setTermType(String termType) {
        this.termType = termType;
    }
}
