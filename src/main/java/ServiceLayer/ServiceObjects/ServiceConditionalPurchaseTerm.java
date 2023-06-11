package ServiceLayer.ServiceObjects;

import java.util.List;

public class ServiceConditionalPurchaseTerm {
    private List<ServicePurchaseTerm> ifPurchaseTerms;
    private List<ServicePurchaseTerm> thenPurchaseTerms;
    
    public ServiceConditionalPurchaseTerm(List<ServicePurchaseTerm> ifPurchaseTerms, List<ServicePurchaseTerm> thenPurchaseTerms) {
        this.ifPurchaseTerms = ifPurchaseTerms;
        this.thenPurchaseTerms = thenPurchaseTerms;
    }
    
    public List<ServicePurchaseTerm> getIfPurchaseTerms() {
        return ifPurchaseTerms;
    }
    
    public List<ServicePurchaseTerm> getThenPurchaseTerms() {
        return thenPurchaseTerms;
    }
    
    public void setIfPurchaseTerms(List<ServicePurchaseTerm> ifPurchaseTerms) {
        this.ifPurchaseTerms = ifPurchaseTerms;
    }
    
    public void setThenPurchaseTerms(List<ServicePurchaseTerm> thenPurchaseTerms) {
        this.thenPurchaseTerms = thenPurchaseTerms;
    }
}
