package DomainLayer.Market.Users.Roles;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "owner_petitions")
public class OwnerPetition {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "petition_id", nullable = false, unique = true)
    private UUID id;

    @Column(name = "appointee_id")
    private UUID appointeeId;

    @Column
    private UUID appointer;

    @Column(name = "store_id")
    private UUID storeId;

    @ElementCollection
    @CollectionTable(name = "owners_list",
            joinColumns = @JoinColumn(name = "petition_id"))
    @Column(name = "owner_id")
    private List<UUID> ownersList;

    public OwnerPetition(UUID appointeeId, UUID appointer, UUID storeId) {
        this.appointeeId = appointeeId;
        this.appointer = appointer;
        this.storeId = storeId;
        ownersList = new ArrayList<>();
        ownersList.add(appointer);
    }
    public OwnerPetition(){ownersList = new ArrayList<>();}
    
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
