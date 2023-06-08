package DomainLayer.Market.Stores.PurchaseTypes;

public abstract class PurchaseType {
    
    public static final String DIRECT_PURCHASE = "DIRECT";
    public static final String BID_PURCHASE = "BID";
    public static final String AUCTION_PURCHASE = "AUCTION";
    public static final String LOTTERY_PURCHASE= "LOTTERY";
    
    
    private final String type;
    
    public PurchaseType(String type) {
        this.type = type;
    }
    
    public String getType() {
        return type;
    }
    
    @Override
    public String toString() {
        return type;
    }
}
