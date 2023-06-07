package DomainLayer.Market.Users.Roles;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Entity
@Table(name = "store_owners")
public class StoreOwner extends Role {

    @ElementCollection
    @Column(name = "appointee_id")
    private List<UUID> appointees;

    public StoreOwner(UUID storeId) {
        super(storeId);
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
    
    public List<UUID> getAppointees() {
        return appointees;
    }

    public void addAppointee(UUID appoint){
        appointees.add(appoint);
    }

    public void removeAppointee(UUID appoint){
        appointees.remove(appoint);
    }
}
