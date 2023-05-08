package DomainLayer.Market.Users;

import DomainLayer.Market.Users.Roles.Role;
import DomainLayer.Market.Users.Roles.StorePermissions;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public class User extends Client{
    private final String username;
    private ConcurrentLinkedQueue<Role> roles;
    private ConcurrentLinkedQueue<Purchase> purchases;
    protected boolean isAdmin;

    public User(String username, UUID id){
        super(id);
        this.username = username;
        roles = new ConcurrentLinkedQueue<>();
        purchases = new ConcurrentLinkedQueue<>();
        isAdmin=false;
    }

    public String getUsername() {
        return username;
    }

    public boolean isAdmin(){
        return isAdmin;
    }

    public ConcurrentLinkedQueue<Role> getRoles() {
        return roles;
    }
    
    public ConcurrentLinkedQueue<Purchase> getPurchases() {
        return purchases;
    }

    public void setRoles(ConcurrentLinkedQueue<Role> roles){
        this.roles=roles;
    }

    public void addStoreRole(Role role) throws Exception {
        for (Role existingRole : roles) {
            if (existingRole.getStoreId().equals(role.getStoreId())) {
                if (role.getPermissions().contains(StorePermissions.STORE_OWNER)
                        && !existingRole.getPermissions().contains(StorePermissions.STORE_OWNER)) {
                    roles.add(role);
                    roles.remove(existingRole);
                    return;
                } else {
                    throw new Exception("User is already a member of store staff.");
                }
            }
        }
        roles.add(role);
    }

    public void removeStoreRole(UUID storeId){
        for (Role role : roles)
            if (role.getStoreId().equals(storeId)) {
                roles.remove(role);
                return;
            }
    }
    
    public void addPurchase(Purchase purchase){this.purchases.add(purchase);}
    
    public void removePurchase(Purchase purchase){this.purchases.remove(purchase);}

    public void setPurchases(ConcurrentLinkedQueue<Purchase> purchases){
        this.purchases=purchases;
    }



}
