package APILayer.Requests;

import ServiceLayer.ServiceObjects.ServiceDiscount;

import java.util.UUID;

public class DiscountRequest extends Request {
    private ServiceDiscount discount;
    private UUID storeId;
    
    public DiscountRequest(UUID clientCredentials, ServiceDiscount discount, UUID storeId) {
        super(clientCredentials);
        this.discount = discount;
        this.storeId = storeId;
    }
    public DiscountRequest(){}
    
    public ServiceDiscount getDiscount() {
        return discount;
    }
    
    public void setDiscount(ServiceDiscount discount) {
        this.discount = discount;
    }
    
    public UUID getStoreId() {
        return storeId;
    }
    
    public void setStoreId(UUID storeId) {
        this.storeId = storeId;
    }
}
