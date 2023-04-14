package ServiceLayer.ServiceObjects;

import DomainLayer.Market.Users.Purchase;

import java.util.Date;
import java.util.UUID;

public class ServicePurchase {
    private UUID id;
    private UUID userId;
    private UUID itemId;
    private Date date;
    private int quantity;


    public ServicePurchase(Purchase purchase) {
        this.id = purchase.getId();
        this.userId = purchase.getUserId();
        this.itemId = purchase.getItemId();
        this.date = purchase.getDate();
        this.quantity = purchase.getQuantity();
    }

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
}
