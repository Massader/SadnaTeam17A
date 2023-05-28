package DomainLayer.Market.Users;

import jakarta.persistence.*;
import org.hibernate.annotations.IdGeneratorType;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.util.UUID;

//@Entity
//@Table (name = "Clients")
public class Client {
//    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
//    @Column(name = "uuid", nullable = false)
//    private Long uuid;
//    @Column(name = "id")
    private UUID clientCredentials;

//    @OneToOne  (cascade = CascadeType.ALL)// cascade
    private ShoppingCart cart;

    public Client(UUID id){
        this.clientCredentials = id;
        cart = new ShoppingCart(id);

    }

    public Client(){
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
