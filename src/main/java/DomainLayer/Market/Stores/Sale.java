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
//
//    @Column(name = "store_id")
//    private UUID storeId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(name = "item_id")
    private UUID itemId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date date;

    @Column
    private int quantity;


    public Sale(UUID userId, UUID storeId, UUID itemId, int quantity, Store store) {
//        this.id = UUID.randomUUID();
        this.date = Date.from(Instant.now());
        this.itemId = itemId;
//        this.storeId = storeId;
        this.userId = userId;
        this.quantity = quantity;
        this.store = store;
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

    public Store getStore() {
        return store;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setItemId(UUID itemId) {
        this.itemId = itemId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
