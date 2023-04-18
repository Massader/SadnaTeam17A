package DomainLayer.Market;

import DomainLayer.Market.Users.Message;

import java.util.UUID;

public class Complaint extends Message {

    private boolean open;
    private UUID assignedAdmin;
    private UUID purchaseId;

    public Complaint(String body, UUID sender, UUID purchaseId) {
        super(body, sender, UserController.getInstance().getId("admin"));
        open = true;
        assignedAdmin = UserController.getInstance().getId("admin");
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
        open = false;
    }

    public void reopenComplaint() {
        open = true;
    }
}
