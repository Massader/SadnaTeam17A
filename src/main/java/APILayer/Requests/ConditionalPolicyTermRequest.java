package APILayer.Requests;

import DomainLayer.Market.Stores.PurchaseTypes.PurchaseRule.PurchaseTerm;
import ServiceLayer.ServiceObjects.ServiceConditionalPurchaseTerm;

import java.util.List;
import java.util.UUID;

public class ConditionalPolicyTermRequest extends Request{
    
    private UUID storeId;
    private ServiceConditionalPurchaseTerm term;
    
    public ConditionalPolicyTermRequest(UUID clientCredentials, UUID storeId, ServiceConditionalPurchaseTerm term) {
        super(clientCredentials);
        this.term = term;
        this.storeId = storeId;
        this.term = term;
    }
    
    public ServiceConditionalPurchaseTerm getTerm() {
        return term;
    }
    
    public void setTerm(ServiceConditionalPurchaseTerm term) {
        this.term = term;
    }
    
    public UUID getStoreId() {
        return storeId;
    }
    
    public void setStoreId(UUID storeId) {
        this.storeId = storeId;
    }
}
