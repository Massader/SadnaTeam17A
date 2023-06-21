package APILayer.Requests;

import java.util.UUID;

public class AcceptBidRequest extends Request {
    private UUID bidderId;
    private UUID storeId;
    private UUID itemId;
    private double bidPrice;
    
    public AcceptBidRequest(UUID clientCredentials, UUID bidderId, UUID storeId, UUID itemId, double bidPrice) {
        super(clientCredentials);
        this.bidderId = bidderId;
        this.storeId = storeId;
        this.itemId = itemId;
        this.bidPrice = bidPrice;
    }
    public AcceptBidRequest(){}
    
    public UUID getBidderId() {
        return bidderId;
    }
    
    public UUID getStoreId() {
        return storeId;
    }
    
    public UUID getItemId() {
        return itemId;
    }
    
    public void setBidderId(UUID bidderId) {
        this.bidderId = bidderId;
    }
    
    public void setStoreId(UUID storeId) {
        this.storeId = storeId;
    }
    
    public void setItemId(UUID itemId) {
        this.itemId = itemId;
    }
    
    public double getBidPrice() {
        return bidPrice;
    }
    
    public void setBidPrice(double bidPrice) {
        this.bidPrice = bidPrice;
    }
}
