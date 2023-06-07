package DomainLayer.Market.Users;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "Purchases")
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column
    private UUID itemId;
    @Column
    private Date date;
    @Column
    private int quantity;
    @Column
    private boolean rated;
    @Column
    private UUID storeId;


    public Purchase(User user, UUID itemId, int quantity, UUID storeId) {
        this.id = UUID.randomUUID();
        this.date = Date.from(Instant.now());
        this.itemId = itemId;
        this.user = user;
        this.quantity = quantity;
        this.storeId = storeId;
        rated = false;
    }
    public Purchase(){}

    public UUID getId() {
        return id;
    }

    public User getUser() {
        return user;
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

    public boolean isRated() {
        return rated;
    }

    public void setRated(boolean rated) {
        this.rated = rated;
    }


    public UUID getStoreId() {
        return storeId;
    }

    public void setStoreId(UUID storeId) {
        this.storeId = storeId;
    }
}
