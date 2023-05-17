package DomainLayer.Market.Stores;

import java.util.*;

import DomainLayer.Market.Stores.Discounts.condition.*;
import DomainLayer.Market.Stores.PurchaseTypes.PurchaseRule.*;
import DomainLayer.Market.Users.Client;
import DomainLayer.Market.Users.Purchase;
import DomainLayer.Market.Users.Roles.Role;
import DomainLayer.Market.Users.Roles.StoreOwner;
import DomainLayer.Market.Users.Roles.StorePermissions;
import DomainLayer.Market.Users.ShoppingBasket;
import DomainLayer.Market.Users.User;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.persistence.*;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootApplication
@Entity(name="dummy12")
@Table
public class dummy1 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
//    public dummy1(){
//        id = 1;
//        name ="p";
//        description = "hallo";
//    }
//    public static void main(String[] args){
//        System.out.println("print");
//    }

}
