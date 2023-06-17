package DomainLayer.Market.Users;

import DomainLayer.Market.Users.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Users_Admin")
public class Admin extends User {

    public Admin(String username, UUID id){
        super(username, id);
        isAdmin = true;
    }
    public Admin(){
        super();
    }
    public Admin(String username){
        super(username);
        isAdmin = true;
    }
}


