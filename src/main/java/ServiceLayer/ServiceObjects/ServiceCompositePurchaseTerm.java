package ServiceLayer.ServiceObjects;

import DomainLayer.Market.Stores.PurchaseRule.*;

import java.util.ArrayList;
import java.util.List;

public class ServiceCompositePurchaseTerm {

    private List<ServicePurchaseTerm> purchaseTerms;
    private String type;
    
    public ServiceCompositePurchaseTerm(List<ServicePurchaseTerm> purchaseTerms, String type) {
        this.purchaseTerms = purchaseTerms;
        this.type = type;
    }
    
    public ServiceCompositePurchaseTerm(CompositePurchaseTerm term) {
        this.purchaseTerms = new ArrayList<>();
        for (PurchaseTerm purchaseTerm : term.getPurchaseTerms()) {
            if (purchaseTerm instanceof AtLeastPurchaseTerm)
                purchaseTerms.add(new ServicePurchaseTerm(new ServicePurchaseRule(purchaseTerm.getPurchaseRule()), true,
                        ((AtLeastPurchaseTerm) purchaseTerm).getQuantity()));
            else if (purchaseTerm instanceof AtMostPurchaseTerm)
                purchaseTerms.add(new ServicePurchaseTerm(new ServicePurchaseRule(purchaseTerm.getPurchaseRule()), false,
                        ((AtMostPurchaseTerm) purchaseTerm).getQuantity()));
        }
        if (term instanceof CompositePurchaseTermAnd)
            this.type = "AND";
        else if (term instanceof CompositePurchaseTermOr)
            this.type = "OR";
        else
            this.type = "XOR";
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
