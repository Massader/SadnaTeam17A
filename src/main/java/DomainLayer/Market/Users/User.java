package DomainLayer.Market.Users;

import DataAccessLayer.controllers.UserDalController;
import DomainLayer.Market.UserController;
import DomainLayer.Market.Users.Roles.OwnerPetition;
import DomainLayer.Market.Users.Roles.Role;
import DomainLayer.Market.Users.Roles.StorePermissions;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import java.util.*;

import java.util.concurrent.ConcurrentLinkedQueue;

@Entity
@Table(name = "Users_User")
public class User extends Client{

    @Column(unique = true)
    private String username;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Collection<Role> roles;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Collection<Purchase> purchases;
    @Column
    protected boolean isAdmin;



    public User(String username, UUID id){
        super(id);
        this.username = username;
        roles = new ConcurrentLinkedQueue<>();

        purchases = new ConcurrentLinkedQueue<>();
        isAdmin=false;

//        userDalController = UserDalController.getInstance(UserController.repositoryFactory);
    }

    public User(String username){
        super();
        this.username = username;
        roles = new ConcurrentLinkedQueue<>();
        purchases = new ConcurrentLinkedQueue<>();
        isAdmin=false;
//        userDalController = UserDalController.getInstance(UserController.repositoryFactory);
    }

    public User()
    {
        purchases = new ConcurrentLinkedQueue<>();
        roles = new ConcurrentLinkedQueue<>();
//        userDalController = UserDalController.getInstance(UserController.repositoryFactory);
    }
    public String getUsername() {
        return username;
    }

    public boolean isAdmin(){
        return isAdmin;
    }

    public Collection<Role> getRoles() {
        return roles;
    }
    
    public Collection<Purchase> getPurchases() {
        return purchases;
    }

    public void setRoles(ConcurrentLinkedQueue<Role> roles){
        this.roles=roles;
    }
    public void addRole(Role role ){
        roles.add(role);
    }

    @Transactional
    public void addStoreRole(Role role) throws Exception {
//        Collection<Role> currRoles = getRoles();
//        for (Role existingRole : currRoles) {
//            if (existingRole.getStore().getStoreId().equals(role.getStore().getStoreId())) {
//                if (role.getPermissions().contains(StorePermissions.STORE_OWNER)
//                        && !existingRole.getPermissions().contains(StorePermissions.STORE_OWNER)) {
//                    roles.add(role);
//                    roles.remove(existingRole);
//                    return;
//                } else {
//                    throw new Exception("User is already a member of store staff.");
//                }
//            }
//        }
        roles.add(role);
        role.setUser(this);
//        userDalController.saveUser(this);
    }

    public void removeStoreRole(UUID storeId){
        for (Role role : roles)
            if (role.getStore().getStoreId().equals(storeId)) {
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
