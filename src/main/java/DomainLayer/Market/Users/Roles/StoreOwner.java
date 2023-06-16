package DomainLayer.Market.Users.Roles;

import DomainLayer.Market.Stores.Store;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
@Entity
@Table(name = "Roles_StoreOwner")
public class StoreOwner extends Role {

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "appointee_id")
    private Collection<UUID> appointees;

    public StoreOwner(Store store) {
        super(store);
        appointees = new ArrayList<UUID>();
    }
    public StoreOwner(){
        super();
        appointees = new ArrayList<UUID>();
    }

    @Override
    public List<StorePermissions> getPermissions() {
        List<StorePermissions> permissionList = new ArrayList<StorePermissions>();
        permissionList.add(StorePermissions.STORE_OWNER);
        return permissionList;
    }

    @Override
    public void addPermission(StorePermissions permission) {
    }
    
    @Override
    public void setPermissions(List<Integer> permissions) {
    
    }
    
    public Collection<UUID> getAppointees() {
        return appointees;
    }

    public void addAppointee(UUID appoint){
        appointees.add(appoint);
    }

    public void removeAppointee(UUID appoint){
        appointees.remove(appoint);
    }

    public void setAppointees(Collection<UUID> appointees) {
        this.appointees = appointees;
    }
}
