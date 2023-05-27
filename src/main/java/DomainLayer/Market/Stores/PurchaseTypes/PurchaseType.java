package DomainLayer.Market.Stores.PurchaseTypes;

public abstract class PurchaseType {
    
    public static final String DIRECT_PURCHASE = "Direct Purchase";
    public static final String BID_PURCHASE = "Bid Purchase";
    public static final String AUCTION_PURCHASE = "Auction Purchase";
    public static final String LOTTERY_PURCHASE= "Lottery Purchase";
    
    
    private final String type;
    
    public PurchaseType(String type) {
        this.type = type;
    }
    
    public String getType() {
        return type;
    }
}
