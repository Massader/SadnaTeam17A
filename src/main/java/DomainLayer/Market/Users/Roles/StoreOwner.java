package DomainLayer.Market.Users.Roles;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StoreOwner extends Role {

    private List<UUID> appointees;

    public StoreOwner(UUID storeId) {
        super(storeId);
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
