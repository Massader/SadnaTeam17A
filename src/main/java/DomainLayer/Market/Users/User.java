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
}
