package DomainLayer.Market.Users;

import DomainLayer.Market.Users.Roles.Role;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public class User extends Client{
    private String username;
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

    public void addStoreRole(Role role){
        this.roles.add(role);
    }

    public void removeStoreRole(UUID storeId){
        for (Role role : roles)
            if (role.getStoreId().equals(storeId)) {
                roles.remove(role);
                return;
            }
    }

    public void setPurchases(ConcurrentLinkedQueue<Purchase> purchases){
        this.purchases=purchases;
    }



}
