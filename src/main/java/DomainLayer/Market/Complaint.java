package DomainLayer.Market;

import java.util.UUID;

public class Complaint extends Message {

    private boolean open;
    private UUID assignedAdmin;
    private UUID purchaseId;
    private UUID storeId;
    private UUID itemId;

    public Complaint(String body, UUID sender, UUID purchaseId, UUID storeId, UUID itemId) {
        super(body, sender, UserController.getInstance().getId("admin"));
        open = true;
        assignedAdmin = UserController.getInstance().getId("admin");
        this.purchaseId = purchaseId;
        this.storeId = storeId;
        this.itemId = itemId;
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
