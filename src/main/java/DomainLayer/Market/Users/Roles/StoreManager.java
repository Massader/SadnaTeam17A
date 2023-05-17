package DomainLayer.Market.Users.Roles;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StoreManager extends Role{
    private List<StorePermissions> permissionsList;

    public StoreManager(UUID storeId) {
        super(storeId);
        permissionsList = new ArrayList<StorePermissions>();
        permissionsList.add(StorePermissions.STORE_MANAGEMENT_INFORMATION);
        permissionsList.add(StorePermissions.STORE_COMMUNICATION);
        permissionsList.add(StorePermissions.STORE_SALE_HISTORY);
    }

    @Override
    public List<StorePermissions> getPermissions() {
        return permissionsList;
    }

    @Override
    public void addPermission(StorePermissions permission) {
        if(!permissionsList.contains(permission))
            permissionsList.add(permission);
    }
    
    @Override
    public void setPermissions(List<Integer> permissions) {
        for (int i = 0; i < StorePermissions.values().length; i++) {
            StorePermissions permission = StorePermissions.values()[i];
            if (permission != StorePermissions.STORE_FOUNDER
                    && permission != StorePermissions.STORE_OWNER) {
                if (permissions.contains(i) && !permissionsList.contains(permission)) {
                    permissionsList.add(permission);
                }
                else if (!permissions.contains(i) && permissionsList.contains(permission)) {
                    permissionsList.remove(permission);
                }
            }
        }
    }
}
