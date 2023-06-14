package DomainLayer.Market.Users.Roles;

import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.User;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public Role(Store store){
        this.store = store;
    }

    public Role(){
    }

    public abstract List<StorePermissions> getPermissions();

    public abstract void setPermissions(List<Integer> permissions);

    public abstract void addPermission(StorePermissions permission);

    public Store getStore() {
        return store;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user){
        this.user = user;
    }

    public void setStore(Store store)
    {
        this.store = store;
    }
//    public long getId

//    public void addUser(User user) {
//        users.add(user);
//        user.getRoles().add(this);
//    }
//
//    public void removeUser(User user) {
//        users.remove(user);
//        user.getRoles().remove(this);
//    }
}
