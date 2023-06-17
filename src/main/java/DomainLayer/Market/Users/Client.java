package DomainLayer.Market.Users;

import jakarta.persistence.*;
import org.hibernate.annotations.IdGeneratorType;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.web.WebProperties;

import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "client_credentials", nullable = false, unique = true)
    private UUID clientCredentials;

//    @OneToOne  (fetch = FetchType.EAGER, cascade = CascadeType.ALL)// cascade
//@JoinColumn(name = "shopping_cart_id")
//    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "client_id")

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_id")
    private ShoppingCart cart;

    public Client(UUID id){
        this.clientCredentials = id;
//        cart = new ShoppingCart(id);

    }

    public Client(){
        cart = new ShoppingCart();
    }

    public void setCart(ShoppingCart cart){
        this.cart = cart;
    }

    public void setId(UUID id) {
        this.clientCredentials = id;
    }

    public UUID getId() {
        return clientCredentials;
    }

    public ShoppingCart getCart() {
        return cart;
    }

    public void clearCart(){
        cart.clearCart();
    }
}
