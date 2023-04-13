package DomainLayer.Market.Users.Roles;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StoreOwner extends Role {

    private List<UUID> appoints;

    public StoreOwner(UUID storeId) {
        super(storeId);
        appoints = new ArrayList<UUID>();
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

    public List<UUID> getAppoints() {
        return appoints;
    }

    public void addAppoint(UUID appoint){
        appoints.add(appoint);
    }

    public void removeAppoint(UUID appoint){
        appoints.remove(appoint);
    }
}
