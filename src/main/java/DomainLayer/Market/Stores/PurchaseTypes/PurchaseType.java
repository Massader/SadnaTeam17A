package DomainLayer.Market.Stores.PurchaseTypes;

import jakarta.persistence.*;

@Entity
//@Table(name = "purchase_types")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class PurchaseType {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "type")
    private String type;
    public static final String DIRECT_PURCHASE = "DIRECT";
    public static final String BID_PURCHASE = "BID";
    public static final String AUCTION_PURCHASE = "AUCTION";
    public static final String LOTTERY_PURCHASE= "LOTTERY";

    protected PurchaseType(String type) {
        this.type = type;
    }

    protected PurchaseType() {
    }

    public String getType() {
        return type;
    }
    
    @Override
    public String toString() {
        return type;
    }
}
