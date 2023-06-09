package DomainLayer.Market.Users.Roles;

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
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(name = "store_id")
    private UUID storeId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public Role(UUID storeId){
        this.storeId = storeId;
    }

    public Role(){
    }

    public abstract List<StorePermissions> getPermissions();

    public abstract void setPermissions(List<Integer> permissions);

    public abstract void addPermission(StorePermissions permission);

    public UUID getStoreId() {
        return storeId;
    }

    public User getUser() {
        return user;
    }

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
