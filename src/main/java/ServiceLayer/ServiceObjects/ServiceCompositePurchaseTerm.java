package ServiceLayer.ServiceObjects;

import DomainLayer.Market.Stores.PurchaseTypes.PurchaseRule.PurchaseTerm;

import java.util.List;
import java.util.UUID;

public class ServiceCompositePurchaseTerm {

    private List<ServicePurchaseTerm> purchaseTerms;
    private String type;
    
    public ServiceCompositePurchaseTerm(List<ServicePurchaseTerm> purchaseTerms, String type) {
        this.purchaseTerms = purchaseTerms;
        this.type = type;
    }
    
    public List<ServicePurchaseTerm> getPurchaseTerms() {
        return purchaseTerms;
    }
    
    public String getType() {
        return type;
    }
    
    public void setPurchaseTerms(List<ServicePurchaseTerm> purchaseTerms) {
        this.purchaseTerms = purchaseTerms;
    }
    
    public void setType(String type) {
        this.type = type;
    }
}
