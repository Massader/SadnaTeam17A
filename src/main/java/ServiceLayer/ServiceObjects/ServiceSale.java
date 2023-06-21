package ServiceLayer.ServiceObjects;

import DomainLayer.Market.Stores.Sale;
import java.util.Date;
import java.util.UUID;

public class ServiceSale {
    private UUID id;
    private UUID userId;
    private UUID storeId;
    private UUID itemId;
    private Date date;
    private int quantity;


    public ServiceSale(Sale sale) {
        this.id = sale.getId();
        this.userId = sale.getUserId();
        this.storeId = sale.getStore().getStoreId();
        this.itemId = sale.getItemId();
        this.date = sale.getDate();
        this.quantity = sale.getQuantity();
    }public ServiceSale(){}

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