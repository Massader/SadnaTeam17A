package DomainLayer.Market.Stores.PurchaseTypes;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "auction_purchases")
public class AuctionPurchase extends PurchaseType {

    public AuctionPurchase(String purchaseType) {
        super(purchaseType);
    }
    public AuctionPurchase() {
        super();
    }

    // Additional properties and methods specific to AuctionPurchase
}
