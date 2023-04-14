package ServiceLayer.ServiceObjects;

import DomainLayer.Market.Users.Purchase;
import DomainLayer.Market.Users.Roles.Role;
import DomainLayer.Market.Users.Roles.StorePermissions;
import DomainLayer.Market.Users.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ServiceUser {
    private String username;
    private Map<UUID,List<StorePermissions>> roles;

    public ServiceUser(User user){
        this.username = user.getUsername();
        this.roles = new HashMap<UUID,List<StorePermissions>>();
        for(Role role : user.getRoles())
            roles.put(role.getStoreId(), role.getPermissions());
    }
}
