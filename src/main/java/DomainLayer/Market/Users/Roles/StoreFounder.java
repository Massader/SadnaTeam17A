package DomainLayer.Market.Users.Roles;

import java.util.UUID;

public class StoreFounder extends Role {
    private UUID storeId;

    public StoreFounder(UUID storeId){
        this.storeId = storeId;
    }
}
