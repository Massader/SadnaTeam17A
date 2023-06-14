package DomainLayer.Market.Stores.PurchaseTypes;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "Market_Stores_PurchaseTypes_DirectPurchase")
public class DirectPurchase extends PurchaseType {

    public DirectPurchase(String purchaseType) {
        super(purchaseType);
    }
    public DirectPurchase(){super();}

}
