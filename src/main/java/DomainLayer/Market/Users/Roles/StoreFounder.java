package DomainLayer.Market.Users.Roles;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Entity
@Table(name = "store_founders")
public class StoreFounder extends StoreOwner {

    public StoreFounder(UUID storeId){
        super(storeId);
    }

    public StoreFounder(){
        super();
    }

    @Override
    public List<StorePermissions> getPermissions() {
        List<StorePermissions> permissionList = new ArrayList<StorePermissions>();
        permissionList.add(StorePermissions.STORE_FOUNDER);
        permissionList.add(StorePermissions.STORE_OWNER);
        return permissionList;
    }
}
