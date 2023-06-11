package DomainLayer.Market.Users.Roles;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StoreFounder extends StoreOwner {

    public StoreFounder(UUID storeId){
        super(storeId);
    }

    @Override
    public List<StorePermissions> getPermissions() {
        List<StorePermissions> permissionList = new ArrayList<StorePermissions>();
        permissionList.add(StorePermissions.STORE_FOUNDER);
        permissionList.add(StorePermissions.STORE_OWNER);
        return permissionList;
    }
}
