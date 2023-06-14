package DomainLayer.Market.Stores;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "Stores_Sale")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "sale_id", nullable = false, unique = true)
    private UUID id;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "store_id")
    private UUID storeId;

    @Column(name = "item_id")
    private UUID itemId;

    @Column
    private Date date;

    @Column
    private int quantity;


    public Sale(UUID userId, UUID storeId, UUID itemId, int quantity) {
        this.id = UUID.randomUUID();
        this.date = Date.from(Instant.now());
        this.itemId = itemId;
        this.storeId = storeId;
        this.userId = userId;
        this.quantity = quantity;
    }
    public Sale(){}

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getItemId() {
        return itemId;
    }

    public Date getDate() {
        return date;
    }

    public int getQuantity() {
        return quantity;
    }

    public UUID getStoreId() {
        return storeId;
    }
}
