package DomainLayer.Market.Stores.PurchaseTypes;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "Market_Stores_PurchaseTypes_AuctionPurchase")
public class AuctionPurchase extends PurchaseType {

    public AuctionPurchase(String purchaseType) {
        super(purchaseType);
    }
    public AuctionPurchase() {
        super();
    }

    // Additional properties and methods specific to AuctionPurchase
}
