package ServiceLayer.ServiceObjects;

import DomainLayer.Market.Stores.PurchaseRule.ConditionalPurchaseTerm;

import java.util.UUID;

public class ServiceConditionalPurchaseTerm {
    private UUID termId;
    private ServicePurchaseTerm ifPurchaseTerm;
    private ServicePurchaseTerm thenPurchaseTerm;
    private String termType = "CONDITIONAL";
    
    
    public ServiceConditionalPurchaseTerm(UUID termId, ServicePurchaseTerm ifPurchaseTerm, ServicePurchaseTerm thenPurchaseTerm) {
        this.termId = termId;
        this.ifPurchaseTerm = ifPurchaseTerm;
        this.thenPurchaseTerm = thenPurchaseTerm;
    }
    
    public ServiceConditionalPurchaseTerm(ConditionalPurchaseTerm term) {
        this.termId = term.getTermId();
        ifPurchaseTerm = new ServicePurchaseTerm(term.getPurchaseTermIf());
        thenPurchaseTerm = new ServicePurchaseTerm(term.getPurchaseTermThen());
    }
    
    public ServicePurchaseTerm getIfPurchaseTerm() {
        return ifPurchaseTerm;
    }
    
    public ServicePurchaseTerm getThenPurchaseTerm() {
        return thenPurchaseTerm;
    }
    
    public void setIfPurchaseTerm(ServicePurchaseTerm ifPurchaseTerm) {
        this.ifPurchaseTerm = ifPurchaseTerm;
    }
    
    public void setThenPurchaseTerm(ServicePurchaseTerm thenPurchaseTerm) {
        this.thenPurchaseTerm = thenPurchaseTerm;
    }
    
    public String getTermType() {
        return termType;
    }
    
    public void setTermType(String termType) {
        this.termType = termType;
    }
    
    public UUID getTermId() {
        return termId;
    }
    
    public void setTermId(UUID termId) {
        this.termId = termId;
    }
}
