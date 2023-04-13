package DomainLayer.Market.Users.Roles;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Role {
    private UUID storeId;

    public Role(UUID storeId){
        this.storeId = storeId;
    }

    public abstract List<StorePermissions> getPermissions();

}
