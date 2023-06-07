package ServiceLayer.ServiceObjects;

import DomainLayer.Market.Stores.PurchaseRule.ConditionalPurchaseTerm;

import java.util.List;

public class ServiceConditionalPurchaseTerm {
    private ServicePurchaseTerm ifPurchaseTerm;
    private ServicePurchaseTerm thenPurchaseTerm;
    
    public ServiceConditionalPurchaseTerm(ServicePurchaseTerm ifPurchaseTerm, ServicePurchaseTerm thenPurchaseTerm) {
        this.ifPurchaseTerm = ifPurchaseTerm;
        this.thenPurchaseTerm = thenPurchaseTerm;
    }
    
    public ServiceConditionalPurchaseTerm(ConditionalPurchaseTerm term) {
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
}
