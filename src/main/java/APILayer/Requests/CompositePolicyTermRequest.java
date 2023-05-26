package APILayer.Requests;

import ServiceLayer.ServiceObjects.ServiceCompositePurchaseTerm;

import java.util.UUID;

public class CompositePolicyTermRequest extends Request{
    
    private UUID storeId;
    private ServiceCompositePurchaseTerm term;
    
    public CompositePolicyTermRequest(UUID clientCredentials, UUID storeId, ServiceCompositePurchaseTerm term) {
        super(clientCredentials);
        this.term = term;
        this.storeId = storeId;
    }
    
    public ServiceCompositePurchaseTerm getTerm() {
        return term;
    }
    
    public void setTerm(ServiceCompositePurchaseTerm term) {
        this.term = term;
    }
    
    public UUID getStoreId() {
        return storeId;
    }
    
    public void setStoreId(UUID storeId) {
        this.storeId = storeId;
    }
}
