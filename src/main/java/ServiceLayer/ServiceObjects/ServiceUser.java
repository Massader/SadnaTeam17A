package ServiceLayer.ServiceObjects;

import DomainLayer.Market.Users.Roles.Role;
import DomainLayer.Market.Users.Roles.StorePermissions;
import DomainLayer.Market.Users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class ServiceUser {
    private UUID id;
    private String username;
    private Map<UUID,List<StorePermissions>> roles;
    private boolean isAdmin;

    public ServiceUser() {}

    public ServiceUser(User user){
        this.id = user.getId();
        this.username = user.getUsername();
        this.roles = new HashMap<>();
        for(Role role : user.getRoles())
            roles.put(role.getStoreId(), role.getPermissions());
        this.isAdmin = user.isAdmin();
    }
    @Autowired
    public ServiceUser(String username) {
        this.username = username;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Map<UUID, List<StorePermissions>> getRoles() {
        return roles;
    }

    public boolean IsAdmin() {
        return isAdmin;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRoles(Map<UUID, List<StorePermissions>> roles) {
        this.roles = roles;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}
