package DomainLayer.Market.Stores.Discounts;

import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;
import jakarta.persistence.*;

@Entity
@Table(name = "Discounts_ShoppingBasketCalculateDiscount")
public class ShoppingBasketCalculateDiscount implements CalculateDiscount {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    public ShoppingBasketCalculateDiscount() {
    }

    @Override
    public Double calculateDiscount(ShoppingBasket shoppingBasket, Store store, Double discountPercentage) {
        if (discountPercentage > 1 || discountPercentage <= 0)
            return 0.0;
        Double price = store.calculatePriceOfBasket(shoppingBasket.getItems());
        return price * discountPercentage;
    }
}
