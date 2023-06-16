package DomainLayer.Market.Users;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "Users_Purchase")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;
    @Column
    private UUID itemId;
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date date;
    @Column
    private int quantity;
    @Column
    private boolean rated;
    @Column
    private UUID storeId;


    public Purchase(User user, UUID itemId, int quantity, UUID storeId) {
//        this.id = UUID.randomUUID();
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

    public void setId(UUID id) {
        this.id = id;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setItemId(UUID itemId) {
        this.itemId = itemId;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
