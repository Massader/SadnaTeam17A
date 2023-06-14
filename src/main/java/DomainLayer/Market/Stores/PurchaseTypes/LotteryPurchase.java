package DomainLayer.Market.Stores.PurchaseTypes;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "Market_Stores_PurchaseTypes_LotteryPurchase")
public class LotteryPurchase extends PurchaseType {

    public LotteryPurchase(String purchaseType) {
        super(purchaseType);
    }
    public LotteryPurchase(){super();}

    // Additional properties and methods specific to LotteryPurchase
}
