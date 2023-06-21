package ServiceLayer.ServiceObjects;

import DomainLayer.Market.Complaint;

import java.util.UUID;

public class ServiceComplaint {
    private UUID id;
    private String body;
    private UUID sender;
    private boolean open;
    private UUID assignedAdmin;
    private UUID purchaseId;
    private UUID itemId;
    private UUID storeId;

    public ServiceComplaint(Complaint complaint) {
        this.id = complaint.getId();
        this.body = complaint.getBody();
        this.sender = complaint.getSender();
        this.open = complaint.isOpen();
        this.assignedAdmin = complaint.getAssignedAdmin();
        this.purchaseId = complaint.getPurchaseId();
        this.storeId = complaint.getStoreId();
        this.itemId = complaint.getItemId();
    }
    public ServiceComplaint(){}

    public UUID getId() {
        return id;
    }

    public String getBody() {
        return body;
    }

    public UUID getSender() {
        return sender;
    }

    public boolean getOpen() {
        return open;
    }

    public UUID getAssignedAdmin() {
        return assignedAdmin;
    }

    public UUID getPurchaseId() {
        return purchaseId;
    }
    
    public UUID getItemId() {
        return itemId;
    }
    
    public UUID getStoreId() {
        return storeId;
    }
}
