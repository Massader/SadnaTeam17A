package DomainLayer.Market.Users;

import DomainLayer.Market.Users.Roles.Role;
import DomainLayer.Market.Users.Roles.StoreManager;
import DomainLayer.Market.Users.Roles.StoreOwner;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class User extends Client{
    private String userName;
    private ConcurrentHashMap<UUID,ConcurrentLinkedQueue<Role>> roles;//storeID, role
    private ConcurrentLinkedQueue<Purchase> purchases;

    public User(String userName, UUID id){
        super(id);
        this.userName = userName;
        roles = new ConcurrentHashMap<>();
        purchases = new ConcurrentLinkedQueue<>();
    }

    public String getUserName() {
        return userName;
    }
    public ConcurrentHashMap<UUID,ConcurrentLinkedQueue<Role>> getRoles() {
        return roles;
    }
    public ConcurrentLinkedQueue<Purchase> getPurchases() {
        return purchases;
    }

    public void setUserName(String userName){
        this.userName=userName;
    }
    public void setRoles(ConcurrentHashMap<UUID,ConcurrentLinkedQueue<Role>> roles){
        this.roles=roles;
    }

    public void addRole(UUID storeID, Role role){
        ConcurrentLinkedQueue<Role> myRole;
        if(!roles.containsKey(storeID)){
            myRole= new ConcurrentLinkedQueue<Role>();}
        else {
            myRole= roles.get(storeID);}
        myRole.add(role);
        roles.put(storeID,myRole);
    }


    public void setPurchases(ConcurrentLinkedQueue<Purchase> purchases){
        this.purchases=purchases;
    }
    public ConcurrentLinkedQueue<Role> getRoleInStore(UUID storeId){
        if(roles.containsKey(storeId)) {return roles.get(storeId);}
        return null;
    }

    public boolean isStoreManager(UUID storeId) {
        for (Role role:getRoleInStore(storeId)) {
            if(role.getClass()== StoreManager.class) return true;}
        return  false;
        }

     public Role getStoreManager(UUID storeId){
         for (Role role:getRoleInStore(storeId)) {
             if(role.getClass()== StoreManager.class) return role;}
         return  null;
     }
    public boolean isStoreOwner(UUID storeId) {
        for (Role role:getRoleInStore(storeId)) {
            if(role.getClass()== StoreOwner.class) return true;}
        return  false;
    }

    public Role getStoreOwner(UUID storeId){
        for (Role role:getRoleInStore(storeId)) {
            if(role.getClass()== StoreOwner.class) return role;}
        return  null;
    }

    public Boolean removeStoreOwnerRole(UUID storeId){
        ConcurrentLinkedQueue<Role> oldRole = getRoleInStore(storeId);
        for (Role role:oldRole) {
            if(role.getClass()== StoreOwner.class) oldRole.remove(role);
            roles.put(storeId,oldRole);
            return true;}
        return  false;
    }

    public Boolean removeStoreManagerRole(UUID storeId){
        ConcurrentLinkedQueue<Role> oldRole = getRoleInStore(storeId);
        for (Role role:oldRole) {
            if(role.getClass()== StoreManager.class) oldRole.remove(role);
            roles.put(storeId,oldRole);
            return true;}
        return  false;
    }

        }





