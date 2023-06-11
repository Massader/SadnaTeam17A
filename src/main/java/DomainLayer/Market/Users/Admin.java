package DomainLayer.Market.Users;

import DomainLayer.Market.Users.User;

import java.util.List;
import java.util.UUID;

public class Admin extends User {

    public Admin(String username, UUID id){
        super(username, id);
        isAdmin=true;
    }

}
