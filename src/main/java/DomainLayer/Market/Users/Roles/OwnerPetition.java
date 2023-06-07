package DomainLayer.Market.Users.Roles;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OwnerPetition {
    
    private UUID appointeeId;
    private UUID appointer;
    private UUID storeId;
    private List<UUID> ownersList;
    
    public OwnerPetition(UUID appointeeId, UUID appointer, UUID storeId) {
        this.appointeeId = appointeeId;
        this.appointer = appointer;
        this.storeId = storeId;
        ownersList = new ArrayList<>();
        ownersList.add(appointer);
    }
    
    public UUID getAppointeeId() {
        return appointeeId;
    }
    
    public UUID getAppointer() {
        return appointer;
    }
    
    public List<UUID> getOwnersList() {
        return ownersList;
    }
    
    public UUID getStoreId() {
        return storeId;
    }
    
    public List<UUID> approveAppointment(UUID ownerId) throws Exception {
        if (ownersList.contains(ownerId))
            throw new Exception("Owner already approved appointnment.");
        ownersList.add(ownerId);
        return ownersList;
    }
    
    public void removeApproval(UUID ownerId) throws Exception {
        if (!ownersList.contains(ownerId))
            throw new Exception("Owner has not approved of the appointment");
        ownersList.remove(ownerId);
    }
}
