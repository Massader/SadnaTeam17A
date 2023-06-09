package DomainLayer.Market.Stores.PurchaseTypes;

import jakarta.persistence.*;

@Entity
@Table(name = "purchase_types")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class PurchaseType {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(name = "type")
    private String type;
    public static final String DIRECT_PURCHASE = "Direct Purchase";
    public static final String BID_PURCHASE = "Bid Purchase";
    public static final String AUCTION_PURCHASE = "Auction Purchase";
    public static final String LOTTERY_PURCHASE= "Lottery Purchase";


    protected PurchaseType(String type) {
        this.type = type;
    }

    protected PurchaseType() {
    }

    public String getType() {
        return type;
    }
}
