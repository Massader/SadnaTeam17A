package DomainLayer.Market.Stores;

import java.util.UUID;

public class Complaint {
    private boolean open;
    private UUID userId;
    private UUID purchaseId;
    private UUID messageId;

    public Complaint(UUID userId, UUID purchaseId, UUID messageId){
        this.userId = userId;
        this.purchaseId = purchaseId;
        this.messageId = messageId;
    }

    public boolean isOpen(){
        return open;
    }

    public void closeComplaint(){
        this.open = false;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getPurchaseId() {
        return purchaseId;
    }

    public UUID getMessageId() {
        return messageId;
    }

}
