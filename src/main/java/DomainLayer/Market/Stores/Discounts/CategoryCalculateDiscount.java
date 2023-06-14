package DomainLayer.Market.Stores.Discounts;

import DomainLayer.Market.Stores.Category;
import DomainLayer.Market.Stores.Item;
import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.CartItem;
import DomainLayer.Market.Users.ShoppingBasket;
import jakarta.persistence.*;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Table(name = "Market_Stores_Discounts_CategoryCalculateDiscount")
public class CategoryCalculateDiscount implements CalculateDiscount {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    Category category;


    public CategoryCalculateDiscount(Category category) {
        this.category = category;
    }

    public CategoryCalculateDiscount() {

    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public Double calculateDiscount(ShoppingBasket shoppingBasket, Store store, Double discountPercentage) {
        if (discountPercentage > 1 || discountPercentage <= 0){
            return 0.0;
        }
        double categoryDiscount = 0.0;
        Map<UUID, CartItem> items = shoppingBasket.getItems();
        for (CartItem cartItem : items.values()) {
            if (cartItem != null && cartItem.getItem() != null && cartItem.getItem().containsCategory(category.getCategoryName())) {
                int quantity = cartItem.getQuantity();
                categoryDiscount += cartItem.getPrice() * quantity * discountPercentage;
            }
        }
        return categoryDiscount;
    }
}
