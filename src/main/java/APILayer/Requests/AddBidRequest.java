package APILayer.Requests;

import java.util.UUID;

public class AddBidRequest extends Request {
    private UUID storeId;
    private UUID itemId;
    private int quantity;
    private double bidPrice;
    
    public AddBidRequest(UUID clientCredentials, UUID storeId, UUID itemId, int quantity, double bidPrice) {
        super(clientCredentials);
        this.storeId = storeId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.bidPrice = bidPrice;
    }
    public AddBidRequest(){}
    
    public UUID getStoreId() {
        return storeId;
    }
    
    public UUID getItemId() {
        return itemId;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public double getBidPrice() {
        return bidPrice;
    }
    
    public void setStoreId(UUID storeId) {
        this.storeId = storeId;
    }
    
    public void setItemId(UUID itemId) {
        this.itemId = itemId;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public void setBidPrice(double bidPrice) {
        this.bidPrice = bidPrice;
    }
}
