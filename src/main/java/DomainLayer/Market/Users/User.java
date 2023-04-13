package DomainLayer.Market.Users;

import DomainLayer.Market.Users.Roles.Role;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public class User extends Client{
    private String userName;
    private ConcurrentLinkedQueue<Role> roles;
    private ConcurrentLinkedQueue<Purchase> purchases;

    public User(String userName, UUID id){
        super(id);
        this.userName = userName;
        roles = new ConcurrentLinkedQueue<>();
        purchases = new ConcurrentLinkedQueue<>();
    }

    public String getUserName() {
        return userName;
    }
    public ConcurrentLinkedQueue<Role> getRoles() {
        return roles;
    }
    public ConcurrentLinkedQueue<Purchase> getPurchases() {
        return purchases;
    }

    public void setUserName(String userName){
        this.userName=userName;
    }
    public void setRoles(ConcurrentLinkedQueue<Role> roles){
        this.roles=roles;
    }

    public void addRole(Role role){
        this.roles.add(role);
    }


    public void setPurchases(ConcurrentLinkedQueue<Purchase> purchases){
        this.purchases=purchases;
    }



}
