package DomainLayer.Market.Stores.Discounts;

import DomainLayer.Market.Stores.Category;
import DomainLayer.Market.Stores.Item;
import DomainLayer.Market.Stores.Store;
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
        if (discountPercentage>1||discountPercentage<=0){
            return 0.0;
        }
    //    ConcurrentLinkedQueue<Double> DiscountOption= new ConcurrentLinkedQueue<>();
        double categoryDiscount = 0.0;
        Collection<Item> storeItems = store.getItems();
        Map<UUID, Integer> items = shoppingBasket.getItems();
        for (UUID itemId : items.keySet()) {
            Item item = store.getItem(itemId);
            if (item != null && item.containsCategory(category.getCategoryName())) {
                int quantity = items.get(itemId);
                categoryDiscount += item.getPrice() * quantity * discountPercentage;
            }
        }
      //  DiscountOption.add(categoryDiscount);

        return categoryDiscount;
    }
}
