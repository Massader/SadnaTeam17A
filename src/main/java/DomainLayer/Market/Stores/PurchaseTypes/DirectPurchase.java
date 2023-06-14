package DomainLayer.Market.Stores.PurchaseTypes;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "direct_purchases")
public class DirectPurchase extends PurchaseType {

    public DirectPurchase(String purchaseType) {
        super(purchaseType);
    }
    public DirectPurchase(){super();}

}
