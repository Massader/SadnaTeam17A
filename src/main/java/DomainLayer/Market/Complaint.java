package DomainLayer.Market;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "Market_Complaint")
public class Complaint extends Message {
    @Transient
    private boolean open;
    @Transient
    private UUID assignedAdmin;
    @Transient
    private UUID purchaseId;
    @Transient
    private UUID storeId;
    @Transient
    private UUID itemId;

    public Complaint(String body, UUID sender, UUID purchaseId, UUID storeId, UUID itemId) {
        super(body, sender, UserController.getInstance().getId("admin"));
        open = true;
        assignedAdmin = UserController.getInstance().getId("admin");
        this.purchaseId = purchaseId;
        this.storeId = storeId;
        this.itemId = itemId;
    }

    public Complaint() {

    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public void setAssignedAdmin(UUID assignedAdmin) {
        this.assignedAdmin = assignedAdmin;
    }

    public void setPurchaseId(UUID purchaseId) {
        this.purchaseId = purchaseId;
    }

    public boolean isOpen() {
        return open;
    }

    public UUID getAssignedAdmin() {
        return assignedAdmin;
    }

    public UUID getPurchaseId() {
        return purchaseId;
    }

    public void assignAdmin(UUID adminId) {
        assignedAdmin = adminId;
    }

    public void closeComplaint() {
        if (!open) throw new RuntimeException("Complaint already closed.");
        open = false;
    }

    public void reopenComplaint() {
        if (open) throw new RuntimeException("Complaint already open.");
        open = true;
    }
    
    public UUID getStoreId() {
        return storeId;
    }
    
    public void setStoreId(UUID storeId) {
        this.storeId = storeId;
    }
    
    public UUID getItemId() {
        return itemId;
    }
    
    public void setItemId(UUID itemId) {
        this.itemId = itemId;
    }
}
